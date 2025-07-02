package star.part02.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.part02.model.Transaction;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.service.RuleMapper;
import star.part03.model.SQLCreator;
import star.part03.model.Stat;

import java.util.*;

/**
 * Обеспечивает взаимодействие с базами данных:
 * - transaction.mv.db, предложенная банком и доступная только для чтения;
 * - star - базой данных postgresql
 */
@Repository
public class StarRepositoryPart02{
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private final RuleMapper ruleMapper;
    private static final Logger logger = LoggerFactory.getLogger(StarRepositoryPart02.class);

    public StarRepositoryPart02(@Qualifier("transactionsJdbcTemplate") JdbcTemplate transactionsJdbcTemplate,
                                @Qualifier("rulesJdbcTemplatePart02") JdbcTemplate rulesJdbcTemplate,
                                RuleMapper ruleMapper) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
        this.ruleMapper = ruleMapper;
    }

    /**
     * Возвращает банковское правило по индентификатору
     * @param id идентификатор правила
     * @return Правило банка
     */
    @Cacheable(value = "rules", key = "#id")
    public Rule findRuleById(UUID id) {
        try {
            return rulesJdbcTemplate.queryForObject(
                    SQLCreator.SELECT_FROM_RULE,
                    (rs, rowNum)->ruleMapper.getRule(rs),
                    id.toString()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    private void insertRule(Rule rule, UUID id) {
        Optional<String> argument01;
        Optional<String> argument02 = Optional.empty();
        Optional<String> argument03 = Optional.empty();
        Optional<String> argument04 = Optional.empty();
        argument01 = Optional.ofNullable(rule.getArguments()[0]);
        if (rule.getArguments().length > 1) {
            argument02 = Optional.ofNullable(rule.getArguments()[1]);
        }
        if (rule.getArguments().length > 2) {
            argument03 = Optional.ofNullable(rule.getArguments()[2]);
        }
        if (rule.getArguments().length > 3) {
            argument04 = Optional.ofNullable(rule.getArguments()[3]);
        }

        rulesJdbcTemplate.update(
                SQLCreator.INSERT_INTO_RULE,
                id.toString(),
                rule.getQuery(),
                argument01.orElse(""),
                argument02.orElse(""),
                argument03.orElse(""),
                argument04.orElse(""),
                rule.isNegative());
    }

    @Cacheable(value = "recommendations")
    public List<Recommendation> findAllRecommendations() {
        List<UUID> recommendationsId =
                rulesJdbcTemplate.query(
                        SQLCreator.SELECT_ID_FROM_RECOMMENDATION,
                        (rs, rowNum) -> UUID.fromString(rs.getString("ID"))
                );


        List<Recommendation> recommendations = new LinkedList<>();
        for (UUID id : recommendationsId) {
            recommendations.add(findRecommendationById(id));
        }
        return recommendations;
    }

    @CacheEvict(value = "recommendation", allEntries = true)
    public void clearRecommendationCache() {
        logger.info("Clearing all cached recommendations");
    }

    @Cacheable(value = "recommendation", key = "#id")
    private Recommendation findRecommendationById(UUID id) {

        List<UUID> rulesId = rulesJdbcTemplate.query(
                SQLCreator.SELECT_RULE_ID,
                (rs, rowNum) -> UUID.fromString(rs.getString("RULE_ID")),
                id.toString()
        );

        if (rulesId.isEmpty()) {
            return null;
        }

        int size = rulesId.size();
        Rule[] rules = new Rule[size];
        for (int i = 0; i < size; i++) {
            rules[i] = findRuleById(rulesId.get(i));
        }

        return rulesJdbcTemplate.queryForObject(
                SQLCreator.SELECT_ALL_FROM_RECOMMENDATION,
                (rs, rowNum) -> new Recommendation(
                        UUID.fromString(rs.getString("CONTRACT_ID")),
                        rs.getString("NAME"),
                        rs.getString("TEXT"),
                        rules
                ),
                id.toString()
        );
    }

    /**
     * Добавляет в таблицу RECOMMENDATION новую рекомендацию.
     * - id присваивается сервисом;
     * - CONTRACT_ID - id, установленный банком в id.
     * Добавляет в таблицу RULE_TO_RECOMMENDATION запись, обеспечивающую связь "many to many" между таблицами
     * RULE и RECOMMENDATION
     * @param recommendation
     */
    public void insertRecommendation(Recommendation recommendation) {
        UUID ruleId;
        UUID id = UUID.randomUUID();

        rulesJdbcTemplate.update(
                SQLCreator.INSERT_INTO_RECOMMENDATION,
                id.toString(),
                recommendation.getId().toString(),
                recommendation.getName(),
                recommendation.getText()
        );

        for (Rule rule : recommendation.getRules()) {
            ruleId = UUID.randomUUID();
            rulesJdbcTemplate.update(
                    SQLCreator.INSERT_INTO_RULE_TO_RECOMMENDATION,
                    id.toString(),
                    ruleId.toString()
            );
            insertRule(rule, ruleId);
        }
    }

    /**
     * Удаляет правило по его идентификатору
     * @param id Идентификатор правила
     */
    private void deleteRule(UUID id) {
        rulesJdbcTemplate.update(SQLCreator.DELETE_FROM_RULE, id.toString());
    }

    /**
     * Удаляет рекомендацию по ее идентификатору CONTRACT_ID, установленному банком.
     * Записи с одинаковыми CONTRACT_ID означают выполнение логики OR
     * Также удаляются все связанные записи из таблиц RULE и RULE_TO_RECOMMENDATION
     * @param contractId Идентификатор, установленный банком
     */
    public void deleteRecommendation(UUID contractId) {
        logger.info("deleteRecommendation: contractId = '{}'", contractId);
        List<UUID> listId = rulesJdbcTemplate.query(
                SQLCreator.SELECT_FROM_RECOMMENDATION_BY_CONTRACT_ID,
                (rs, rowNum) -> UUID.fromString(rs.getString("ID")),
                contractId.toString()
        );
        for (UUID id : listId) {
            List<UUID> rulesId = rulesJdbcTemplate.query(
                    SQLCreator.SELECT_ALL_FROM_RULE_TO_RECOMMENDATION,
                    (rs, rowNum) -> UUID.fromString(rs.getString("RULE_ID")),
                    id.toString()
            );

            rulesJdbcTemplate.update(SQLCreator.DELETE_FROM_RULE_TO_RECOMMENDATION, id);

            for (UUID ruleId : rulesId) {
                deleteRule(ruleId);
            }
        }
        rulesJdbcTemplate.update(SQLCreator.DELETE_FROM_RECOMMENDATION, contractId);
    }

    /**
     * Возвращает список Transaction для клиента с заданным идентификатором
     * @param id Идентификатор клиента
     * @return Список транзакций клиента, сгруппированных по типу транзакций и типу банковских операций
     */
    @Cacheable(value = "transactions", key = "#id")
    public List<Transaction> getAmountsByTypes(UUID id) {

        return transactionsJdbcTemplate.query(
                SQLCreator.SELECT_FROM_TRANSACTIONS,
                (rs, rowNum) -> new Transaction(
                        rs.getInt("AMOUNT"),
                        rs.getInt("COUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                ),
                id.toString()
        );
    }

    /**
     * Удаляет все записи из таблиц базы данных star
     */
    public void deleteAll() {
        rulesJdbcTemplate.update(SQLCreator.DELETE_ALL_FROM_RECOMMENDATION);
        rulesJdbcTemplate.update(SQLCreator.DELETE_ALL_FROM_RULE);
        rulesJdbcTemplate.update(SQLCreator.DELETE_ALL_FROM_RULE_TO_RECOMMENDATION);
    }

    /**
     * Возвращает идентификатор клиента по его имени и фамилии
     * @param firstName фамилия клиента
     * @param lastName фамилия клиента
     * @return
     */
    public List<UUID> findUUIDByName(String firstName, String lastName){
        return transactionsJdbcTemplate.query(
                SQLCreator.SELECT_ID_FROM_USERS_BY_NAME,
                (rs, rowNum)->UUID.fromString(rs.getString("ID")),
                firstName,
                lastName
        );
    }

    /**
     * Обновляет таблицу со статистикой для каждого правила
     * @param ruleName Краткое название правила
     */
    public void updateRuleStatistic(String ruleName){
        List<Integer>results = rulesJdbcTemplate.query(
                SQLCreator.SELECT_FROM_RULE_STATISTICS_BY_ID,
                (rs, rowNum)-> rs.getInt("COUNT"),
                ruleName
        );
        if (results.size() == 1){
            int count = results.get(0) + 1;
            rulesJdbcTemplate.update(
                    SQLCreator.UPDATE_RULE_STATISTICS,
                    count,
                    ruleName
            );
        }else{
            rulesJdbcTemplate.update(
                    SQLCreator.INSERT_INTO_RULE_STATISTICS,
                    ruleName,
                    1
            );
        }
    }

    /**
     * Возвращает список результатов статистического сбора информации
     * @return
     */
   public Optional<List<Stat>>getStat(){
        List<Stat>stats = rulesJdbcTemplate.query(
                SQLCreator.SELECT_ALL_RECORDS_FROM_RULE_STATISTICS,
                (rs, rowNum)->new Stat(
                        rs.getString("RULE_NAME"),
                        rs.getInt("COUNT")
                )
        );
        return Optional.of(stats);
   }
}
