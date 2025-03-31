package capstone.mju.backend.domain.openapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_nutrition")
@Getter
@Setter
@NoArgsConstructor
public class FoodNutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_name_kr")
    private String foodNmKr; // 식품명

    @Column(name = "food_code")
    private String foodCd; // 식품코드

    @Column(name = "item_report_no")
    private String itemReportNo; // 품목제조보고번호

    @Column(name = "energy")
    private Double amtNum1; // 에너지

    @Column(name = "protein")
    private Double amtNum3; // 단백질

    @Column(name = "fat")
    private Double amtNum4; // 지방

    @Column(name = "carbohydrate")
    private Double amtNum6; // 탄수화물

    @Column(name = "sugars")
    private Double amtNum7; // 당류

    @Column(name = "sodium")
    private Double amtNum13; // 나트륨

    @Column(name = "cholesterol")
    private Double amtNum23; // 콜레스테롤

    @Column(name = "saturated_fat")
    private Double amtNum24; // 포화지방산

    @Column(name = "trans_fat")
    private Double amtNum25; // 트랜스지방산

    @Column(name = "galactose")
    private Double amtNum51; // 갈락토오스

    @Column(name = "fructose")
    private Double amtNum52; // 과당

    @Column(name = "sugar_alcohol")
    private Double amtNum53; // 당알콜

    @Column(name = "maltose")
    private Double amtNum54; // 맥아당

    @Column(name = "allulose")
    private Double amtNum55; // 알룰로오스

    @Column(name = "erythritol")
    private Double amtNum56; // 에리스리톨

    @Column(name = "lactose")
    private Double amtNum57; // 유당

    @Column(name = "sucrose")
    private Double amtNum58; // 자당

    @Column(name = "glucose")
    private Double amtNum60; // 포도당

    @Column(name = "food_origin_name")
    private String foodOrNm; // 식품 기원명

    @Column(name = "food_category_name")
    private String foodCat1Nm; // 식품 대분류 명

    @Column(name = "food_ref_name")
    private String foodRefNm; // 대표 식품 명

    @Column(name = "nutri_amount_serving")
    private String nutri_amount_serving; // 1회 섭취참고량

    @Column(name = "food_weight")
    private String z10500; // 식품 중량

    @Column(name = "serving_size")
    private String SERVING_SIZE; //ex) 100g 당 ~ 제로는 355g~ 이런것
}
