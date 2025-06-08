package star.model;

import java.util.UUID;

public class RuleToRecommendation {
    private final UUID id;
    private final int part;
    private final String ruleName;

    public RuleToRecommendation(UUID id, int part, String ruleName) {
        this.id = id;
        this.part = part;
        this.ruleName = ruleName;
    }

    public UUID getId() {
        return id;
    }

    public int getPart() {
        return part;
    }

    public String getRuleName() {
        return ruleName;
    }

    @Override
    public String toString() {
        return "RuleToRecommendation{" +
                "recommendationId='" + id + '\'' +
                ", part=" + part +
                ", ruleName='" + ruleName + '\'' +
                '}';
    }
}
