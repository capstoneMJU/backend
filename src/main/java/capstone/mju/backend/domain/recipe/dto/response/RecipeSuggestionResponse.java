package capstone.mju.backend.domain.recipe.dto.response;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecipeSuggestionResponse {
    private String title;
    private List<String> ingredients;

    public RecipeSuggestionResponse() {}

    public void setIngredients(String ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            this.ingredients = Arrays.asList(ingredients.split(","));
        } else {
            this.ingredients = Collections.emptyList();
        }
    }
}