package capstone.mju.backend.domain.news.controller;

import capstone.mju.backend.domain.news.dto.res.NewsResponseDto;
import capstone.mju.backend.domain.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News API", description = "제로 칼로리 및 대체당 관련 뉴스 API")
public class NewsController {

    private final NewsService newsService;

    @Operation(
            summary = "최신 뉴스 목록 조회",
            description = "Naver API를 통해 매 시간마다 갱신된 '제로 칼로리', '대체당' 관련 최신 뉴스를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "뉴스 목록 조회 성공",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NewsResponseDto.class)))),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping
    public List<NewsResponseDto> getNews() {
        return newsService.getCachedNews();
    }
}
