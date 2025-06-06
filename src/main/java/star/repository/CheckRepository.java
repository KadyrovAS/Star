package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.service.AmountByType;

import java.util.List;
import java.util.UUID;

@Repository
public class CheckRepository{
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CheckRepository.class);

    public CheckRepository(@Qualifier("transactionsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTypes(UUID user){
        logger.info("getTypes " + user);
        List<String> result = jdbcTemplate.queryForList(
                "SELECT DISTINCT  TYPE FROM TRANSACTIONS t  WHERE USER_ID  = ?",
                String.class,
                user
        );
        logger.info("getTypes result " + result);
        return result;
    }

    public Integer getSum(UUID id, String type){
        logger.info("id = " + id + ", type = " + type);
        Integer result = jdbcTemplate.queryForObject(
                "SELECT SUM(AMOUNT) FROM TRANSACTIONS WHERE user_id = ? AND TYPE = ? GROUP BY type",
                Integer.class,
                id,
                type
        );
        return result;
    }

    public List<AmountByType> getAmountsByTypes(UUID id) {
        logger.info("id = " + id);
        List<AmountByType> result = jdbcTemplate.query(
                "SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, SUM(AMOUNT) AS AMOUNT FROM\n" +
                        "TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ?" +
                        " GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE ",
                (rs, rowNum)->new AmountByType(
                        rs.getInt("AMOUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                        ),
                id
        );
        return result;
    }
}
