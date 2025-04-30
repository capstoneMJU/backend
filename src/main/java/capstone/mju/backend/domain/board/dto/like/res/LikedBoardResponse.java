package capstone.mju.backend.domain.board.dto.like.res;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LikedBoardResponse {
    private UUID boardId;
    private String title;
    private String content;
    private String author;
}
