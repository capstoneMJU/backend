package capstone.mju.backend.domain.board.dto.comment.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "댓글 응답 정보")
public class CommentResponse {

    @Schema(description = "댓글 ID", example = "a14bb06d-0000-1111-bbbb-ccccdddd1234")
    private UUID id;

    @Schema(description = "댓글 내용", example = "좋은 정보 감사합니다!")
    private String content;

    @Schema(description = "작성자 닉네임", example = "user123")
    private String username;

    @Schema(description = "작성 시간", example = "2025-05-01T10:20:30")
    private LocalDateTime createdAt;
}
