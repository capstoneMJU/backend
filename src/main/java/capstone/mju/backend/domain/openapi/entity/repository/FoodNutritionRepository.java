package capstone.mju.backend.domain.openapi.entity.repository;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition, Long> {
    Optional<FoodNutrition> findByFoodCd(String foodCd); //푸드
}
