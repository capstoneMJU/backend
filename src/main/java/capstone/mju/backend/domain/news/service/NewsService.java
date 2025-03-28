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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsProperties newsProperties;

    private List<NewsResponseDto> cachedNews = new ArrayList<>();

    public void refreshNewsCache() {
        this.cachedNews = fetchNewsFromNaver();
        System.out.println("뉴스 캐시 갱신 완료!");
    }

    public List<NewsResponseDto> getCachedNews() {
        return cachedNews;
    }

    private List<NewsResponseDto> fetchNewsFromNaver() {
        List<NewsResponseDto> resultList = new ArrayList<>();
        try {
            String query = URLEncoder.encode("제로 칼로리", "UTF-8");
            String apiURL = newsProperties.getUrl()
                    + "?query=" + query
                    + "&sort=date"
                    + "&display=5" //보여주는 갯수
                    + "&start=1"; //시작 페이지

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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode items = mapper.readTree(response.toString()).get("items");

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
            throw new RuntimeException("뉴스 검색 실패", e);
        }

        return resultList;
    }
}