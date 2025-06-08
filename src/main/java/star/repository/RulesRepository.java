package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Rule;

import java.util.List;
import java.util.UUID;

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
                        rs.getObject("ID", UUID.class),
                        rs.getString("INSTRUCTION"),
                        rs.getString("ANNOTATION")
                )
        );
    }

    public Rule addRule(Rule rule) {
        logger.info(rule.toString());
        jdbcTemplate.update(
                "INSERT INTO RULES (" +
                        "ID, INSTRUCTION, ANNOTATION" +
                        ") VALUES (?, ?, ?)",
                rule.getId(),
                rule.getInstruction(),
                rule.getAnnotation()
        );
        return rule;
    }

    public void delete(UUID id) {
        jdbcTemplate.execute("DELETE FROM RULES WHERE ID = '" + id + "'");
    }
}
