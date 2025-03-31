package capstone.mju.backend.domain.news.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDto {
    private String title;
    private String link;
    private String description;
    private String pubDate;
}