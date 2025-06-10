package star.model;

import java.util.List;
import java.util.UUID;

public class Recommendations{
    private final UUID user_id;
    private final Recommendation[] recommendations;

    public Recommendations(UUID userId, Recommendation[] recommendations) {
        this.user_id = userId;
        this.recommendations = recommendations;
    }

    public Recommendations(UUID userId, List<Recommendation> recommendations) {
        this.user_id = userId;
        this.recommendations = recommendations.toArray(new Recommendation[recommendations.size()]);
    }

    public UUID getUser_id() {
        return user_id;
    }

    public Recommendation[] getRecommendations() {
        return recommendations;
    }
}
