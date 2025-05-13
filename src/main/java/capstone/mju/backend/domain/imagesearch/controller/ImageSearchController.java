package capstone.mju.backend.domain.imagesearch.controller;

import capstone.mju.backend.domain.imagesearch.service.ImageSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image-search")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;

    @Operation(summary = "음식명으로 대표 이미지 검색", description = "네이버 이미지 검색 API를 사용해 음식 이름으로 대표 이미지를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 이미지 URL 반환"),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (API 호출 실패)")
    })
    @GetMapping
    public Mono<ResponseEntity<String>> searchFirstImage(
            @Parameter(description = "음식,제품 이름(한글)") @RequestParam String query
    ) {
        return imageSearchService.searchFirstImageUrl(query)
                .map(url -> url == null
                        ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No image found")
                        : ResponseEntity.ok(url))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("API 호출 실패: " + e.getMessage()))
                );
    }
}
