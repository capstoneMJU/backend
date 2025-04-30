package capstone.mju.backend.domain.board.dto.board.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "카테고리별 게시글 응답")
public class BoardCategoryResponse {

    @Schema(description = "게시글 제목", example = "자취생을 위한 꿀팁")
    private String title;

    @Schema(description = "작성자 이름", example = "제로픽")
    private String name;

    @Schema(description = "게시글 내용", example = "냉장고 정리 요령 공유합니다.")
    private String content;
}
