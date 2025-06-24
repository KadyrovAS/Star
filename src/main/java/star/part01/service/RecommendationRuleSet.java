package star.part01.service;

import star.part01.model.Recommendations;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet{
    Optional<Recommendations>getRecommendation(UUID id);
}
