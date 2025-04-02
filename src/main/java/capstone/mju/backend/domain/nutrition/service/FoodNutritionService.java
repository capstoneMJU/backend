package capstone.mju.backend.domain.nutrition.service;

import capstone.mju.backend.domain.nutrition.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.nutrition.dto.res.FoodNamedetailResponse;
import capstone.mju.backend.domain.nutrition.dto.res.FoodResponseDto;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodNutritionService {

    private final FoodNutritionRepository foodRepository;

    //상품명 검색 -> 상품명 나옴
    public List<FoodNameResponseDto> searchFoodNames(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(keyword, pageable);
        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    //상품명 -> 영양성분
    public List<FoodNamedetailResponse> getFoodDetailsByFoodNameKr(String foodNmKr, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(foodNmKr,pageable);
        return foods.map(FoodNamedetailResponse::fromEntity).getContent();
    }

    //품목제조보고번호 검색 -> 상품명
    public List<FoodNameResponseDto> searchByItemReportNo(String itemReportNo, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo, pageable);
        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    //품목제조보고번호 검색 -> 영양성분
    public List<FoodResponseDto> getFoodDetailsByItemReportNo(String itemReportNo, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo, pageable);
        return foods.map(FoodResponseDto::fromEntity).getContent();
    }
}