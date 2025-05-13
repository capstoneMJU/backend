package capstone.mju.backend.domain.ocr.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class OcrScrapRes {
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
