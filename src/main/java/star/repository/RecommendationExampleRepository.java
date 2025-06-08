package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

}
