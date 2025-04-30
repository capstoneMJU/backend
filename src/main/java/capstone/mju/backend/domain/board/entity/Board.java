package capstone.mju.backend.domain.board.entity;

import capstone.mju.backend.domain.common.BaseEntity;
import capstone.mju.backend.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category_name;

    private String post_image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String title, String content, Category categoryName, String postImage) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (categoryName != null) {
            this.category_name = categoryName;
        }
        if (postImage != null) {
            this.post_image = postImage;
        }
    }

}
