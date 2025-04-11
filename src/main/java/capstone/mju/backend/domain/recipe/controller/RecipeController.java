package capstone.mju.backend.domain.recipe.controller;

import capstone.mju.backend.domain.common.ResponseDto;
import capstone.mju.backend.domain.recipe.dto.request.RecipeDto;
import capstone.mju.backend.domain.recipe.dto.request.ScrapRecipeRequest;
import capstone.mju.backend.domain.recipe.dto.response.RecipeListResponse;
import capstone.mju.backend.domain.recipe.dto.response.RecipeResponse;
import capstone.mju.backend.domain.recipe.dto.response.ScrapRecipeResponse;
import capstone.mju.backend.domain.recipe.service.RecipeService;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Recipe", description = "레시피 관련 API")
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    @Operation(summary = "레시피 생성", description = "새로운 저칼로리 레시피를 생성합니다.")
    public ResponseEntity<ResponseDto<RecipeResponse>> createRecipe(@Parameter(hidden = true)@AuthenticatedUser User user, @RequestBody RecipeDto request) {
        RecipeResponse recipeResponse = recipeService.createLowCalorieRecipe(request);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 생성 성공", recipeResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "레시피 조회", description = "ID로 특정 레시피를 조회합니다.")
    public ResponseEntity<ResponseDto<RecipeResponse>> getRecipe(@Parameter(hidden = true) @AuthenticatedUser User user, @PathVariable UUID recipeId) {
        RecipeResponse recipeResponse = recipeService.getRecipeById(recipeId);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 조회 성공", recipeResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    @Operation(summary = "모든 레시피 조회", description = "저장된 모든 레시피 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<RecipeListResponse>> getAllRecipes(@Parameter(hidden = true) @AuthenticatedUser User user) {
        RecipeListResponse recipes = recipeService.getAllRecipes(user);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "모든 레시피 조회 성공", recipes),
                HttpStatus.OK
        );
    }

    @PostMapping("/scrap")
    @Operation(summary = "레시피 스크랩", description = "외부 레시피를 스크랩하여 저장합니다.")
    public ResponseEntity<ResponseDto<ScrapRecipeResponse>> scrapRecipe(@Parameter(hidden = true) @AuthenticatedUser User user,
            @Validated @RequestBody ScrapRecipeRequest request
    ) {
        ScrapRecipeResponse scrapRecipeResponse = recipeService.scrapRecipe(user, request);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "스크랩 성공", scrapRecipeResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "레시피 삭제", description = "ID로 특정 레시피를 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteRecipe(@Parameter(hidden = true) @AuthenticatedUser User user, @Parameter(description = "삭제할 레시피 ID") @PathVariable UUID recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 삭제 성공"),
                HttpStatus.OK
        );
    }
}
