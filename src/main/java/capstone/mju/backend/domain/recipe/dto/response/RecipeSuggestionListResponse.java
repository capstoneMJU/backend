package capstone.mju.backend.domain.recipe.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class RecipeSuggestionListResponse {
    private List<RecipeSuggestionResponse> suggestions;
}