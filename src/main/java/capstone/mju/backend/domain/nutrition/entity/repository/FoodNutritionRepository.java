package capstone.mju.backend.domain.nutrition.entity.repository;

import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition, Long> {
    Page<FoodNutrition> findByFoodNmKrContaining(String foodNmKr, Pageable pageable); //상품명 입력
    boolean existsByItemReportNo(String itemReportNo); // 품목제조보고번호로 중복 체크
    Page<FoodNutrition> findByItemReportNoContaining(String itemReportNo, Pageable pageable);


}
