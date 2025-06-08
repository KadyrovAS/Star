package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.model.Recommendation;
import star.model.RuleToRecommendation;

import java.util.List;
import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationRepository.class);

    public RecommendationRepository(@Qualifier("rulesJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RuleToRecommendation> getRecommendations() {
        logger.info("getRecommendations");
        return jdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION_RULES",
                (rs, rowNum) -> new RuleToRecommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getInt("PART"),
                        rs.getString("RULE_NAME")
                )
        );
    }

    public RuleToRecommendation addRecommendation(RuleToRecommendation ruleToRecommendation) {
        logger.info(ruleToRecommendation.toString());
        jdbcTemplate.update(
                "INSERT INTO RECOMMENDATION_RULES (" +
                        "ID, PART, RULE_NAME" +
                        ") VALUES (?, ?, ?)",
                ruleToRecommendation.getId(),
                ruleToRecommendation.getPart(),
                ruleToRecommendation.getRuleName()
        );
        return ruleToRecommendation;
    }

    public void delete(UUID id) {
            jdbcTemplate.execute("DELETE FROM RECOMMENDATION_RULES WHERE " +
                    "ID = '" + id + "'");
    }

    public List<RuleToRecommendation>getRecommendationNamesWithPart(){
        return jdbcTemplate.query(
                "SELECT DISTINCT ID, PART FROM RECOMMENDATION_RULES",
                (rs, rowNum)->new RuleToRecommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getInt("PART"),
                        null
                )
        );
    }

    public List<String>getRulesByRecommendations(RuleToRecommendation ruleToRecommendation){
        return jdbcTemplate.queryForList(
            "SELECT RULE_NAME FROM RECOMMENDATION_RULES WHERE ID = ? AND PART = ?",
                String.class,
                ruleToRecommendation.getId(),
                ruleToRecommendation.getPart()
        );
    }

    public List<Recommendation>getRecommendationById(UUID id){
        return jdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION WHERE ID = '" + id + "'",
                (rs, rowNum)->new Recommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getString("NAME"),
                        rs.getString("ANNOTATION")
                )
        );
    }

}
