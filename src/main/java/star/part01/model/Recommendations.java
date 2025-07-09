package star.part01.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Класс, содержащий массив рекомендаций для клиента с заданным id
 * Разработан на первом этапе
 */
public class Recommendations{
    private final UUID user_id;
    private final Recommendation[] recommendations;

    public Recommendations(UUID userId, Recommendation[] recommendations) {
        this.user_id = userId;
        if (recommendations == null) {
            this.recommendations = new Recommendation[0];
        } else {
            this.recommendations = recommendations;
        }
    }

    @JsonCreator
    public Recommendations(
            @JsonProperty("user_id") UUID userId,
            @JsonProperty("recommendations") List<Recommendation> recommendations) {
        this.user_id = userId;
        if (recommendations == null) {
            this.recommendations = new Recommendation[0];
        } else {
            this.recommendations = recommendations.toArray(new Recommendation[recommendations.size()]);
        }
    }

    @JsonProperty("user_id")
    public UUID getUser_id() {
        return user_id;
    }

    @JsonProperty("recommendations")
    public Recommendation[] getRecommendations() {
        return recommendations;
    }
}
