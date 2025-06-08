package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.Recommendation;
import star.model.UserTransaction;
import star.model.RuleToRecommendation;
import star.model.Rule;
import star.repository.RecommendationRepository;
import star.repository.RulesRepository;
import star.repository.TransactionsRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final RecommendationRepository repository;
    private final RulesRepository rulesRepository;
    private final TransactionsRepository transactionsRepository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(RecommendationRepository repository, RulesRepository rulesRepository, TransactionsRepository transactionsRepository) {
        this.repository = repository;
        this.rulesRepository = rulesRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public List<RuleToRecommendation> getRecommendations(){
        logger.info("getRecommendations");
        return repository.getRecommendations();
    }

    public RuleToRecommendation addRecommendation(RuleToRecommendation recommendation){
        logger.info("addRecommendation: {}", recommendation);
        return repository.addRecommendation(recommendation);
    }

    public void deleteRecommendation(UUID id){
        logger.info("deleteRecommendation: {}", id);
        repository.delete(id);
    }

    /**
     * Возвращает список рекомендаций для id, в зависимости от сделанных им транзакций
     * @param id
     * @return
     */
    public List<Recommendation> getRecommendation(UUID id) {
        logger.info("Запрос рекомендации для {}", id.toString());
        Map<String, Boolean> map = new HashMap<>();
        Set<UUID> set = new HashSet<>();
        List<Rule> rules = rulesRepository.getRules();
        List<UserTransaction> userTransactions = transactionsRepository.getAmountsByTypes(id);

        for (Rule rule : rules) {
            map.put(rule.getName(), checkRule(rule, userTransactions));
        }

        boolean isRecommendTo;
        for (RuleToRecommendation recommendation: repository.getRecommendationNamesWithPart()){
            isRecommendTo = true;
            for (String rule: repository.getRulesByRecommendations(recommendation)){
                if (!map.get(rule)){
                    isRecommendTo = false;
                    break;
                }
            }
            if (isRecommendTo) {
                set.add(recommendation.getId());
            }
        }

        return set.stream()
                .map(repository::getRecommendationById)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает true, если правило клиентом выполнилось
     * @param rule
     * @param userTransactions
     * @return
     */
    private boolean checkRule(Rule rule, List<UserTransaction> userTransactions) {
        int amount = 0;

        logger.info("CheckRule: rule {}, userTransactions {}", rule, userTransactions);
        for (UserTransaction userTransaction : userTransactions) {
            if (checkAllTypes(rule.getTransactionType(),
                    userTransaction.getTransactionType(),
                    rule.getProductType(),
                    userTransaction.getProductType()
            )) {
                amount += userTransaction.getAmount();
            }
        }

        if (rule.getCondition().equals("=") && amount == rule.getAmount()){
            return true;
        }
        if (rule.getCondition().equals(">") && amount > rule.getAmount()){
            return true;
        }
        if (rule.getCondition().equals("<") && amount < rule.getAmount()){
            return true;
        }
        if (rule.getCondition().equals(">=") && amount >= rule.getAmount()){
            return true;
        }
        if (rule.getCondition().equals("<=") && amount <= rule.getAmount()){
            return true;
        }
        return rule.getCondition().equals("!=") && amount != rule.getAmount();
    }

    /**
     * Возвращает true, если тип транзакции и тип продукта соответствуют правилу
     * @param ruleTransactionType
     * @param userTransactionType
     * @param ruleProductType
     * @param userProductType
     * @return
     */
    private boolean checkAllTypes(String ruleTransactionType,
                                  String userTransactionType,
                                  String ruleProductType,
                                  String userProductType) {
        logger.info("ruleTransactionType: {}, amountTransactionType: {}, ruleProductType: {}, amountProductType: {}",
                ruleTransactionType, userTransactionType, ruleProductType, userProductType);
        if ((ruleTransactionType.isBlank() || userTransactionType.isBlank())
                && ruleProductType.equals(userProductType)) {
            return true;
        }
        if (ruleTransactionType.equals(userTransactionType) &&
                (ruleProductType.isBlank() || userProductType.isBlank())){
            return true;
        }
        return ruleTransactionType.equals(userTransactionType) &&
                ruleProductType.equals(userProductType);
    }

}
