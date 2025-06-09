package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.*;
import star.repository.RecommendationByRulesRepository;
import star.repository.RecommendationRepository;
import star.repository.RulesRepository;
import star.repository.TransactionsRepository;
import star.model.Transaction;
import star.translator.Evaluate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService{
    private final RecommendationRepository repository;
    private final RulesRepository rulesRepository;
    private final TransactionsRepository transactionsRepository;
    private final RecommendationByRulesRepository recommendationByRulesRepository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(RecommendationRepository repository,
                                 RulesRepository rulesRepository,
                                 TransactionsRepository transactionsRepository,
                                 RecommendationByRulesRepository recommendationByRulesRepository) {
        this.repository = repository;
        this.rulesRepository = rulesRepository;
        this.transactionsRepository = transactionsRepository;
        this.recommendationByRulesRepository = recommendationByRulesRepository;
    }

    public List<Recommendation> getRecommendations() {
        logger.info("getRecommendations");
        return repository.getRecommendations();
    }

    public Recommendation addRecommendation(Recommendation recommendation) {
        logger.info("addRecommendation: " + recommendation);
        return repository.addRecommendation(recommendation);
    }

    public void deleteRecommendation(UUID id) {
        logger.info("deleteRecommendation: {}", id);
        repository.delete(id);
    }

    public Collection<Recommendation> getRecommendation(UUID id) {
        logger.info("Запрос рекомендации для {}", id.toString());
        Map<UUID, Boolean> mapWithResult = new HashMap<>();
        Set<Recommendation> setWithRecommendations = new HashSet<>();
        List<Rule> rules = rulesRepository.getRules();
        List<Transaction> transactions = transactionsRepository.getAmountsByTypes(id);
        Evaluate evaluate = new Evaluate(transactions);

        for (Rule rule : rules) {
            boolean result = evaluate.toEvaluate(rule.getInstruction());
            mapWithResult.put(rule.getId(), result);
        }

        for (Recommendation recommendation : repository.getRecommendations()) {
            boolean isRule = true;
            for (UUID uuid : recommendationByRulesRepository.getRules(recommendation.getId())) {
                if (!mapWithResult.get(uuid)) {
                    isRule = false;
                    break;
                }
            }
            if (isRule) {
                setWithRecommendations.add(recommendation);
            }
        }
        return setWithRecommendations;
    }
}
