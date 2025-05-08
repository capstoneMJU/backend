package capstone.mju.backend.domain.nutrition.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
import capstone.mju.backend.domain.nutrition.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.nutrition.dto.res.FoodNamedetailResponse;
import capstone.mju.backend.domain.nutrition.entity.FoodNutrition;
import capstone.mju.backend.domain.nutrition.entity.repository.FoodNutritionRepository;
import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import capstone.mju.backend.domain.sugarsubstitute.entity.FoodNutritionSweetener;
import capstone.mju.backend.domain.sugarsubstitute.entity.repository.FoodNutritionSweetenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import capstone.mju.backend.domain.sugarsubstitute.entity.SugarSubstitute;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodNutritionService {

    private final FoodNutritionRepository foodRepository;
    private final FoodNutritionSweetenerRepository foodNutritionSweetenerRepository;

    // 상품명 검색 -> 상품명 나열
    public List<FoodNameResponseDto> searchFoodNames(String foodNmKr, int page) {
        validateSearchKeyword(foodNmKr);
        foodNmKr = foodNmKr.trim();
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(foodNmKr, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodNameResponseDto::fromEntity).getContent();
    }

    // 상품명 -> 영양성분 상세
    public List<FoodNamedetailResponse> getFoodDetailsByFoodNameKr(String foodNmKr, int page) {
        validateSearchKeyword(foodNmKr);
        foodNmKr = foodNmKr.trim();
        Pageable pageable = PageRequest.of(page, 10);
        Page<FoodNutrition> foods = foodRepository.findByFoodNmKrContaining(foodNmKr, pageable);

        if (foods.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_NOT_FOUND);
        }

        return foods.map(FoodNamedetailResponse::fromEntity).getContent();
    }

    // 품목제조보고번호 검색 -> 상품명
    public FoodNameResponseDto searchByItemReportNo(String itemReportNo) {
        validateSearchKeyword(itemReportNo);

        List<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo.trim());

        return foods.stream()
                .filter(food -> {
                    String[] codes = food.getItemReportNo().split("/");
                    for (String code : codes) {
                        if (code.trim().equals(itemReportNo.trim())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .map(FoodNameResponseDto::fromEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }



    // 품목제조보고번호 검색 -> 영양성분 상세
    public FoodNamedetailResponse getFoodDetailsByItemReportNo(String itemReportNo) {
        validateSearchKeyword(itemReportNo);

        List<FoodNutrition> foods = foodRepository.findByItemReportNoContaining(itemReportNo.trim());

        return foods.stream()
                .filter(food -> {
                    String[] codes = food.getItemReportNo().split("/");
                    for (String code : codes) {
                        if (code.trim().equals(itemReportNo.trim())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .map(FoodNamedetailResponse::fromEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
    }



    //이름 검색 시
    public FoodNamedetailResponse getDetailById(Long id) {
        FoodNutrition entity = foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        return FoodNamedetailResponse.fromEntity(entity);
    }

    // 식품 고유 ID로 대체당 조회
    public List<SugarSubstituteRes> getSweetenersByFoodId(Long id) {
        FoodNutrition food = foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        List<FoodNutritionSweetener> mappings = foodNutritionSweetenerRepository.findByFoodNutrition(food);

        return mappings.stream()
                .map(m -> {
                    SugarSubstitute s = m.getSugarSubstitute();
                    return SugarSubstituteRes.builder()
                            .name(s.getName())
                            .category(s.getCategory().getKoreanName())
                            .description(s.getDescription())
                            .sideEffect(s.getSideEffect())
                            .giIndex(s.getGiIndex())
                            .calorie(s.getCalorie())
                            .build();
                }).collect(Collectors.toList());
    }


    // 공통 키워드 검증 메서드
    private void validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD);
        }
    }


}
