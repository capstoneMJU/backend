package capstone.mju.backend.domain.news.config;

import capstone.mju.backend.domain.news.service.NewsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsScheduler {

    private final NewsService newsService;

    //매 정각마다 실행
    @Scheduled(cron = "0 0 * * * *")
    public void updateNews() {
        newsService.refreshNewsCache();
    }
    @PostConstruct
    public void init() {
        newsService.refreshNewsCache();
        System.out.println("서버 시작 시 뉴스 최초 로드 완료!");
    }
}