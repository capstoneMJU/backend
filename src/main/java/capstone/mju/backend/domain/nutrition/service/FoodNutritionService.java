package capstone.mju.backend.domain.nutrition.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
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

    // 상품명 검색 -> 상품명 나열
    public List<FoodNameResponseDto> searchFoodNames(String keyword, int page) {
        validateSearchKeyword(keyword);
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(keyword, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    // 상품명 -> 영양성분 상세
    public List<FoodNamedetailResponse> getFoodDetailsByFoodNameKr(String foodNmKr, int page) {
        validateSearchKeyword(foodNmKr);
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(foodNmKr, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodNamedetailResponse::fromEntity).getContent();
    }

    // 품목제조보고번호 검색 -> 상품명
    public List<FoodNameResponseDto> searchByItemReportNo(String itemReportNo, int page) {
        validateSearchKeyword(itemReportNo);
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    // 품목제조보고번호 검색 -> 영양성분 상세
    public List<FoodResponseDto> getFoodDetailsByItemReportNo(String itemReportNo, int page) {
        validateSearchKeyword(itemReportNo);
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodResponseDto::fromEntity).getContent();
    }

    // 공통 키워드 검증 메서드
    private void validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD);
        }
    }
}
