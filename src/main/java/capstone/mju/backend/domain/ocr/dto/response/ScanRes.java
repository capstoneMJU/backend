package capstone.mju.backend.domain.ocr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScanRes {
    private String productName; // 제품명
    private String itemReportNo; // 품목번호
    private String ocrText; // OCR 전체 텍스트

    @Builder
    public ScanRes(String productName, String itemReportNo, String ocrText){
        this.productName = productName;
        this.itemReportNo = itemReportNo;
        this.ocrText = ocrText;
    }
}
