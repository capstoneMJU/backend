package capstone.mju.backend.domain.board.dto.comment.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "댓글 내용", required = true, example = "이 댓글은 예시입니다.")
    private String content;

    @Schema(description = "대댓글의 경우 부모 댓글 ID", example = "a3c3b021-812d-4b3e-b3c5-8b4f5f216d7f")
    private UUID parentId; // Null : 일반 댓글, NotNull : 대댓글
}