package capstone.mju.backend.domain.sugarsubstitute.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class SugarSubstitute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SugarCategory category; // 분류

    private String name; // 이름

    @Column(length = 1000)
    private String description; // 설명

    private String sideEffect; // 부작용

    private String giIndex; // GI지수

    private String calorie; // 칼로리
}
