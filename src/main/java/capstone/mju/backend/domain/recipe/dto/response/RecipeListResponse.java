package capstone.mju.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Schema(description = "재료 이름", example = "토마토")
public class RecipeListResponse {
    private List<RecipeDetailResponse> recipes;
    private int totalPages;
    private long totalElements;

    public static RecipeListResponse from(List<RecipeDetailResponse> recipeResponses, int totalPages, long totalElements) {
        return new RecipeListResponse(recipeResponses, totalPages, totalElements);
    }
}
