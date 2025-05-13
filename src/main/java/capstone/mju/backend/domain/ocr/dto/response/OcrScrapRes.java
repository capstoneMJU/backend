package capstone.mju.backend.domain.ocr.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@Schema(description = "OCR 스크랩 조회 응답 DTO")
public class OcrScrapRes {
    @Schema(description = "식품ID 리스트")
    private List<FoodIdRes> content;
    private int totalPages;
    private long totalElements;

    @Builder
    public OcrScrapRes(List <FoodIdRes> content, int totalPages, long totalElements){
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
