package capstone.mju.backend.domain.recipe.dto.response;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Schema(description = "레시피 상세 응답")
public class RecipeDetailResponse {
    @Schema(description = "레시피 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "제목", example = "스파니쉬 오믈렛")
    private String title;

    @Schema(description = "필요한 재료", example = "닭가슴살,  토마토,  간장, 설탕,  참기름,  마늘")
    private List<Ingredient> ingredients;

    @Schema(description = "레시피 조리 방법", example = "1. 감자와 토마토를 썰어주세요.\n2. 냄비에 넣고 끓이세요.")
    private String steps;

    public static RecipeDetailResponse of(UUID id, String title, List<Ingredient> ingredients, String steps) {
        return new RecipeDetailResponse(id, title, ingredients, steps);
    }
}
