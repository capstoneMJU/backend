package capstone.mju.backend.domain.recipe.domain;

import capstone.mju.backend.domain.common.BaseEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ingredient")
public class Ingredient extends BaseEntity {
    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    public Ingredient(String name) {
        this.name = name;
    }
}
