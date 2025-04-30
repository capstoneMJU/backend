package capstone.mju.backend.domain.board.dto.comment.res;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}
