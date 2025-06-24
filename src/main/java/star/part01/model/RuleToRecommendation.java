package star.part01.model;

import java.util.UUID;

public class RuleToRecommendation {
    private final UUID recommendationId;
    private final UUID ruleId;


    public RuleToRecommendation(UUID recommendationId, UUID ruleId) {
        this.recommendationId = recommendationId;
        this.ruleId = ruleId;
    }

    public UUID getRecommendationId() {
        return recommendationId;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    @Override
    public String toString() {
        return "RuleToRecommendation{" +
                "recommendationId=" + recommendationId +
                ", ruleId=" + ruleId +
                '}';
    }
}
