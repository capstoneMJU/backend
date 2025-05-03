package capstone.mju.backend.domain.recipe.controller;

import capstone.mju.backend.domain.common.ResponseDto;
import capstone.mju.backend.domain.recipe.dto.request.DetailRecipeRequestDto;
import capstone.mju.backend.domain.recipe.dto.request.ScrapRecipeRequest;
import capstone.mju.backend.domain.recipe.dto.request.RecipeSuggestionRequest;
import capstone.mju.backend.domain.recipe.dto.response.*;
import capstone.mju.backend.domain.recipe.service.RecipeService;
import capstone.mju.backend.domain.user.domain.User;
import capstone.mju.backend.global.auth.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Tag(name = "Recipe", description = "레시피 관련 API")
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/suggest")
    @Operation(
            summary = "재료 기반 레시피 추천",
            description = "입력된 재료로 만들 수 있는 저칼로리 레시피들을 추천합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "레시피 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RecipeSuggestionListResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청")})
    public ResponseEntity<ResponseDto<RecipeSuggestionListResponse>> suggestRecipes(
            @Parameter(hidden = true) @AuthenticatedUser User user, @RequestBody RecipeSuggestionRequest request
    ) {
        RecipeSuggestionListResponse response = recipeService.suggestRecipes(request);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 추천 성공", response),
                HttpStatus.OK
        );
    }

    @PostMapping("/createRecipe")
    @Operation(summary = "저칼로리 레시피 생성", description = "입력된 재료와 제목을 바탕으로 저칼로리 레시피 목록을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "레시피 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDetailResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력"
            )})
    public ResponseEntity<ResponseDto<RecipeDetailResponse>> createRecipePromptAndTitle(
            @Parameter(hidden = true) @AuthenticatedUser User user, @Parameter(
                    description = "레시피의 제목",
                    example = "양파 토마토 스프"
            ) @RequestBody DetailRecipeRequestDto requestDto
    ) {
        RecipeDetailResponse response = recipeService.getRecipeDetail(requestDto.getTitle(), requestDto.getIngredients());
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 생성 성공", response),
                HttpStatus.OK
        );
    }

    @PostMapping("/scrap")
    @Operation(summary = "레시피 스크랩", description = "외부 레시피를 스크랩하여 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "레시피 스크랩 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScrapRecipeResponse.class)
                    ))})
    public ResponseEntity<ResponseDto<ScrapRecipeResponse>> scrapRecipe(
            @Parameter(hidden = true) @AuthenticatedUser User user,
            @Validated @RequestBody ScrapRecipeRequest request
    ) {
        ScrapRecipeResponse scrapRecipeResponse = recipeService.scrapRecipe(user, request);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "스크랩 성공", scrapRecipeResponse),
                HttpStatus.OK
        );
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "단건 레시피 조회", description = "ID로 특정 레시피를 조회합니다.")
    public ResponseEntity<ResponseDto<RecipeDetailResponse>> getRecipe(
            @Parameter(hidden = true) @AuthenticatedUser User user,
            @PathVariable UUID recipeId
    ) {
        RecipeDetailResponse recipeResponse = recipeService.getRecipeById(recipeId, user);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 조회 성공", recipeResponse),
                HttpStatus.OK
        );
    }

    @GetMapping
    @Operation(summary = "모든 레시피 조회", description = "저장된 모든 레시피 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<RecipeListResponse>> getAllRecipes(
            @Parameter(hidden = true) @AuthenticatedUser User user
    ) {
        RecipeListResponse recipes = recipeService.getAllRecipes(user);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "모든 레시피 조회 성공", recipes),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "레시피 삭제", description = "ID로 특정 레시피를 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteRecipe(
            @Parameter(hidden = true) @AuthenticatedUser User user,
            @PathVariable UUID recipeId
    ) {
        recipeService.deleteRecipe(user, recipeId);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "레시피 삭제 성공"),
                HttpStatus.OK
        );
    }
}
