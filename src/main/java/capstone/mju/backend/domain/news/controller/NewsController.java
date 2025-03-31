package capstone.mju.backend.domain.news.controller;

import capstone.mju.backend.domain.news.dto.res.NewsResponseDto;
import capstone.mju.backend.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public List<NewsResponseDto> getNews() {
        return newsService.getCachedNews(); // 매 시간마다 갱신된 뉴스 반환
    }
}