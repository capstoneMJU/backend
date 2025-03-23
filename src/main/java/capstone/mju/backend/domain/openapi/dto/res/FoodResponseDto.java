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

    private Double amtNum1;
    private Double amtNum3;
    private Double amtNum4;
    private Double amtNum7;
    private Double amtNum8;
    private Double amtNum14;
    private Double amtNum24;
    private Double amtNum25;
    private Double amtNum26;

    private Double amtNum52;
    private Double amtNum53;
    private Double amtNum54;
    private Double amtNum55;
    private Double amtNum56;
    private Double amtNum57;
    private Double amtNum58;
    private Double amtNum59;
    private Double amtNum61;

    private String foodOrNm;
    private String foodCat1Cd;
    private String foodCat1Nm;
    private String foodRefCd;
    private String foodRefNm;
    private String foodCat2Cd;

    public static FoodResponseDto fromEntity(FoodNutrition entity) {
        return FoodResponseDto.builder()
                .foodNmKr(entity.getFoodNmKr())
                .foodCd(entity.getFoodCd())
                .itemReportNo(entity.getItemReportNo())
                .amtNum1(entity.getAmtNum1())
                .amtNum3(entity.getAmtNum3())
                .amtNum4(entity.getAmtNum4())
                .amtNum7(entity.getAmtNum7())
                .amtNum8(entity.getAmtNum8())
                .amtNum14(entity.getAmtNum14())
                .amtNum24(entity.getAmtNum24())
                .amtNum25(entity.getAmtNum25())
                .amtNum26(entity.getAmtNum26())
                .amtNum52(entity.getAmtNum52())
                .amtNum53(entity.getAmtNum53())
                .amtNum54(entity.getAmtNum54())
                .amtNum55(entity.getAmtNum55())
                .amtNum56(entity.getAmtNum56())
                .amtNum57(entity.getAmtNum57())
                .amtNum58(entity.getAmtNum58())
                .amtNum59(entity.getAmtNum59())
                .amtNum61(entity.getAmtNum61())
                .foodOrNm(entity.getFoodOrNm())
                .foodCat1Cd(entity.getFoodCat1Cd())
                .foodCat1Nm(entity.getFoodCat1Nm())
                .foodRefCd(entity.getFoodRefCd())
                .foodRefNm(entity.getFoodRefNm())
                .build();
    }
}
