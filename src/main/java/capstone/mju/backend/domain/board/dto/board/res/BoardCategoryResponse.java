package capstone.mju.backend.domain.board.dto.board.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardCategoryResponse {
    private String title;
    private String name;
    private String content;
}
