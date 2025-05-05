package capstone.mju.backend.domain.board.dto.comment.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "수정할 댓글 내용을 입력해주세요.")
    @Schema(description = "수정할 댓글 내용", required = true, example = "수정된 댓글입니다.")
    private String content;
}
