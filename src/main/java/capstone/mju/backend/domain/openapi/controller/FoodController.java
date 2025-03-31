package capstone.mju.backend.domain.openapi.controller;

import capstone.mju.backend.domain.openapi.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.openapi.dto.res.FoodResponseDto;
import capstone.mju.backend.domain.openapi.service.FoodNutritionInsertService;
import capstone.mju.backend.domain.openapi.service.FoodNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodNutritionInsertService foodInsertService;
    private final FoodNutritionService foodService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData() {
        foodInsertService.fetchAndSaveFoodData();
        return ResponseEntity.ok("데이터 수집 완료");
    }

    //상품명 입력
    @GetMapping("/search-names")
    public ResponseEntity<List<FoodNameResponseDto>> searchFoodNames(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<FoodNameResponseDto> result = foodService.searchFoodNames(keyword, page);
        return ResponseEntity.ok(result);
    }

    //품목제조보고 번호 입력 -> 상품명 추출
    @GetMapping("/search-num")
    public ResponseEntity<List<FoodNameResponseDto>> searchByItemReportNo(
            @RequestParam String itemReportNo,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<FoodNameResponseDto> result = foodService.searchByItemReportNo(itemReportNo, page);
        return ResponseEntity.ok(result);
    }

    //품목제조보고 번호 입력 -> 영양성분 표시
    @GetMapping("/search-num-detail")
    public ResponseEntity<List<FoodResponseDto>> searchDetailsByItemReportNo(
            @RequestParam String itemReportNo,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<FoodResponseDto> result = foodService.getFoodDetailsByItemReportNo(itemReportNo, page);
        return ResponseEntity.ok(result);
    }

}