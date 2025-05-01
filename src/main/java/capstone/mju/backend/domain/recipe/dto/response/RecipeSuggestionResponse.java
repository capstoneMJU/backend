package capstone.mju.backend.domain.recipe.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class RecipeSuggestionResponse {
    private String title;
    private List<String> ingredients; // 5~10개의 재료
}