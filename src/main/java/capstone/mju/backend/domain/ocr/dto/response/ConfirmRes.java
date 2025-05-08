package capstone.mju.backend.domain.ocr.dto.response;

import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "대체당 정보 응답 DTO")
public class ConfirmRes {
    @Schema(description = "OCR 텍스트에서 추출된 대체당 정보 리스트")
    private List<SugarSubstituteRes> matchedSubstitutes;

    @Builder
    public ConfirmRes(List<SugarSubstituteRes> matchedSubstitutes){
        this.matchedSubstitutes = matchedSubstitutes;
    }
}
