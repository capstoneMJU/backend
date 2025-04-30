package capstone.mju.backend.domain.board.dto.comment.res;

import capstone.mju.backend.domain.board.entity.Comment;
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
public class CommentTreeResponse {
    private UUID id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
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
