package capstone.mju.backend.domain.ocr.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfirmReq {
    private String itemReportNo;
    private String ocrText;
}
