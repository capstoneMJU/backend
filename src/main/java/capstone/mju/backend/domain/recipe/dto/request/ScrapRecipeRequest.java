package capstone.mju.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "레시피 생성 요청 DTO")
public class ScrapRecipeRequest {
    @NotBlank
    @Schema(description = "스크랩할 레시피 제목", example = "\"title\": \"스파니쉬 오믈렛\"")
    private String title;

    @NotEmpty
    @Schema(description = "필요한 재료 리스트", example = "\"ingredients\": [\n" +
            "        {\n" +
            "            \"name\": \"감자\"\n" +
            "        },")
    private List<IngredientRequest> ingredients;

    @NotBlank
    @Schema(description = "레시피 단계 및 설명", example = "\"steps\": \"1. 감자와 토마토를 깨끗이 씻은 후, 감자는 작은 큐브 모양으로, 토마토는 웨지 모양으로 잘라줍니다.\\n2. 팬에 약간의 올리브 오일을 두르고, 감자")
    private String steps;

    @Getter
    @Schema(description = "재료 정보 DTO")
    public static class IngredientRequest {

        @NotBlank
        @Schema(description = "재료 이름", example = "감자")
        private String name;
    }
}
