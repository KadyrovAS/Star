package star.part02.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.model.RuleType;
import star.part02.model.Transaction;
import star.part02.repository.StarRepositoryPart02;
import star.part03.model.Stat;

import java.util.*;

/**
 * Обеспечивает подбор рекомендаций банка для клиента по их идентификатору
 */
@Component("servicePart02")
public class RecommendationService implements RecommendationRuleSet {
    private final StarRepositoryPart02 repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(StarRepositoryPart02 repository) {
        this.repository = repository;
    }

    /**
     * Проверяет выполнение правил банка и возвращает список рекомендаций для клиента с заданным идентификатором
     * @param id Идентификатор клиента
     * @return Список рекомендаций
     */
    @Override
    public Optional<List<Recommendation>> findRecommendationById(UUID id) {
        List<Transaction> transactions = repository.getAmountsByTypes(id);
        Map<UUID, Recommendation> resultRecommendations = new HashMap<>();

        boolean isRecommend;
        for (Recommendation recommendation : repository.findAllRecommendations()) {
            isRecommend = true;
            for (Rule rule : recommendation.getRules()) {
                if (!checkRule(rule, transactions)) {
                    isRecommend = false;
                }else {
                    repository.updateRuleStatistic(rule.getQuery());
                }
            }
            if (isRecommend) {
                resultRecommendations.put(recommendation.getId(), recommendation);
            }
        }

        return Optional.of(resultRecommendations.values().stream().toList());
    }

    /**
     * Добавляет рекомендацию в базу данных
     * @param recommendation Рекомендация
     */
    @Override
    public void addRecommendation(Recommendation recommendation) {
        repository.insertRecommendation(recommendation);
    }

    /**
     * Удаляет рекомендацию с идентификатором, указанным банком в ТЗ
     * @param contractId Идентификатор рекомендации, указанный в ТЗ
     */
    @Override
    public void deleteRecommendation(UUID contractId) {
        repository.deleteRecommendation(contractId);
    }

    /**
     * @return Список всех рекомендаций
     */
    @Override
    public List<Recommendation> findAllRecommendations() {
        return repository.findAllRecommendations();
    }

    /**
     * Проверяет выполнение правил для клиента
     * @param rule Правило
     * @param transactions Список транзакций клиента
     * @return true или false, в зависимости от того, выполняется правило или нет
     */
    private boolean checkRule(Rule rule, List<Transaction> transactions) {
        try {
            RuleType ruleType = RuleType.valueOf(rule.getQuery());
            return switch (ruleType) {
                case USER_OF -> checkUserOf(rule, transactions);
                case ACTIVE_USER_OF -> activeUserOf(rule, transactions);
                case TRANSACTION_SUM_COMPARE -> transactionSumCompare(rule, transactions);
                case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW ->
                        transactionSumCompareDepositWithdraw(rule, transactions);
            };
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет выполнение команды USER_OF
     * @param rule правило
     * @param transactions список транзакций клиента
     * @return true или false
     */
    private boolean checkUserOf(Rule rule, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getProductType().equals(rule.getArguments()[0])) {
                return !rule.isNegative();
            }
        }
        return rule.isNegative();
    }

    /**
     * Проверяет выполнение команды ACTIVE_USER_OF
     * @param rule Правило
     * @param transactions Список транзакций клиента
     * @return true или false
     */
    private boolean activeUserOf(Rule rule, List<Transaction> transactions) {
        String productType = rule.getArguments()[0];
        for (Transaction transaction : transactions) {
            if (transaction.getProductType().equals(productType)) {
                return transaction.getCount() > 5 && !rule.isNegative();
            }
        }
        return false;
    }

    /**
     * Прверяет выполнение команды TRANSACTION_SUM_COMPARE
     * @param rule Правило
     * @param transactions Список транзакций клиента
     * @return true или false
     */
    private boolean transactionSumCompare(Rule rule, List<Transaction> transactions) {
        String productType = rule.getArguments()[0];
        String transactionType = rule.getArguments()[1];
        String comp = rule.getArguments()[2];
        int arg = Integer.parseInt(rule.getArguments()[3]);

        int sum = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getProductType().equals(productType) &&
                    transaction.getTransactionType().equals(transactionType)) {
                sum += transaction.getAmount();
            }
        }
        return compare(sum, arg, comp) && !rule.isNegative();
    }

    /**
     * Проверяет выполнение команды TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
     * @param rule правило
     * @param transactions список транзакций
     * @return true или false
     */
    private boolean transactionSumCompareDepositWithdraw(Rule rule, List<Transaction> transactions) {
        int sum = 0;
        String productType = rule.getArguments()[0];
        String comp = rule.getArguments()[1];

        for (Transaction transaction : transactions) {
            if (transaction.getProductType().equals(productType)) {
                if (transaction.getTransactionType().equals("DEPOSIT")) {
                    sum += transaction.getAmount();
                } else {
                    sum -= transaction.getAmount();
                }
            }
        }

        return compare(sum, 0, comp) && !rule.isNegative();
    }

    /**
     * Выполняет операцию сравнения для двух аргументов
     * @param arg1 аргумент 1
     * @param arg2 аргумент 2
     * @param comp знак сравнения
     * @return true или false
     */
    private boolean compare(int arg1, int arg2, String comp) {
        if (arg1 > arg2 && comp.equals(">")) {
            return true;
        } else if (arg1 < arg2 && comp.equals("<")) {
            return true;
        } else if (arg1 == arg2 && comp.equals("=")) {
            return true;
        } else if (arg1 >= arg2 && comp.equals(">=")) {
            return true;
        } else return arg1 <= arg2 && comp.equals("<=");
    }

    /**
     * Возвращает список идентификаторов клиентов по их имени и фамилии
     * @param firstName имя клиента
     * @param lastName фамилия клиента
     * @return список идентификаторов
     */
    @Override
    public List<UUID> findUserIdByName(String firstName, String lastName) {
        return repository.findUUIDByName(firstName, lastName);
    }

    /**
     * Возвращает список статистики о выполнении каждого правила
     * @return список статистики о выполнении каждого правила
     */
    @Override
    public Optional<List<Stat>> findStat() {
        return repository.getStat();
    }

    /**
     * Очищает кэш вызовов
     */
    @Override
    public void toClearCaches() {
        repository.clearRecommendationCache();
    }
}
