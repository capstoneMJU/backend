package capstone.mju.backend.domain.openapi.dto.res;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodNamedetailResponse {

    private String foodNmKr;         // 식품명
    private String itemReportNo;     // 품목제조번호

    private Double energy;           // 에너지 (amtNum1)
    private Double protein;          // 단백질 (amtNum3)
    private Double fat;              // 지방 (amtNum4)
    private Double carbohydrate;     // 탄수화물 (amtNum6)
    private Double sugars;           // 당류 (amtNum7)
    private Double sodium;           // 나트륨 (amtNum13)
    private Double cholesterol;      // 콜레스테롤 (amtNum23)
    private Double saturatedFat;     // 포화지방산 (amtNum24)
    private Double transFat;         // 트랜스지방산 (amtNum25)
    private Double galactose;        // 갈락토오스 (amtNum51)
    private Double fructose;         // 과당 (amtNum52)
    private Double sugarAlcohol;     // 당알콜 (amtNum53)
    private Double maltose;          // 맥아당 (amtNum54)
    private Double allulose;         // 알룰로오스 (amtNum55)
    private Double erythritol;       // 에리스리톨 (amtNum56)
    private Double lactose;          // 유당 (amtNum57)
    private Double sucrose;          // 자당 (amtNum58)
    private Double glucose;          // 포도당 (amtNum60)

    private String totalWeight;      // 식품 총중량 (z10500)
    private String servingSize;      // 1회 섭취량 (SERVING_SIZE)

    // 계산된 값 (총량 기준)
    private Double scaledSugars;
    private Double scaledSaturatedFat;
    private Double scaledSodium;
    private Double scaledEnergy;

    public static FoodNamedetailResponse fromEntity(FoodNutrition entity) {
        double ratio = 1.0;

        try {
            String servingRaw = entity.getSERVING_SIZE();
            String weightRaw = entity.getZ10500();

            if (servingRaw != null && weightRaw != null &&
                    !servingRaw.trim().isEmpty() && !weightRaw.trim().isEmpty()) {

                double serving = Double.parseDouble(servingRaw.trim().replaceAll("[^\\d.]", ""));
                double total = Double.parseDouble(weightRaw.trim().replaceAll("[^\\d.]", ""));

                if (serving > 0) {
                    ratio = total / serving; // 총량 / 섭취량
                }
            }
        } catch (NumberFormatException e) {
            ratio = 1.0;
        }

        return FoodNamedetailResponse.builder()
                .foodNmKr(entity.getFoodNmKr())
                .itemReportNo(entity.getItemReportNo())

                .energy(entity.getAmtNum1())
                .protein(entity.getAmtNum3())
                .fat(entity.getAmtNum4())
                .carbohydrate(entity.getAmtNum6())
                .sugars(entity.getAmtNum7())
                .sodium(entity.getAmtNum13())
                .cholesterol(entity.getAmtNum23())
                .saturatedFat(entity.getAmtNum24())
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

                .scaledSugars(entity.getAmtNum7() != null ? entity.getAmtNum7() * ratio : null)
                .scaledSaturatedFat(entity.getAmtNum24() != null ? entity.getAmtNum24() * ratio : null)
                .scaledSodium(entity.getAmtNum13() != null ? entity.getAmtNum13() * ratio : null)
                .scaledEnergy(entity.getAmtNum1() != null ? entity.getAmtNum1() * ratio : null)

                .build();
    }
}
