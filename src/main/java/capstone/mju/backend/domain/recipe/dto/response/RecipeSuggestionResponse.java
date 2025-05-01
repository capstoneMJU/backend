package capstone.mju.backend.domain.recipe.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class RecipeSuggestionResponse {
    private String title;
    private String ingredients;
}