package capstone.mju.backend.domain.nutrition.controller;

import capstone.mju.backend.domain.nutrition.dto.res.FoodNameResponseDto;
import capstone.mju.backend.domain.nutrition.dto.res.FoodNamedetailResponse;
import capstone.mju.backend.domain.nutrition.service.FoodNutritionInsertService;
import capstone.mju.backend.domain.nutrition.service.FoodNutritionService;
import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
@Tag(name = "FoodNutrition API", description = "영양 성분 API")
public class FoodController {

    private final FoodNutritionInsertService foodInsertService;
    private final FoodNutritionService foodService;

    @Operation(
            summary = "데이터 수집",
            description = "식품 데이터를 수집하여 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "데이터 수집 완료"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류 (INTERNAL_SERVER_ERROR)")
            }
    )
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData() {
        foodInsertService.fetchAndSaveFoodData();
        return ResponseEntity.ok("데이터 수집 완료");
    }

    @Operation(
            summary = "상품명 검색",
            description = "주어진 키워드로 상품명을 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품명 검색 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FoodNameResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 키워드 입력 (INVALID_SEARCH_KEYWORD)"),
                    @ApiResponse(responseCode = "404", description = "검색 결과 없음 (FOOD_NOT_FOUND)"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류 (INTERNAL_SERVER_ERROR)")
            }
    )
    @GetMapping("/search-names")
    public ResponseEntity<List<FoodNameResponseDto>> searchFoodNames(
            @Parameter(description = "검색할 상품명 키워드", required = true) @RequestParam String name,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page
    ) {
        List<FoodNameResponseDto> result = foodService.searchFoodNames(name, page);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "품목제조보고번호로 상품명 검색",
            description = "주어진 품목제조번호로 상품명을 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품명 검색 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FoodNameResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 키워드 입력 (INVALID_SEARCH_KEYWORD)"),
                    @ApiResponse(responseCode = "404", description = "검색 결과 없음 (FOOD_NOT_FOUND)"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류 (INTERNAL_SERVER_ERROR)")
            }
    )
    @GetMapping("/search-item-name")
    public ResponseEntity<FoodNameResponseDto> searchByItemReportNo(
            @Parameter(description = "품목제조보고번호", required = true)
            @RequestParam String itemReportNo
    ) {
        FoodNameResponseDto result = foodService.searchByItemReportNo(itemReportNo);
        return ResponseEntity.ok(result);
    }



    @Operation(
            summary = "품목제조보고번호로 상세 정보 검색",
            description = "주어진 품목제조번호로 영양성분 상세정보를 검색합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "영양성분 검색 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FoodNamedetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 키워드 입력 (INVALID_SEARCH_KEYWORD)"),
                    @ApiResponse(responseCode = "404", description = "검색 결과 없음 (FOOD_NOT_FOUND)"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류 (INTERNAL_SERVER_ERROR)")
            }
    )
    @GetMapping("/search-item-detail")
    public ResponseEntity<FoodNamedetailResponse> searchByItemReportNoDetail(
            @Parameter(description = "품목제조보고번호", required = true)
            @RequestParam String itemReportNo
    ) {
        FoodNamedetailResponse result = foodService.getFoodDetailsByItemReportNo(itemReportNo);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "ID 기반 식품 상세 조회",
            description = "식품의 고유 ID(PK)를 이용하여 상세 영양정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "식품 고유 ID", required = true, example = "123")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 반환",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FoodNamedetailResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 ID 식품 없음 (FOOD_NOT_FOUND)"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FoodNamedetailResponse> getDetailById(
            @PathVariable Long id
    ) {
        FoodNamedetailResponse detail = foodService.getDetailById(id);
        return ResponseEntity.ok(detail);
    }

    @Operation(
            summary = "ID 기반 대체당 목록 조회",
            description = "식품의 고유 ID(PK)를 이용하여 해당 식품과 연관된 대체당 목록을 조회합니다.(아무도 사진찍은 적이 없으면 대체당 정보도 없음)",
            parameters = {
                    @Parameter(name = "id", description = "식품 고유 ID", required = true, example = "69907")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 반환",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SugarSubstituteRes.class)))),
                    @ApiResponse(responseCode = "404", description = "해당 ID 식품 없음 (FOOD_NOT_FOUND)"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/{id}/sweeteners")
    public ResponseEntity<List<SugarSubstituteRes>> getSweetenersByFoodId(
            @Parameter(description = "조회할 식품의 고유ID", example = "69907")
            @PathVariable Long id
    ) {
        List<SugarSubstituteRes> sweeteners = foodService.getSweetenersByFoodId(id);
        return ResponseEntity.ok(sweeteners);
    }

}
