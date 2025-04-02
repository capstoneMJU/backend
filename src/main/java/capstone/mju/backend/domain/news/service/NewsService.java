package capstone.mju.backend.domain.news.service;

import capstone.mju.backend.domain.common.error.ErrorCode;
import capstone.mju.backend.domain.common.exception.CustomException;
import capstone.mju.backend.domain.news.config.NewsProperties;
import capstone.mju.backend.domain.news.dto.res.NewsResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
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

    @Getter
    private List<NewsResponseDto> cachedNews = new ArrayList<>();

    public void refreshNewsCache() {
        List<NewsResponseDto> zeroCalorieNews = fetchNewsFromNaver("제로 칼로리");
        List<NewsResponseDto> aspartameNews = fetchNewsFromNaver("대체당");

        List<NewsResponseDto> combined = new ArrayList<>();
        combined.addAll(zeroCalorieNews);
        combined.addAll(aspartameNews);

        combined.sort((a, b) -> {
            ZonedDateTime dateA = ZonedDateTime.parse(a.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
            ZonedDateTime dateB = ZonedDateTime.parse(b.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
            return dateB.compareTo(dateA);
        });

        this.cachedNews = combined;
        System.out.println("뉴스 캐시 갱신 완료!");
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

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                throw new CustomException(ErrorCode.NEWS_API_ERROR,
                        "Naver API 응답 코드: " + responseCode);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JsonNode root = objectMapper.readTree(response.toString());
            JsonNode items = root.get("items");

            if (items == null || !items.isArray()) {
                throw new CustomException(ErrorCode.NEWS_PARSE_ERROR,
                        "items 필드가 null이거나 배열 형식이 아닙니다.");
            }

            for (JsonNode item : items) {
                NewsResponseDto dto = NewsResponseDto.builder()
                        .title(item.get("title").asText())
                        .link(item.get("link").asText())
                        .description(item.get("description").asText())
                        .pubDate(item.get("pubDate").asText())
                        .build();
                resultList.add(dto);
            }

        } catch (IOException e) {
            throw new CustomException(ErrorCode.NEWS_API_ERROR, "네이버 API 호출 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NEWS_PARSE_ERROR, "뉴스 응답 파싱 실패: " + e.getMessage());
        }

        return resultList;
    }
}
