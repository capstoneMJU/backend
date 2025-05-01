package capstone.mju.backend.domain.recipe.service;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import capstone.mju.backend.domain.recipe.dto.request.RecipeSuggestionRequest;
import capstone.mju.backend.domain.recipe.dto.request.ScrapRecipeRequest;
import capstone.mju.backend.domain.recipe.dto.response.*;
import capstone.mju.backend.domain.recipe.repository.IngredientRepository;
import capstone.mju.backend.domain.recipe.domain.Recipe;
import capstone.mju.backend.domain.recipe.dto.request.RecipeDto;
import capstone.mju.backend.domain.recipe.repository.RecipeRepository;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final OpenAiService openAiService;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    /*
     레시피 생성
     */
    public RecipeResponse createLowCalorieRecipe(RecipeDto request) {
        String ingredientsText = request.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.joining(", "));

        RecipeResult recipeResult = openAiService.createRecipePromptAndTitle(ingredientsText);

        List<Ingredient> ingredients = request.getIngredients().stream()
                .map(ingredient -> Ingredient.builder()
                        .name(ingredient.getName())
                        .build()
                )
                .collect(Collectors.toList());

        Recipe recipe = Recipe.builder()
                .title(recipeResult.getTitle())
                .requiredIngredients(ingredients)
                .recipeContent(recipeResult.getSteps())
                .build();

        return RecipeResponse.of(recipe.getId(), recipe.getTitle(), recipe.getRequiredIngredients(), recipe.getRecipeContent());
    }

    /*
    레시피 단건 조회
     */
    public RecipeResponse getRecipeById(UUID recipeId) {
        Recipe recipe = findRecipeByIdOrThrow(recipeId);

        return RecipeResponse.of(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getRequiredIngredients(),
                recipe.getRecipeContent()
        );
    }

    // 스크랩한 모든 레시피들 조회
    public RecipeListResponse getAllRecipes(User user) {
        List<Recipe> recipes = recipeRepository.findAll();

        List<RecipeResponse> recipeResponses = recipes.stream()
                .map(recipe -> RecipeResponse.of(
                        recipe.getId(),
                        recipe.getTitle(),
                        recipe.getRequiredIngredients(),
                        recipe.getRecipeContent()
                ))
                .collect(Collectors.toList());

        return RecipeListResponse.from(recipeResponses);
    }

    /*
    레시피 스크랩
     */
    @Transactional
    public ScrapRecipeResponse scrapRecipe(User user, ScrapRecipeRequest request) {
        List<Ingredient> ingredients = request.getIngredients().stream()
                .map(ingredientReq -> Ingredient.builder()
                        .name(ingredientReq.getName())
                        .build())
                .map(ingredientRepository::save)
                .collect(Collectors.toList());

        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .requiredIngredients(ingredients)
                .recipeContent(request.getSteps())
                .build();

        recipeRepository.save(recipe);

        return ScrapRecipeResponse.builder()
                .recipeId(recipe.getId())
                .title(recipe.getTitle())
                .ingredients(ingredients.stream()
                        .map(ingredient -> ScrapRecipeResponse.IngredientSimpleResponse.builder()
                                .name(ingredient.getName())
                                .build())
                        .collect(Collectors.toList()))
                .steps(recipe.getRecipeContent())
                .build();
    }

    /*
    스크랩한 레시피 삭제
     */
    public void deleteRecipe(UUID recipeId) {
        Recipe recipe = findRecipeByIdOrThrow(recipeId);
        recipeRepository.deleteById(recipeId);
    }

    /*
    사용자의 레시피에 대한 권한 validation
     */
    private Recipe findRecipeByIdOrThrow(UUID recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
    }

    public RecipeSuggestionListResponse suggestRecipes(RecipeSuggestionRequest request) {
        List<RecipeSuggestionResponse> suggestions = openAiService.generateRecipeSuggestions(request.getIngredients());
        return RecipeSuggestionListResponse.builder()
                .suggestions(suggestions)
                .build();
    }

}
