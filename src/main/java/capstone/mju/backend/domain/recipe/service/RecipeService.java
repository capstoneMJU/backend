package capstone.mju.backend.domain.recipe.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.ForbiddenException;
import capstone.mju.backend.domain.common.exception.NotFoundException;
import capstone.mju.backend.domain.common.exception.UnauthorizedException;
import capstone.mju.backend.domain.recipe.domain.Ingredient;
import capstone.mju.backend.domain.recipe.domain.ScrapRecipe;
import capstone.mju.backend.domain.recipe.dto.request.RecipeSuggestionRequest;
import capstone.mju.backend.domain.recipe.dto.request.ScrapRecipeRequest;
import capstone.mju.backend.domain.recipe.dto.response.*;
import capstone.mju.backend.domain.recipe.repository.IngredientRepository;
import capstone.mju.backend.domain.recipe.domain.Recipe;
import capstone.mju.backend.domain.recipe.repository.RecipeRepository;
import capstone.mju.backend.domain.recipe.repository.ScrapRecipeRepository;
import capstone.mju.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ScrapRecipeRepository scrapRecipeRepository;

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
        Recipe existingRecipe = recipeRepository.findByTitle(request.getTitle());

        if (existingRecipe == null) {
            List<Ingredient> ingredients = request.getIngredients().stream()
                    .map(ingredientRequest -> {
                        Ingredient existingIngredient = ingredientRepository.findByName(ingredientRequest.getName());

                        if (existingIngredient != null) {
                            return existingIngredient;
                        } else {
                            Ingredient newIngredient = Ingredient.builder()
                                    .name(ingredientRequest.getName())
                                    .build();
                            return ingredientRepository.save(newIngredient);
                        }
                    })
                    .collect(Collectors.toList());

            // 새로운 Recipe 객체 생성
            existingRecipe = Recipe.builder()
                    .title(request.getTitle())
                    .requiredIngredients(ingredients)
                    .recipeContent(request.getSteps())
                    .build();

            // 레시피 저장
            recipeRepository.save(existingRecipe);
        }
        if (scrapRecipeRepository.existsByUserAndRecipe(user, existingRecipe)) {
            throw new ForbiddenException(ErrorCode.ALREADY_SCRAPPED, "이미 저장된 레시피입니다.");
        }

        ScrapRecipe scrapRecipe = ScrapRecipe.builder()
                .user(user)
                .recipe(existingRecipe)
                .build();

        scrapRecipeRepository.save(scrapRecipe);

        return ScrapRecipeResponse.builder()
                .recipeId(existingRecipe.getId())
                .title(existingRecipe.getTitle())
                .ingredients(existingRecipe.getRequiredIngredients().stream()
                        .map(ingredient -> ScrapRecipeResponse.IngredientSimpleResponse.builder()
                                .name(ingredient.getName())
                                .build())
                        .collect(Collectors.toList()))
                .steps(existingRecipe.getRecipeContent())
                .build();
    }


    /*
    스크랩 한 레시피 단건 조회
     */
    public RecipeDetailResponse getRecipeById(UUID recipeId, User user) {
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
    public RecipeListResponse getAllRecipes(User user, Pageable pageable) {
        Page<ScrapRecipe> scrapRecipes = scrapRecipeRepository.findByUser(user, pageable);

        List<RecipeDetailResponse> recipeResponses = scrapRecipes.stream()
                .map(scrapRecipe -> {
                    Recipe recipe = scrapRecipe.getRecipe();
                    validateUser(user, recipe);
                    return RecipeDetailResponse.of(
                            recipe.getId(),
                            recipe.getTitle(),
                            recipe.getRequiredIngredients(),
                            recipe.getRecipeContent()
                    );
                })
                .collect(Collectors.toList());

        return RecipeListResponse.from(recipeResponses, scrapRecipes.getTotalPages(), scrapRecipes.getTotalElements());
    }


    /*
    레시피 삭제
     */
    @Transactional
    public void deleteRecipe(User user, UUID recipeId) {
        Recipe recipe = findRecipeByIdOrThrow(recipeId);
        validateUser(user, recipe);
        scrapRecipeRepository.deleteByUserAndRecipe(user, recipe);
        recipeRepository.delete(recipe);
    }

    /*
    Recipe DB 조회
     */
    private Recipe findRecipeByIdOrThrow(UUID recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECIPE_NOT_FOUND, "레시피를 찾을 수 없습니다."));
    }

    /*
    User, Recipe 권한 검증
     */
    private boolean validateUser(User user, Recipe recipe) {
        boolean isScrapByUser = scrapRecipeRepository.existsByUserAndRecipe(user, recipe);

        if (!isScrapByUser) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_USER, "이 레시피는 유저가 스크랩한 레시피가 아닙니다.");
        }
        return isScrapByUser;
    }
}