package capstone.mju.backend.domain.ocr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OcrTextRes {
    private String ingredientsText; // OCR로 읽은 원재료명 텍스트
}
