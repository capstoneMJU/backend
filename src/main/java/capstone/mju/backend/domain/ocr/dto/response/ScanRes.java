package capstone.mju.backend.domain.ocr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScanRes {
    private String productName; // 제품명
    private String itemReportNo; // 품목번호
    private String ocrText; // OCR 전체 텍스트
}
