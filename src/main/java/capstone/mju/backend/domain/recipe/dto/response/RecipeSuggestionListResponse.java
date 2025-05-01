package capstone.mju.backend.domain.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecipeSuggestionListResponse {
    private List<RecipeSuggestionResponse> suggestions;
}

