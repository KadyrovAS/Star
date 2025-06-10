package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import star.model.*;
import star.model.Transaction;

import java.util.*;

@Service
public class RecommendationService{
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private final RecommendationRuleSet recommendationRuleSet;

    public RecommendationService(@Qualifier("recommendationsChoice") RecommendationRuleSet recommendationRuleSet) {
        this.recommendationRuleSet = recommendationRuleSet;
    }


    public Optional<Recommendations> getRecommendations(UUID id) {
        return recommendationRuleSet.getRecommendation(id);
    }
}
