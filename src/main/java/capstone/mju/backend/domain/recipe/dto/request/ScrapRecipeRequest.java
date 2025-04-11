package capstone.mju.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "레시피 생성 요청 DTO")
public class ScrapRecipeRequest {
    @NotBlank
    @Schema(description = "스크랩할 레시피 제목", example = "맛있는 안창살 스테이크")
    private String title;

    @NotEmpty
    @Schema(description = "필요한 재료 리스트")
    private List<IngredientRequest> ingredients;

    @NotBlank
    @Schema(description = "레시피 단계 및 설명", example = "1. 고기를 굽는다\n2. 감자와 토마토를 굽는다")
    private String steps;

    @Getter
    @Schema(description = "재료 정보 DTO")
    public static class IngredientRequest {

        @NotBlank
        @Schema(description = "재료 이름", example = "감자")
        private String name;
    }
}
