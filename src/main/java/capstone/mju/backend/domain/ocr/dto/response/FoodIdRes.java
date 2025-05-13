package capstone.mju.backend.domain.ocr.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodIdRes {
    private Long foodId;
    private String foodNmKr;

    @Builder FoodIdRes(Long foodId, String foodNmKr){
        this.foodId = foodId;
        this. foodNmKr = foodNmKr;
    }
}
