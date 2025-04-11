package capstone.mju.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Schema(description = "스크랩된 레시피 응답")
public class ScrapRecipeResponse {
    @Schema(description = "레시피 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID recipeId;

    @Schema(description = "레시피 제목", example = "저칼로리 스테이크 샐러드")
    private String title;

    @Schema(description = "레시피에 사용된 재료 목록")
    private List<IngredientSimpleResponse> ingredients;

    @Schema(description = "레시피 조리 순서", example = "1. 고기를 굽는다. 2. 샐러드를 준비한다. 3. 고기와 샐러드를 접시에 담는다.")
    private String steps;

    @Getter
    @Builder
    @Schema(description = "간단한 재료 정보")
    public static class IngredientSimpleResponse {
        @Schema(description = "재료 이름", example = "토마토")
        private String name;
    }
}
