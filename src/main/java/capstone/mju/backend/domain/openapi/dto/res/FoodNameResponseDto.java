package capstone.mju.backend.domain.openapi.dto.res;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodNameResponseDto {
    private String foodNmKr;

    public static FoodNameResponseDto fromEntity(FoodNutrition entity) {
        return FoodNameResponseDto.builder()
                .foodNmKr(entity.getFoodNmKr())
                .build();
    }
}
