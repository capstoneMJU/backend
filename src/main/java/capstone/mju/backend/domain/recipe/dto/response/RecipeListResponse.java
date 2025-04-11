package capstone.mju.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Schema(description = "재료 이름", example = "토마토")
public class RecipeListResponse {
    @Schema(description = "재료 이름", example = "토마토")
    private List<RecipeResponse> recipes;

    public static RecipeListResponse from(List<RecipeResponse> recipes) {
        return new RecipeListResponse(recipes);
    }
}
