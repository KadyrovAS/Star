package star.service;

import star.model.Recommendations;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet{
    Optional<Recommendations>getRecommendation(UUID id);
}
