package capstone.mju.backend.domain.board.dto.comment.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequest {
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private UUID parentId; // Null : 일반 댓글,  NotNull : 대댓글
}
