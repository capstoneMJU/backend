package capstone.mju.backend.domain.nutrition.dto.res;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "식품명 응답 DTO")
public class FoodNameResponseDto {

    @Schema(description = "식품명", example = "김치")
    private String foodNmKr;

    public static FoodNameResponseDto fromEntity(FoodNutrition entity) {
        return FoodNameResponseDto.builder()
                .foodNmKr(entity.getFoodNmKr())
                .build();
    }
}
