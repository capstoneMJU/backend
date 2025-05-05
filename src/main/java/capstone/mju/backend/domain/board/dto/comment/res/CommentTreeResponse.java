package capstone.mju.backend.domain.board.dto.comment.res;

import capstone.mju.backend.domain.board.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "댓글 + 대댓글 트리 구조 응답")
public class CommentTreeResponse {

    @Schema(description = "댓글 ID", example = "b19cb06d-1111-2222-aaaa-ccccdddd5678")
    private UUID id;

    @Schema(description = "댓글 내용", example = "저도 이거 써봤어요!")
    private String content;

    @Schema(description = "작성자 닉네임", example = "user456")
    private String username;

    @Schema(description = "작성 시간", example = "2025-05-01T09:10:00")
    private LocalDateTime createdAt;

    @Schema(description = "대댓글 목록")
    private List<CommentTreeResponse> replies;

    public static CommentTreeResponse from(Comment comment) {
        return CommentTreeResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .replies(comment.getChildren().stream()
                        .map(CommentTreeResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
