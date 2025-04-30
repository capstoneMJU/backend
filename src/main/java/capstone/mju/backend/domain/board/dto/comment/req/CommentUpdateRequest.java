package capstone.mju.backend.domain.board.dto.comment.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
    private String content;
}
