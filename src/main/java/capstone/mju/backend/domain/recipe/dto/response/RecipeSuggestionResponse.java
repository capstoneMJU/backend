package capstone.mju.backend.domain.recipe.dto.response;

import capstone.mju.backend.domain.recipe.domain.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "제목", example = "스파니쉬 오믈렛")
    private String title;
    @Schema(description = "필요한 재료 목록", example = "[\"양파\", \"토마토\", \"감자\", \"치킨 브로스\", \"버터\", \"소금\", \"후추\"]")
    private List<String> ingredients;

    public RecipeSuggestionResponse() {
    }
    public void setIngredients(String ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            this.ingredients = Arrays.asList(ingredients.split(","));
        } else {
            this.ingredients = Collections.emptyList();
        }
    }
}