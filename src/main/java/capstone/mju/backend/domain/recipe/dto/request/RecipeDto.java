package capstone.mju.backend.domain.recipe.dto.request;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "레시피 생성 요청 DTO")
public class RecipeDto {
    @Schema(description = "레시피 생성 요청 DTO")
    private List<Ingredient> ingredients;

    public static RecipeDto of(List<Ingredient> ingredients) {
        return new RecipeDto(ingredients);
    }
}
