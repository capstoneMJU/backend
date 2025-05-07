package capstone.mju.backend.domain.board.dto.like.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Schema(description = "내가 좋아요한 게시글 응답")
public class LikedBoardResponse {

    @Schema(description = "게시글 ID", example = "23afcd67-12fa-4dd3-a5ec-78634f43f231")
    private UUID boardId;

    @Schema(description = "게시글 제목", example = "제로사이다 후기")
    private String title;

    @Schema(description = "게시글 내용", example = "제로사이다 진짜 맛있어요")
    private String content;

    @Schema(description = "작성자 닉네임", example = "제로유저")
    private String author;
}
