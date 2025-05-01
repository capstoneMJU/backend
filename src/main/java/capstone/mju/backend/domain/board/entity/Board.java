package capstone.mju.backend.domain.board.entity;

import capstone.mju.backend.domain.common.BaseEntity;
import capstone.mju.backend.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "게시글 엔티티")
public class Board extends BaseEntity {

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "게시글 제목", example = "제로사이다 후기")
    private String title;

    @Lob
    @Schema(description = "게시글 내용", example = "제로사이다 진짜 맛있어요! 추천합니다.")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "category_name")
    @Schema(description = "카테고리명", example = "FOOD")
    private Category categoryName;

    @Schema(description = "게시글 이미지 URL", example = "https://s3.bucket.com/image.png")
    private String post_image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "작성자 정보 (User)", implementation = User.class)
    private User user;


    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount = Math.max(0, this.commentCount - 1);
    }



    public void update(String title, String content, Category categoryName, String postImage) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        if (postImage != null) {
            this.post_image = postImage;
        }
    }
}
