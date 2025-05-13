package capstone.mju.backend.domain.nutrition.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapExistRes {
    private boolean scrapped;
    @Builder
    public ScrapExistRes(boolean scrapped){
        this.scrapped = scrapped;
    }
}
