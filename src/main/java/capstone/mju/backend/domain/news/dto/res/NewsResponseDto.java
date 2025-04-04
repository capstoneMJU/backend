package capstone.mju.backend.domain.news.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "네이버 뉴스 검색 결과 DTO")
public class NewsResponseDto {

    @Schema(description = "뉴스 제목", example = "식단 관리 브랜드 비비드키친, '저당 푸룬 드링크' 출시")
    private String title;

    @Schema(description = "뉴스 링크", example = "https://weekly.hankooki.com/news/articleView.html?idxno=7108334")
    private String link;

    @Schema(description = "뉴스 내용", example = "동원홈푸드의 식단 관리 전문 브랜드 비비드키친은 2020년 론칭 이후 다양한 저당·저<b>칼로리</b> 소스를 선보이고 있다. 또한 2023년에는 '저당 라떼' 2종과 '<b>제로</b> 에이드' 2종을 출시하며 음료 카테고리로 진출했다.... ")
    private String description;

    @Schema(description = "뉴스 게시 날짜 (RFC 1123 형식)", example = "Wed, 02 Apr 2025 17:02:00 +0900")
    private String pubDate;
}
