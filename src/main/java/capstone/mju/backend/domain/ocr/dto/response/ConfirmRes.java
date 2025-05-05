package capstone.mju.backend.domain.ocr.dto.response;

import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ConfirmRes {
    private List<SugarSubstituteRes> matchedSubstitutes;

    @Builder
    public ConfirmRes(List<SugarSubstituteRes> matchedSubstitutes){
        this.matchedSubstitutes = matchedSubstitutes;
    }
}
