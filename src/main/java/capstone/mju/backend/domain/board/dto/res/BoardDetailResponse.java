package capstone.mju.backend.domain.board.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardDetailResponse {

    private String title;
    private String nickname;
    private String content;
    private String postImage;
}