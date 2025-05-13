package capstone.mju.backend.domain.ocr.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "식품ID 조회 응답 DTO")
public class FoodIdRes {
    @Schema(description = "식품고유번호", example = "17852")
    private Long foodId;

    @Schema(description = "식품회사명", example = "롯데")
    private String foodNmKr;

    @Builder FoodIdRes(Long foodId, String foodNmKr){
        this.foodId = foodId;
        this. foodNmKr = foodNmKr;
    }
}
