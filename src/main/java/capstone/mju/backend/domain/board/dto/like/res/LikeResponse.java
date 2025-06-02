package capstone.mju.backend.domain.board.dto.like.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeResponse {
    private boolean likedByCurrentUser;
}
