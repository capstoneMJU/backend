package capstone.mju.backend.domain.sugarsubstitute.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "감미료 상세 정보 DTO")
public class SugarSubstituteRes {
    @Schema(description = "감미료 이름", example = "스테비아")
    private String name;

    @Schema(description = "감미료 분류", example = "천연 감미료")
    private String category;

    @Schema(description = "감미료 설명", example = "스테비아는 남아메리카 원산의 식물로부터 추출된 천연 감미료입니다.")
    private String description;

    @Schema(description = "부작용 정보", example = "과다 섭취 시 복부 팽만감이 발생할 수 있습니다.")
    private String sideEffect;

    @Schema(description = "GI 지수", example = "0")
    private String giIndex;

    @Schema(description = "칼로리", example = "0 kcal")
    private String calorie;
}

