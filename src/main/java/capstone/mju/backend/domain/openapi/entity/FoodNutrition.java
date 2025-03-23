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

    private String foodNmKr; //식품명
    private String foodCd;// 식품코드
    private String itemReportNo; //품목제조보고번호

    private Double amtNum1; // 에너지
    private Double amtNum3; // 단백질
    private Double amtNum4; // 지방
    private Double amtNum7; // 탄수화물
    private Double amtNum8; // 당류
    private Double amtNum14; // 나트륨
    private Double amtNum24; // 콜레스테롤
    private Double amtNum25; // 포화지방산
    private Double amtNum26; // 트랜스지방산

    private Double amtNum52; // 갈락토오스
    private Double amtNum53; // 과당
    private Double amtNum54; // 당알콜
    private Double amtNum55; // 맥아당
    private Double amtNum56; // 알룰로오스
    private Double amtNum57; // 에리스리톨
    private Double amtNum58; // 유당
    private Double amtNum59; // 자당
    private Double amtNum61; // 포도당

    private String foodOrNm; //식품 기원명
    private String foodCat1Cd; //식품 대분류 코드
    private String foodCat1Nm; //식품 대분류 명
    private String foodRefCd; //대표 식품 코드
    private String foodRefNm; //대표 식품 명
}
