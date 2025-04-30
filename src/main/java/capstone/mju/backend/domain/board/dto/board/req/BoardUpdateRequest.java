package capstone.mju.backend.domain.board.dto.board.req;

import capstone.mju.backend.domain.board.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 수정 요청")
public class BoardUpdateRequest {

    @NotBlank
    @Schema(description = "수정할 게시글 제목", example = "제로제품 추천 업데이트")
    private String title;

    @NotBlank
    @Schema(description = "수정할 게시글 본문", example = "제로사이다도 괜찮더라구요.")
    private String content;

    @NotNull
    @Schema(description = "수정할 카테고리", example = "ZERO_REVIEW")
    private Category categoryName;
}
