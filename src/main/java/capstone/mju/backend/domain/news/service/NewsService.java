package capstone.mju.backend.domain.news.service;

import capstone.mju.backend.domain.news.config.NewsProperties;
import capstone.mju.backend.domain.news.dto.res.NewsResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsProperties newsProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<NewsResponseDto> cachedNews = new ArrayList<>();

    public void refreshNewsCache() {
        List<NewsResponseDto> zeroCalorieNews = fetchNewsFromNaver("제로 칼로리");
        List<NewsResponseDto> aspartameNews = fetchNewsFromNaver("대체당");

        List<NewsResponseDto> combined = new ArrayList<>();
        combined.addAll(zeroCalorieNews);
        combined.addAll(aspartameNews);

        // 날짜 최신순 정렬 (pubDate는 String이므로 파싱 필요)
        combined.sort((a, b) -> {
            ZonedDateTime dateA = ZonedDateTime.parse(a.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
            ZonedDateTime dateB = ZonedDateTime.parse(b.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
            return dateB.compareTo(dateA); // 최신순
        });

        this.cachedNews = combined;
        System.out.println("뉴스 캐시 갱신 완료!");
    }

    public List<NewsResponseDto> getCachedNews() {
        return cachedNews;
    }

    private List<NewsResponseDto> fetchNewsFromNaver(String keyword) {
        List<NewsResponseDto> resultList = new ArrayList<>();
        try {
            String query = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = newsProperties.getUrl()
                    + "?query=" + query
                    + "&sort=date"
                    + "&display=5"
                    + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", newsProperties.getClientId());
            con.setRequestProperty("X-Naver-Client-Secret", newsProperties.getClientSecret());

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JsonNode items = objectMapper.readTree(response.toString()).get("items");

            for (JsonNode item : items) {
                NewsResponseDto dto = NewsResponseDto.builder()
                        .title(item.get("title").asText())
                        .link(item.get("link").asText())
                        .description(item.get("description").asText())
                        .pubDate(item.get("pubDate").asText())
                        .build();
                resultList.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("뉴스 검색 실패 [" + keyword + "]", e);
        }

        return resultList;
    }
}
