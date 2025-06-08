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

    public List<Recommendation> getRecommendations() {
        logger.info("getRecommendations");
        return jdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION",
                (rs, rowNum) -> new Recommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getString("NAME"),
                        rs.getString("ANNOTATION")
                )
        );
    }

    public Recommendation addRecommendation(Recommendation recommendation){
        logger.info("addRecommendation: " + recommendation);
        jdbcTemplate.update("INSERT INTO RECOMMENDATION (" +
            "ID, NAME, ANNOTATION) " +
            "VALUES (?, ?, ?)",
            recommendation.getId(),
            recommendation.getName(),
            recommendation.getAnnotation()
        );
        return recommendation;
    }

    public void delete(UUID id) {
            jdbcTemplate.execute("DELETE FROM RECOMMENDATION WHERE " +
                    "ID = '" + id + "'");
    }

}
