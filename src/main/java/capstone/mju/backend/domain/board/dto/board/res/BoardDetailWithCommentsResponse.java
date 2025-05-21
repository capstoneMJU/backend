package capstone.mju.backend.domain.board.dto.board.res;

import capstone.mju.backend.domain.board.dto.comment.res.CommentTreeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "게시글 상세 + 좋아요 및 댓글 포함 응답")
public class BoardDetailWithCommentsResponse {

    @Schema(description = "게시글 ID", example = "9b2d84d3-404f-4ae1-bd5c-c5b94b8d5247")
    private UUID boardId;

    @Schema(description = "게시글 제목", example = "제로제품 추천")
    private String title;

    @Schema(description = "작성자 닉네임", example = "user123")
    private String nickname;

    @Schema(description = "게시글 작성일 (yyyy-MM-dd)", example = "2025-05-21")
    private String createdDate;

    @Schema(description = "게시글 내용", example = "제로콜라 추천드려요.")
    private String content;

    @Schema(description = "게시글 이미지 URL", example = "https://s3.aws.com/zero.jpg")
    private String postImage;

    @Schema(description = "현재 사용자가 좋아요 눌렀는지 여부", example = "true")
    private boolean liked;

    @Schema(description = "좋아요 수", example = "15")
    private int likeCount;

    @Schema(description = "댓글 수", example = "3")
    private int commentCount;

    @Schema(description = "댓글 목록")
    private List<CommentTreeResponse> comments;
}
