package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Recommendation;
import star.model.Rule;
import star.model.Transaction;

import java.util.List;
import java.util.UUID;

@Repository
public class StarRepository{
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(StarRepository.class);

    public StarRepository(@Qualifier("transactionsJdbcTemplate") JdbcTemplate transactionsJdbcTemplate,
                          @Qualifier("rulesJdbcTemplate") JdbcTemplate rulesJdbcTemplate) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
    }

    public List<Transaction> getAmountsByTypes(UUID id) {
        logger.info("id = {}", id);
        return transactionsJdbcTemplate.query(
                "SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, SUM(AMOUNT) AS AMOUNT FROM\n" +
                        "TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ?" +
                        " GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE ",
                (rs, rowNum)->new Transaction(
                        rs.getInt("AMOUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                        ),
                id
        );
    }

    public List<Rule> getAllRules() {
        logger.info("getRules");
        return rulesJdbcTemplate.query(
                "SELECT * FROM RULE",
                (rs, rowNum) -> new Rule(
                        rs.getObject("ID", UUID.class),
                        rs.getString("INSTRUCTION"),
                        rs.getString("ANNOTATION")
                )
        );
    }

    public List<UUID> getRulesById(UUID id){
        logger.info("getRules by ID: {}", id );
        return rulesJdbcTemplate.query(
                "SELECT RULE_ID FROM RECOMMENDATION_RULES WHERE RECOMMENDATION_ID = ?",
                (rs, rowNum)->rs.getObject("RULE_ID", UUID.class),
                id.toString()
        );
    }

    public List<Recommendation> getRecommendations() {
        logger.info("getRecommendations");
        return rulesJdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION",
                (rs, rowNum) -> new Recommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getString("NAME"),
                        rs.getString("ANNOTATION")
                )
        );
    }
}
