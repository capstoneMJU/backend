package capstone.mju.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeResult {
    private String title;
    private String steps;
}
