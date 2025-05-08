package capstone.mju.backend.domain.sugarsubstitute.entity.repository;


import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.sugarsubstitute.entity.FoodNutritionSweetener;
import capstone.mju.backend.domain.sugarsubstitute.entity.SugarSubstitute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodNutritionSweetenerRepository extends JpaRepository<FoodNutritionSweetener, Long> {

    boolean existsByFoodNutritionAndSugarSubstitute(FoodNutrition food, SugarSubstitute sub);

    List<FoodNutritionSweetener> findByFoodNutrition(FoodNutrition food);
}
