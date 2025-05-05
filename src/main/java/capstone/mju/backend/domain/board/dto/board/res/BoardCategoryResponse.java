package capstone.mju.backend.domain.board.dto.board.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "카테고리별 게시글 응답")
public class BoardCategoryResponse {
    @Schema(description = "게시글 ID", example = "ec54a7b6-2a47-4c77-b294-72ea4dcb6584")
    private UUID boardId; // boardId 추가

    @Schema(description = "게시글 제목", example = "자취생을 위한 꿀팁")
    private String title;

    @Schema(description = "작성자 이름", example = "제로픽")
    private String name;

    @Schema(description = "게시글 내용", example = "냉장고 정리 요령 공유합니다.")
    private String content;

    @Schema(description = "좋아요 수", example = "15")
    private int likeCount;

    @Schema(description = "댓글 수", example = "3")
    private int commentCount;
}
