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
    @Schema(description = "Id", example = "1")
    private String id;

    @Schema(description = "식품명", example = "김치")
    private String foodNmKr;

    @Schema(description = "식품회사명", example = "롯데")
    private String makerNm; //식품 회사 명

    public static FoodNameResponseDto fromEntity(FoodNutrition entity) {
        return FoodNameResponseDto.builder()
                .id(entity.getId().toString())
                .foodNmKr(entity.getFoodNmKr())
                .makerNm(entity.getMakerNm())
                .build();
    }
}
