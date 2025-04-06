package capstone.mju.backend.domain.nutrition.dto.res;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "식품 응답 DTO")
public class FoodResponseDto {

    @Schema(description = "식품명", example = "김치")
    private String foodNmKr;          // 식품명

    @Schema(description = "식품코드", example = "123456")
    private String foodCd;            // 식품코드

    @Schema(description = "품목제조번호", example = "123456789")
    private String itemReportNo;      // 품목제조번호

    @Schema(description = "에너지 (kcal)", example = "100.5")
    private Double energy;            // 에너지 (amtNum1)

    @Schema(description = "단백질 (g)", example = "5.0")
    private Double protein;           // 단백질 (amtNum3)

    @Schema(description = "지방 (g)", example = "3.5")
    private Double fat;               // 지방 (amtNum4)

    @Schema(description = "탄수화물 (g)", example = "15.0")
    private Double carbohydrate;      // 탄수화물 (amtNum6)

    @Schema(description = "당류 (g)", example = "5.0")
    private Double sugars;            // 당류 (amtNum7)

    @Schema(description = "나트륨 (mg)", example = "200.0")
    private Double sodium;            // 나트륨 (amtNum13)

    @Schema(description = "콜레스테롤 (mg)", example = "10.0")
    private Double cholesterol;       // 콜레스테롤 (amtNum23)

    @Schema(description = "포화지방산 (g)", example = "1.0")
    private Double saturatedFat;      // 포화지방산 (amtNum24)

    @Schema(description = "트랜스지방산 (g)", example = "0.5")
    private Double transFat;          // 트랜스지방산 (amtNum25)

    @Schema(description = "갈락토오스 (g)", example = "0.1")
    private Double galactose;         // 갈락토오스 (amtNum51)

    @Schema(description = "과당 (g)", example = "0.2")
    private Double fructose;          // 과당 (amtNum52)

    @Schema(description = "당알콜 (g)", example = "0.3")
    private Double sugarAlcohol;      // 당알콜 (amtNum53)

    @Schema(description = "맥아당 (g)", example = "0.4")
    private Double maltose;           // 맥아당 (amtNum54)

    @Schema(description = "알룰로오스 (g)", example = "0.5")
    private Double allulose;          // 알룰로오스 (amtNum55)

    @Schema(description = "에리스리톨 (g)", example = "0.6")
    private Double erythritol;        // 에리스리톨 (amtNum56)

    @Schema(description = "유당 (g)", example = "0.7")
    private Double lactose;           // 유당 (amtNum57)

    @Schema(description = "자당 (g)", example = "0.8")
    private Double sucrose;           // 자당 (amtNum58)

    @Schema(description = "포도당 (g)", example = "0.9")
    private Double glucose;           // 포도당 (amtNum60)

    @Schema(description = "식품 기원명", example = "채소")
    private String foodOrNm;          // 식품 기원명

    @Schema(description = "식품 대분류 명", example = "기타")
    private String foodCat1Nm;        // 식품 대분류 명

    @Schema(description = "대표 식품 명", example = "간편식")
    private String foodRefNm;         // 대표 식품 명

    @Schema(description = "1회 섭취참고량", example = "200.0")
    private String nutriAmountServing; // 1회 섭취참고량

    @Schema(description = "식품 중량 (g)", example = "500.0")
    private String totalWeight;       // 식품 중량 (z10500)

    @Schema(description = "1회 섭취량 (g)", example = "100.0")
    private String servingSize;       // 1회 섭취량

    public static FoodResponseDto fromEntity(FoodNutrition entity) {
        return FoodResponseDto.builder()
                .foodNmKr(entity.getFoodNmKr())
                .foodCd(entity.getFoodCd())
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

                .foodOrNm(entity.getFoodOrNm())
                .foodCat1Nm(entity.getFoodCat1Nm())
                .foodRefNm(entity.getFoodRefNm())
                .nutriAmountServing(entity.getNutri_amount_serving())
                .totalWeight(entity.getZ10500())
                .servingSize(entity.getSERVING_SIZE())

                .build();
    }
}
