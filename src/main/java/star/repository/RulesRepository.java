package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Rule;

import java.util.List;

@Repository
public class RulesRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RulesRepository.class);

    public RulesRepository(@Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rule> getRules() {
        logger.info("getRules");
        return jdbcTemplate.query(
                "SELECT * FROM RULES",
                (rs, rowNum) -> new Rule(
                        rs.getString("NAME"),
                        rs.getString("PRODUCT_TYPE"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getInt("AMOUNT"),
                        rs.getString("CONDITION"),
                        rs.getString("ANNOTATION")
                )
        );
    }

    public Rule addRule(Rule rule) {
        logger.info(rule.toString());
        jdbcTemplate.update(
                "INSERT INTO RULES (" +
                        "NAME, PRODUCT_TYPE, TRANSACTION_TYPE, AMOUNT, CONDITION, ANNOTATION" +
                        ") VALUES (?, ?, ?, ?, ?, ?)",
                rule.getName(),
                rule.getProductType(),
                rule.getTransactionType(),
                rule.getAmount(),
                rule.getCondition(),
                rule.getAnnotation()
        );
        return rule;
    }

    public void delete(String name) {
        if (name.equals("ALL")) {
            jdbcTemplate.execute("DELETE FROM RULES");
        } else {
            jdbcTemplate.execute("DELETE FROM RULES WHERE NAME = '" + name + "'");
        }
    }
}
