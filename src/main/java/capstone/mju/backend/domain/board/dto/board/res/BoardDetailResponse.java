package capstone.mju.backend.domain.board.dto.board.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "게시글 상세 조회 응답")
public class BoardDetailResponse {

    @Schema(description = "게시글 제목", example = "오늘의 건강 루틴")
    private String title;

    @Schema(description = "작성자 닉네임", example = "제로유저1")
    private String nickname;

    @Schema(description = "게시글 본문 내용", example = "아침 6시에 일어나서 스트레칭부터 시작합니다.")
    private String content;

    @Schema(description = "첨부된 이미지 URL", example = "https://s3.amazonaws.com/bucket-name/image.png")
    private String postImage;
}
