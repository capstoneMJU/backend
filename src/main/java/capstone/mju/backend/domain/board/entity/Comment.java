package capstone.mju.backend.domain.board.entity;

import capstone.mju.backend.domain.common.BaseEntity;
import capstone.mju.backend.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 및 대댓글 엔티티")
public class Comment extends BaseEntity {

    @Lob
    @NotBlank
    @Schema(description = "댓글 내용", example = "이 제품 정말 좋네요!")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @Schema(description = "댓글이 작성된 게시글", implementation = Board.class)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "댓글 작성자", implementation = User.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Schema(description = "부모 댓글 (대댓글일 경우)", implementation = Comment.class, nullable = true)
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) //부모 삭제 -> 자식도 삭제
    @Schema(description = "대댓글 목록", implementation = Comment.class)
    private List<Comment> children = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }
}
