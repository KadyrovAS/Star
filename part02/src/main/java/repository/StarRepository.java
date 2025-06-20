package repository;

import model.Recommendation;
import model.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class StarRepository{
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(star.repository.StarRepository.class);

    public StarRepository(JdbcTemplate transactionsJdbcTemplate, JdbcTemplate rulesJdbcTemplate) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
    }

    public Rule findRuleByQuery(String query) {
        logger.info("findRuleByAQuery {}", query);
        String sql = "SELECT * FROM RULE WHERE QUERY = ?";

        try {
            return rulesJdbcTemplate.queryForObject(sql, this::mapRowToRule, query);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void insertRule(Rule rule) {
        String sql = "INSERT INTO RULE (QUERY, ARGUMENT01, ARGUMENT02, ARGUMENT03, ARGUMENT04, NEGATIVE) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        rulesJdbcTemplate.update(sql,
                rule.getQuery(),
                rule.getArguments()[0],
                rule.getArguments()[1],
                rule.getArguments()[2],
                rule.getArguments()[3],
                rule.isNegative());
    }

    public Recommendation findRecommendationByID(UUID id){
        String sql = "SELECT * FROM RECOMMENDATION WHERE ID = ?";
        try {
            return rulesJdbcTemplate.queryForObject(sql, new Object[]{id.toString()}, (rs, rowNum) -> {
                String name = rs.getString("NAME");
                String text = rs.getString("TEXT");
                String ruleQuery = rs.getString("RULE");

                // Получаем правило по его query
                Rule rule = findRuleByQuery(ruleQuery);
                Rule[] rules = rule != null ? new Rule[]{rule} : new Rule[0];

                return new Recommendation(id, name, text, rules);
            });
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; // Если рекомендация не найдена
        }
    }

    private Rule mapRowToRule(ResultSet rs, int rowNum) throws SQLException {
        String query = rs.getString("QUERY");
        boolean negative = rs.getBoolean("NEGATIVE");

        String[] arguments = new String[4];
        arguments[0] = rs.getString("ARGUMENT01");
        arguments[1] = rs.getString("ARGUMENT02");
        arguments[2] = rs.getString("ARGUMENT03");
        arguments[3] = rs.getString("ARGUMENT04");

        return new Rule(query, arguments, negative);
    }

}
