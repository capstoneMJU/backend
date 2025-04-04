package capstone.mju.backend.domain.ocr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OcrIngredientsRes {
    private String ingredients;
    private String itemReportCode;
}
