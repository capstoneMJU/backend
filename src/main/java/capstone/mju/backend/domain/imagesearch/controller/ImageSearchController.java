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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image-search")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;

    @GetMapping
    public Mono<ResponseEntity<String>> searchFirstImage(
            @RequestParam String foodNmKr
    ) {
        return imageSearchService.searchFirstImageUrl(foodNmKr)
                .map(url -> url == null
                        ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No image found")
                        : ResponseEntity.ok(url))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("API 호출 실패: " + e.getMessage()))
                );
    }
}
