package capstone.mju.backend.domain.sugarsubstitute.domain;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class FoodNutritionSweetener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_nutrition_id")
    private FoodNutrition foodNutrition; // 영양정보

    @ManyToOne
    @JoinColumn(name = "sweetener_id")
    private SugarSubstitute sugarSubstitute; // 대체당

    public FoodNutritionSweetener(FoodNutrition foodNutrition, SugarSubstitute sugarSubstitute) {
        this.foodNutrition = foodNutrition;
        this.sugarSubstitute = sugarSubstitute;
    }
}
