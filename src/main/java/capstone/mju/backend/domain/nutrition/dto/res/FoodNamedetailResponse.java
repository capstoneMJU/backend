package capstone.mju.backend.domain.nutrition.dto.res;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "식품 상세 정보 응답 DTO")
public class FoodNamedetailResponse {

    @Schema(description = "식품명", example = "김치")
    private String foodNmKr;

    @Schema(description = "품목제조번호", example = "123456789")
    private String itemReportNo;

    @Schema(description = "에너지 (kcal)", example = "100.5")
    private Double energy;

    @Schema(description = "단백질 (g)", example = "5.0")
    private Double protein;

    @Schema(description = "단백질 기준치 대비 비율 (%)", example = "9.1")
    private Double proteinPercent;

    @Schema(description = "지방 (g)", example = "3.5")
    private Double fat;

    @Schema(description = "지방 기준치 대비 비율 (%)", example = "6.5")
    private Double fatPercent;

    @Schema(description = "탄수화물 (g)", example = "15.0")
    private Double carbohydrate;

    @Schema(description = "탄수화물 기준치 대비 비율 (%)", example = "4.6")
    private Double carbohydratePercent;

    @Schema(description = "당류 (g)", example = "5.0")
    private Double sugars;

    @Schema(description = "당류 기준치 대비 비율 (%)", example = "5.0")
    private Double sugarsPercent;

    @Schema(description = "나트륨 (mg)", example = "200.0")
    private Double sodium;

    @Schema(description = "나트륨 기준치 대비 비율 (%)", example = "10.0")
    private Double sodiumPercent;

    @Schema(description = "콜레스테롤 (mg)", example = "10.0")
    private Double cholesterol;

    @Schema(description = "콜레스테롤 기준치 대비 비율 (%)", example = "3.3")
    private Double cholesterolPercent;

    @Schema(description = "포화지방산 (g)", example = "1.0")
    private Double saturatedFat;

    @Schema(description = "포화지방 기준치 대비 비율 (%)", example = "6.7")
    private Double saturatedFatPercent;

    @Schema(description = "트랜스지방산 (g)", example = "0.5")
    private Double transFat;

    @Schema(description = "갈락토오스 (g)", example = "0.1")
    private Double galactose;

    @Schema(description = "과당 (g)", example = "0.2")
    private Double fructose;

    @Schema(description = "당알콜 (g)", example = "0.3")
    private Double sugarAlcohol;

    @Schema(description = "맥아당 (g)", example = "0.4")
    private Double maltose;

    @Schema(description = "알룰로오스 (g)", example = "0.5")
    private Double allulose;

    @Schema(description = "에리스리톨 (g)", example = "0.6")
    private Double erythritol;

    @Schema(description = "유당 (g)", example = "0.7")
    private Double lactose;

    @Schema(description = "자당 (g)", example = "0.8")
    private Double sucrose;

    @Schema(description = "포도당 (g)", example = "0.9")
    private Double glucose;

    @Schema(description = "식품 총중량 (g)", example = "500.0")
    private String totalWeight;

    @Schema(description = "1회 섭취량 (g)", example = "100.0")
    private String servingSize;

    public static FoodNamedetailResponse fromEntity(FoodNutrition entity) {
        Double protein = entity.getAmtNum3();
        Double fat = entity.getAmtNum4();
        Double carbohydrate = entity.getAmtNum6();
        Double sugars = entity.getAmtNum7();
        Double sodium = entity.getAmtNum13();
        Double cholesterol = entity.getAmtNum23();
        Double saturatedFat = entity.getAmtNum24();

        return FoodNamedetailResponse.builder()
                .foodNmKr(entity.getFoodNmKr())
                .itemReportNo(entity.getItemReportNo())
                .energy(entity.getAmtNum1())
                .protein(protein)
                .proteinPercent(calcPercent(protein, 55.0))
                .fat(fat)
                .fatPercent(calcPercent(fat, 54.0))
                .carbohydrate(carbohydrate)
                .carbohydratePercent(calcPercent(carbohydrate, 324.0))
                .sugars(sugars)
                .sugarsPercent(calcPercent(sugars, 100.0))
                .sodium(sodium)
                .sodiumPercent(calcPercent(sodium, 2000.0))
                .cholesterol(cholesterol)
                .cholesterolPercent(calcPercent(cholesterol, 300.0))
                .saturatedFat(saturatedFat)
                .saturatedFatPercent(calcPercent(saturatedFat, 15.0))
                .transFat(entity.getAmtNum25())
                .galactose(entity.getAmtNum51())
                .fructose(entity.getAmtNum52())
                .sugarAlcohol(entity.getAmtNum53())
                .maltose(entity.getAmtNum54())
                .allulose(entity.getAmtNum55())
                .erythritol(entity.getAmtNum56())
                .lactose(entity.getAmtNum57())
                .sucrose(entity.getAmtNum58())
                .glucose(entity.getAmtNum60())
                .totalWeight(entity.getZ10500())
                .servingSize(entity.getSERVING_SIZE())
                .build();
    }

    private static Double calcPercent(Double value, double standard) {
        if (value == null || standard <= 0) return null;

        double result = (value / standard) * 100;

        return new BigDecimal(result)
                .setScale(1, RoundingMode.DOWN)
                .doubleValue();
    }
}
