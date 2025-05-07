package capstone.mju.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeSuggestionRequest {
    @Schema(description = "후보 레시피 생성 요청 DTO", example = "토마토, 계란, 피망")
    private String ingredients;
}