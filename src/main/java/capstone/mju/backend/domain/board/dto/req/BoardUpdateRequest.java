package capstone.mju.backend.domain.board.dto.req;

import capstone.mju.backend.domain.board.entity.Category;
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
public class BoardUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Category categoryName;
}
