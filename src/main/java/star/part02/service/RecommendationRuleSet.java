package star.part02.service;


import star.part02.model.Recommendation;
import star.part02.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<List<Recommendation>> findRecommendationById(UUID id);
    void addRecommendation(Recommendation recommendation);
    void deleteRecommendation(UUID id);
    List<Recommendation>findAllRecommendations();
}
