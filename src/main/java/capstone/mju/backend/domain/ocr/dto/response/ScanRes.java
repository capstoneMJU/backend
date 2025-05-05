package capstone.mju.backend.domain.ocr.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "OCR 스캔 결과 DTO")
public class ScanRes {
    @Schema(description = "제품명", example = "제로콜라")
    private String productName; // 제품명

    @Schema(description = "품목보고번호", example = "19970353001114")
    private String itemReportNo; // 품목번호

    @Schema(description = "OCR로 추출된 전체 텍스트", example = "제품명 제로 쿠앤크 샌드품유형 과자 품목보번호|19960242067423 제조판매원 롯데월드(대)")
    private String ocrText; // OCR 전체 텍스트

    @Builder
    public ScanRes(String productName, String itemReportNo, String ocrText){
        this.productName = productName;
        this.itemReportNo = itemReportNo;
        this.ocrText = ocrText;
    }
}
