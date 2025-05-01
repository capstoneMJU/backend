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

//    /*
//     레시피 생성
//     */
//    public RecipeDetailResponse createLowCalorieRecipe(RecipeDto request) {
//        String ingredientsText = request.getIngredients().toString();
//
//        // OpenAI 호출
//        RecipeResult recipeResult = openAiService.createRecipePromptAndTitle(ingredientsText);
//
//        // DB 저장용 Ingredient 파싱 및 저장
//        List<Ingredient> ingredients = List.of(ingredientsText.split(",")).stream()
//                .map(String::trim)
//                .map(name -> Ingredient.builder().name(name).build())
//                .map(ingredientRepository::save)
//                .collect(Collectors.toList());
//
//        // Recipe 저장
//        Recipe recipe = Recipe.builder()
//                .title(recipeResult.getTitle())
//                .requiredIngredients(ingredients)
//                .recipeContent(recipeResult.getSteps())
//                .build();
//        recipeRepository.save(recipe);
//
//        return RecipeDetailResponse.of(
//                recipe.getId(),
//                recipe.getTitle(),
//                ingredients,
//                recipe.getRecipeContent()
//        );
//    }

    /*
    추천 레시피 목록 응답
     */
    public RecipeSuggestionListResponse suggestRecipes(RecipeSuggestionRequest request) {
        List<RecipeSuggestionResponse> suggestions = openAiService.generateRecipeSuggestions(request.getIngredients());
        return RecipeSuggestionListResponse.builder()
                .suggestions(suggestions)
                .build();
    }

    /*
    추천된 레시피 제목으로 상세 내용 가져오기
     */
    public RecipeDetailResponse getRecipeDetail(String title, String ingredients) {
        RecipeDetailResponse recipeDetailResponse = openAiService.createRecipePromptAndTitle(title, ingredients);
        return recipeDetailResponse;
    }

    /*
   레시피 스크랩
    */
    @Transactional
    public ScrapRecipeResponse scrapRecipe(User user, ScrapRecipeRequest request) {
        // ingredients를 List<IngredientRequest>로 처리
        List<Ingredient> ingredients = request.getIngredients().stream()
                .map(ingredientRequest -> Ingredient.builder()
                        .name(ingredientRequest.getName())
                        .build())
                .map(ingredientRepository::save)
                .collect(Collectors.toList());

        // Recipe 객체 생성
        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .requiredIngredients(ingredients)
                .recipeContent(request.getSteps())
                .build();

        // 레시피 저장
        recipeRepository.save(recipe);

        // 반환
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
    레시피 단건 조회
     */
    public RecipeDetailResponse getRecipeById(UUID recipeId) {
        Recipe recipe = findRecipeByIdOrThrow(recipeId);
        List<Ingredient> ingredients = recipe.getRequiredIngredients();

        return RecipeDetailResponse.of(
                recipe.getId(),
                recipe.getTitle(),
                ingredients,
                recipe.getRecipeContent()
        );
    }

    /*
    저장된 모든 레시피 조회
     */
    public RecipeListResponse getAllRecipes(User user) {
        List<Recipe> recipes = recipeRepository.findAll();

        List<RecipeDetailResponse> recipeResponses = recipes.stream()
                .map(recipe -> {
                    String ingredients = recipe.getRequiredIngredients().stream()
                            .map(Ingredient::getName)
                            .collect(Collectors.joining(", "));
                    return RecipeDetailResponse.of(
                            recipe.getId(),
                            recipe.getTitle(),
                            recipe.getRequiredIngredients(),
                            recipe.getRecipeContent()
                    );
                })
                .collect(Collectors.toList());

        return RecipeListResponse.from(recipeResponses);
    }

    /*
    레시피 삭제
     */
    public void deleteRecipe(UUID recipeId) {
        Recipe recipe = findRecipeByIdOrThrow(recipeId);
        recipeRepository.delete(recipe);
    }

    private Recipe findRecipeByIdOrThrow(UUID recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
    }
}
