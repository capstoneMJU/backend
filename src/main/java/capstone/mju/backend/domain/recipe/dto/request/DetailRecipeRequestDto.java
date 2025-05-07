package capstone.mju.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DetailRecipeRequestDto {
    @Schema(description = "제목", example = "스파니쉬 오믈렛")
    String title;
    @Schema(description = "필요한 재료 목록", example = "감자, 토마토, 후추, 소금")
    String ingredients;
}
