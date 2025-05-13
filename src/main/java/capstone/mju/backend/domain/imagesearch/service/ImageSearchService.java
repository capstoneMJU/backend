package capstone.mju.backend.domain.imagesearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageSearchService {

    private final WebClient webClient;
    private final ImageProperties imageProperties;

    public ImageSearchService(@Qualifier("naverImageWebClient") WebClient webClient,
                              ImageProperties imageProperties) {
        this.webClient = webClient;
        this.imageProperties = imageProperties;
    }

    public Mono<List<String>> searchImageUrls(String query, int display, int start, String sort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/image")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .queryParam("start", start)
                        .queryParam("sort", sort)
                        .build())
                .header("X-Naver-Client-Id", imageProperties.getClientId())
                .header("X-Naver-Client-Secret", imageProperties.getClientSecret())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    List<String> links = new ArrayList<>();
                    JsonNode items = json.get("items");
                    if (items != null && items.isArray()) {
                        for (JsonNode item : items) {
                            links.add(item.get("link").asText());
                        }
                    }
                    return links;
                });
    }

    // 필요한 경우: 가장 첫 번째 이미지 하나만
    public Mono<String> searchFirstImageUrl(String query) {
        return searchImageUrls(query, 1, 1, "sim")
                .map(urls -> urls.isEmpty() ? null : urls.get(0));
    }
}
