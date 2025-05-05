package capstone.mju.backend.domain.sugarsubstitute.domain;

public enum SugarCategory {
    ARTIFICIAL("인공 감미료"), // 인공 감미료(합성 감미료)
    NATURAL("천연 감미료"), // 천연 감미료
    SUGAR_ALCOHOL("당알콜류"), // 당알콜류
    COMPOUND("혼합 감미료"), // 혼합제제 ((예: 감미료(수크랄로스, 아세설팜칼륨))
    OTHER("기타"); // 기타

    private final String koreanName;

    SugarCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
