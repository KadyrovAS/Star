package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Recommendation;
import star.model.Rule;
import star.model.RuleToRecommendation;

@Repository
public class RecommendationExampleRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExampleRepository.class);

    public RecommendationExampleRepository(@Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addRecommendationExample(String sqlLine) {
        logger.info(sqlLine);
        jdbcTemplate.update(sqlLine);
    }

    public void addRecommendation(Recommendation recommendation){
        logger.info("addRecomendation");
        jdbcTemplate.update(
                "INSERT INTO RECOMMENDATION (ID, NAME, ANNOTATION) VALUES (?, ?, ?)",
                recommendation.getId(),
                recommendation.getName(),
                recommendation.getAnnotation()
        );
    }

    public void addRule(Rule rule){
        logger.info("addRule");
        jdbcTemplate.update(
                "INSERT INTO RULE (ID, INSTRUCTION, ANNOTATION) VALUES (?, ?, ?)",
                rule.getId(),
                rule.getInstruction(),
                rule.getAnnotation()
        );
    }

    public void addRecommendationRules(RuleToRecommendation ruleToRecommendation){
        logger.info("addRecommendationRules");
        jdbcTemplate.update(
                "INSERT INTO RECOMMENDATION_RULES (RECOMMENDATION_ID, RULE_ID) VALUES (?, ?)",
                ruleToRecommendation.getRecommendationId(),
                ruleToRecommendation.getRuleId()
        );
    }

    public void deleteAll(){
        logger.info("deleteAll");
        jdbcTemplate.update("DELETE FROM RECOMMENDATION");
        jdbcTemplate.update("DELETE FROM RECOMMENDATION_RULES");
        jdbcTemplate.update("DELETE FROM RULE");
    }
}
