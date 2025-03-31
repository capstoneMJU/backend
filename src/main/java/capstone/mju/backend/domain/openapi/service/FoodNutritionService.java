package capstone.mju.backend.domain.openapi.service;

import capstone.mju.backend.domain.openapi.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.openapi.dto.res.FoodResponseDto;
import capstone.mju.backend.domain.openapi.entity.FoodNutrition;
import capstone.mju.backend.domain.openapi.entity.repository.FoodNutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodNutritionService {

    private final FoodNutritionRepository foodRepository;

    //상품명 검색 -> 상품명 나옴
    public List<FoodNameResponseDto> searchFoodNames(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(keyword, pageable);
        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    //상품명 -> 영양성분
    public List<FoodResponseDto> getFoodDetailsByFoodNameKr(String foodNmKr, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(foodNmKr,pageable);
        return foods.map(FoodResponseDto::fromEntity).getContent();
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