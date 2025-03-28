package capstone.mju.backend.domain.openapi.dto.res;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponseDto {

    private String foodNmKr;
    private String foodCd;
    private String itemReportNo;

    private Double amtNum1;  // 에너지
    private Double amtNum3;  // 단백질
    private Double amtNum4;  // 지방
    private Double amtNum6;  // 탄수화물
    private Double amtNum7;  // 당류
    private Double amtNum13; // 나트륨
    private Double amtNum23; // 콜레스테롤
    private Double amtNum24; // 포화지방산
    private Double amtNum25; // 트랜스지방산
    private Double amtNum51; // 갈락토오스
    private Double amtNum52; // 과당
    private Double amtNum53; // 당알콜
    private Double amtNum54; // 맥아당
    private Double amtNum55; // 알룰로오스
    private Double amtNum56; // 에리스리톨
    private Double amtNum57; // 유당
    private Double amtNum58; // 자당
    private Double amtNum60; // 포도당

    private String foodOrNm; // 식품 기원명
    private String foodCat1Nm; // 식품 대분류 명
    private String foodRefNm; // 대표 식품 명
    private String nutri_amount_serving; // 1회 섭취참고량
    private String z10500; // 식품 중량
    private String servingSize; // 1회 섭취량 (추가된 필드)

    public static FoodResponseDto fromEntity(FoodNutrition entity) {
        return FoodResponseDto.builder()
                .foodNmKr(entity.getFoodNmKr())
                .foodCd(entity.getFoodCd())
                .itemReportNo(entity.getItemReportNo())
                .amtNum1(entity.getAmtNum1())
                .amtNum3(entity.getAmtNum3())
                .amtNum4(entity.getAmtNum4())
                .amtNum6(entity.getAmtNum6())
                .amtNum7(entity.getAmtNum7())
                .amtNum13(entity.getAmtNum13())
                .amtNum23(entity.getAmtNum23())
                .amtNum24(entity.getAmtNum24())
                .amtNum25(entity.getAmtNum25())
                .amtNum51(entity.getAmtNum51())
                .amtNum52(entity.getAmtNum52())
                .amtNum53(entity.getAmtNum53())
                .amtNum54(entity.getAmtNum54())
                .amtNum55(entity.getAmtNum55())
                .amtNum56(entity.getAmtNum56())
                .amtNum57(entity.getAmtNum57())
                .amtNum58(entity.getAmtNum58())
                .amtNum60(entity.getAmtNum60())
                .foodOrNm(entity.getFoodOrNm())
                .foodCat1Nm(entity.getFoodCat1Nm())
                .foodRefNm(entity.getFoodRefNm())
                .nutri_amount_serving(entity.getNutri_amount_serving())
                .z10500(entity.getZ10500())
                .servingSize(entity.getSERVING_SIZE())
                .build();
    }
}