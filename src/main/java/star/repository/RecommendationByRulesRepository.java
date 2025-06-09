package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Rule;
import star.model.RuleToRecommendation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RecommendationByRulesRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationByRulesRepository.class);

    public RecommendationByRulesRepository(@Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RuleToRecommendation> getRules() {
        logger.info("getRules");
        return jdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION_RULES",
                (rs, rowNum) -> new RuleToRecommendation(
                        rs.getObject("RECOMMENDATION_ID", UUID.class),
                        rs.getObject("RULE_ID", UUID.class)
                )
        );
    }

    public List<UUID> getRules(UUID id){
        logger.info("getRules by ID: {}", id );
        return jdbcTemplate.query(
        "SELECT RULE_ID FROM RECOMMENDATION_RULES WHERE RECOMMENDATION_ID = ?",
                (rs, rowNum)->rs.getObject("RULE_ID", UUID.class),
                id.toString()
        );
    }

    public RuleToRecommendation addRule(RuleToRecommendation ruleToRecommendation) {
        logger.info(ruleToRecommendation.toString());
        jdbcTemplate.update(
                "INSERT INTO RECOMMENDATION_RULES (" +
                        "RECOMMENDATION_ID, RULE_ID" +
                        ") VALUES (?, ?)",
                ruleToRecommendation.getRecommendationId(),
                ruleToRecommendation.getRuleId()
        );
        return ruleToRecommendation;
    }

    public void delete(UUID id) {
        jdbcTemplate.execute("DELETE FROM RECOMMENDATION_RULES " +
                "WHERE RECOMMENDATION_ID = '" + id + "'");
    }
}
