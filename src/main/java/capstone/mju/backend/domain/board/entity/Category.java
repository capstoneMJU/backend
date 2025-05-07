package capstone.mju.backend.domain.board.entity;

public enum Category {
    ZERO_PRODUCT_REVIEW("제로제품 후기"),
    RECIPE("레시피"),
    FREE_BOARD("자유게시판");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}