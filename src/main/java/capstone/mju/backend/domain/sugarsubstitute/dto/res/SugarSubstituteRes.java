package capstone.mju.backend.domain.sugarsubstitute.dto.res;

import capstone.mju.backend.domain.sugarsubstitute.domain.SugarCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SugarSubstituteRes {
    private String name;
    private SugarCategory category;
    private String description;
    private String sideEffect;
    private String giIndex;
    private String calorie;
}

