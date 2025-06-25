package star.part02.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.part02.model.Transaction;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.service.RuleMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StarRepositoryPart02{
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private final RuleMapper ruleMapper;
    private static final Logger logger = LoggerFactory.getLogger(StarRepositoryPart02.class);

    private static final String SELECT_FROM_RULE = "SELECT * FROM RULE WHERE ID = ?";
    private static final String INSERT_INTO_RULE = "INSERT INTO RULE " +
            "(ID, QUERY, ARGUMENT01, ARGUMENT02, ARGUMENT03, ARGUMENT04, NEGATIVE) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ID_FROM_RECOMMENDATION = "SELECT ID FROM RECOMMENDATION";
    private static final String SELECT_RULE_ID =
            "SELECT RULE_ID FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    private static final String SELECT_ALL_FROM_RECOMMENDATION =
            "SELECT * FROM RECOMMENDATION WHERE ID = ?";
    private static final String INSERT_INTO_RECOMMENDATION = "INSERT INTO RECOMMENDATION " +
            "(ID, CONTRACT_ID, NAME, TEXT) " +
            "VALUES (?, ?, ?, ?)";
    private static final String INSERT_INTO_RULE_TO_RECOMMENDATION =
            "INSERT INTO RULE_TO_RECOMMENDATION " +
            "(RECOMMENDATION_ID, RULE_ID) " +
            "VALUES (?, ?)";
    private static final String DELETE_FROM_RULE = "DELETE FROM RULE WHERE ID = ?";
    private static final String SELECT_FROM_RECOMMENDATION_BY_CONTRACT_ID =
            "SELECT * FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
    private static final String SELECT_ALL_FROM_RULE_TO_RECOMMENDATION =
            "SELECT * FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    private static final String DELETE_FROM_RULE_TO_RECOMMENDATION =
            "DELETE FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    private static final String DELETE_FROM_RECOMMENDATION =
            "DELETE FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
    private static final String SELECT_FROM_TRANSACTIONS =
            """
            SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, 
            SUM(AMOUNT) AS AMOUNT, COUNT(AMOUNT) AS COUNT FROM 
            TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ? 
            GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE""";
    private static final String DELETE_ALL_FROM_RECOMMENDATION = "DELETE FROM RECOMMENDATION";
    private static final String DELETE_ALL_FROM_RULE = "DELETE FROM RULE";
    private static final String DELETE_ALL_FROM_RULE_TO_RECOMMENDATION =
            "DELETE FROM RULE_TO_RECOMMENDATION";

    public StarRepositoryPart02(@Qualifier("transactionsJdbcTemplate") JdbcTemplate transactionsJdbcTemplate,
                                @Qualifier("rulesJdbcTemplatePart02") JdbcTemplate rulesJdbcTemplate, RuleMapper ruleMapper) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
        this.ruleMapper = ruleMapper;
    }

    @Cacheable(value = "rules", key = "#id")
    public Rule findRuleById(UUID id) {
        try {
            return rulesJdbcTemplate.queryForObject(
                    SELECT_FROM_RULE,
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
                INSERT_INTO_RULE,
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
                        SELECT_ID_FROM_RECOMMENDATION,
                        (rs, rowNum) -> UUID.fromString(rs.getString("ID"))
                );


        List<Recommendation> recommendations = new LinkedList<>();
        for (UUID id : recommendationsId) {
            recommendations.add(findRecommendationById(id));
        }
        return recommendations;
    }

    @Cacheable(value = "recommendation", key = "#id")
    private Recommendation findRecommendationById(UUID id) {

        List<UUID> rulesId = rulesJdbcTemplate.query(
                SELECT_RULE_ID,
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
                SELECT_ALL_FROM_RECOMMENDATION,
                (rs, rowNum) -> new Recommendation(
                        UUID.fromString(rs.getString("CONTRACT_ID")),
                        rs.getString("NAME"),
                        rs.getString("TEXT"),
                        rules
                ),
                id.toString()
        );
    }

    public void insertRecommendation(Recommendation recommendation) {
        UUID ruleId;
        UUID id = UUID.randomUUID();

        rulesJdbcTemplate.update(
                INSERT_INTO_RECOMMENDATION,
                id.toString(),
                recommendation.getId().toString(),
                recommendation.getName(),
                recommendation.getText()
        );

        for (Rule rule : recommendation.getRules()) {
            ruleId = UUID.randomUUID();
            rulesJdbcTemplate.update(
                    INSERT_INTO_RULE_TO_RECOMMENDATION,
                    id.toString(),
                    ruleId.toString()
            );
            insertRule(rule, ruleId);
        }
    }

    private void deleteRule(UUID id) {
        rulesJdbcTemplate.update(DELETE_FROM_RULE, id.toString());
    }

    public void deleteRecommendation(UUID contractId) {
        logger.info("deleteRecommendation: contractId = '{}'", contractId);
        List<UUID> listId = rulesJdbcTemplate.query(
                SELECT_FROM_RECOMMENDATION_BY_CONTRACT_ID,
                (rs, rowNum) -> UUID.fromString(rs.getString("ID")),
                contractId.toString()
        );
        for (UUID id : listId) {
            List<UUID> rulesId = rulesJdbcTemplate.query(
                    SELECT_ALL_FROM_RULE_TO_RECOMMENDATION,
                    (rs, rowNum) -> UUID.fromString(rs.getString("RULE_ID")),
                    id.toString()
            );

            rulesJdbcTemplate.update(DELETE_FROM_RULE_TO_RECOMMENDATION, id);

            for (UUID ruleId : rulesId) {
                deleteRule(ruleId);
            }
        }
        rulesJdbcTemplate.update(DELETE_FROM_RECOMMENDATION, contractId);
    }

    private record RecommendationRecord(String name, String text, String query){
    }


    @Cacheable(value = "transactions", key = "#id")
    public List<Transaction> getAmountsByTypes(UUID id) {

        return transactionsJdbcTemplate.query(
                SELECT_FROM_TRANSACTIONS,
                (rs, rowNum) -> new Transaction(
                        rs.getInt("AMOUNT"),
                        rs.getInt("COUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                ),
                id.toString()
        );
    }

    public void deleteAll() {
        rulesJdbcTemplate.update(DELETE_ALL_FROM_RECOMMENDATION);
        rulesJdbcTemplate.update(DELETE_ALL_FROM_RULE);
        rulesJdbcTemplate.update(DELETE_ALL_FROM_RULE_TO_RECOMMENDATION);
    }

}
