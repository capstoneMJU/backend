package capstone.mju.backend.domain.openapi.entity.repository;

import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodNutritionRepository extends JpaRepository<FoodNutrition, Long> {
    Optional<FoodNutrition> findByFoodCd(String foodCd); //푸드
    Page<FoodNutrition> findByFoodNmKrContaining(String keyword, Pageable pageable); //상품명 입력
    Page<FoodNutrition> findByItemReportNoContaining(String itemReportNo, Pageable pageable); //품목제조보고번호 입력
    boolean existsByItemReportNo(String itemReportNo); // 품목제조보고번호로 중복 체크
}
