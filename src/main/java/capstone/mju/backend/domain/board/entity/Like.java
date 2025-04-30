package capstone.mju.backend.domain.board.entity;

import capstone.mju.backend.domain.common.BaseEntity;
import capstone.mju.backend.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "board_id"}))
@Schema(description = "게시글 좋아요 엔티티")
public class Like extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "좋아요 누른 사용자", implementation = User.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @Schema(description = "좋아요 대상 게시글", implementation = Board.class)
    private Board board;
}
