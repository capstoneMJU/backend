package capstone.mju.backend.domain.ocr.dto.response;

import capstone.mju.backend.domain.sugarsubstitute.dto.res.SugarSubstituteRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConfirmRes {
    private List<SugarSubstituteRes> matchedSubstitutes;
}
