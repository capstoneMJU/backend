package capstone.mju.backend.domain.sugarsubstitute.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SugarSubstituteRes {
    private String name;
    private String category;
    private String description;
    private String sideEffect;
    private String giIndex;
    private String calorie;
}

