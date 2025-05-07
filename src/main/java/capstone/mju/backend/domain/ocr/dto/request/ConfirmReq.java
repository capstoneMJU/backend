package capstone.mju.backend.domain.ocr.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "OCR로 추출된 텍스트와 품목번호를 포함한 대체당 확인 요청 DTO")
public class ConfirmReq {
    @Schema(description = "OCR로부터 추출된 품목보고번호", example = "19970353001114")
    private String itemReportNo;

    @Schema(description = "OCR로부터 추출된 전체 텍스트", example = "제품명 제로 쿠앤크 샌드품유형 과자 품목보번호|19960242067423 제조판매원 롯데월드(대)")
    private String ocrText;
}
