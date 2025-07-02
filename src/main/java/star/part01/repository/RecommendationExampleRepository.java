package star.part01.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.part01.model.Recommendation;
import star.part01.model.Rule;
import star.part01.model.RuleToRecommendation;

/**
 * Заполняет базу данных recommendationsPart01.mv.db рекомендациями банка в формализованном виде
 */
@Repository
public class RecommendationExampleRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExampleRepository.class);

    public RecommendationExampleRepository(@Qualifier("rulesJdbcTemplatePart01") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавляет рекомендацию в таблицу RECOMMENDATION
     * @param recommendation Рекомендация банка (устанавливается банком в ТЗ)
     */
    public void addRecommendation(Recommendation recommendation){
        logger.info("addRecommendation");
        String sql = "INSERT INTO RECOMMENDATION (ID, NAME, ANNOTATION) VALUES (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                recommendation.getId().toString(),
                recommendation.getName(),
                recommendation.getAnnotation()
        );
    }

    /**
     * Добавляет правило в таблицу RULE.
     * Поле INSTRUCTION - содержит правило в формализованном виде
     * @param rule Правило. Устанавливается банком.
     */
    public void addRule(Rule rule){
        logger.info("addRule");
        jdbcTemplate.update(
                "INSERT INTO RULE (ID, INSTRUCTION, ANNOTATION) VALUES (?, ?, ?)",
                rule.getId(),
                rule.getInstruction(),
                rule.getAnnotation()
        );
    }

    /**
     * Разные банковские рекомендации могут иметь одинаковые правила.
     * Метод добавляет запись в таблицу RECOMMENDATION_RULES, обеспечивающей связь "many to many" между таблицами
     * RULE и RECOMMENDATION
     * @param ruleToRecommendation
     */
    public void addRecommendationRules(RuleToRecommendation ruleToRecommendation){
        logger.info("addRecommendationRules");
        jdbcTemplate.update(
                "INSERT INTO RECOMMENDATION_RULES (RECOMMENDATION_ID, RULE_ID) VALUES (?, ?)",
                ruleToRecommendation.getRecommendationId(),
                ruleToRecommendation.getRuleId()
        );
    }

    /**
     * Перед добавлением рекомендаций и правил все записи из всех таблиц базы данных удаляются
     */
    public void deleteAll(){
        logger.info("deleteAll");
        jdbcTemplate.update("DELETE FROM RECOMMENDATION");
        jdbcTemplate.update("DELETE FROM RECOMMENDATION_RULES");
        jdbcTemplate.update("DELETE FROM RULE");
    }
}
