package capstone.mju.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class RecentRecipes {
    @Schema(description = "id", example = "4of0738k-16e3-4cd8-b7e8-fg6c6a560ea0")
    private UUID id;
    @Schema(description = "제목", example = "스파니쉬 오믈렛")
    private String title;
    @Schema(description = "필요한 재료 목록", example = "[\"양파\", \"토마토\", \"감자\", \"치킨 브로스\", \"버터\", \"소금\", \"후추\"]")
    private List<String> ingredients;

    public static RecentRecipes from(UUID recipeId, String title, List<String> ingredients) {
        return new RecentRecipes(recipeId, title, ingredients);
    }
}
