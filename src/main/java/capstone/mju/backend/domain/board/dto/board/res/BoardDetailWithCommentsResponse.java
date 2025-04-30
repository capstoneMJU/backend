package capstone.mju.backend.domain.board.dto.board.res;

import capstone.mju.backend.domain.board.dto.comment.res.CommentTreeResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class BoardDetailWithCommentsResponse {
    private UUID boardId;
    private String title;
    private String nickname;
    private String content;
    private String postImage;
    private boolean liked;
    private int likeCount;
    private int commentCount;
    private List<CommentTreeResponse> comments;
}
