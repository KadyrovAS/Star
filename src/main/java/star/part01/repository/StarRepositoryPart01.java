package star.part01.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.part01.model.Recommendation;
import star.part01.model.Rule;
import star.part01.model.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий обеспечивает взаимодействие с двумя базами данных:
 * transaction.mv.db - предоставляется банком. Доступ только для чтения
 * recommendationsPart01.mv.db - создана на 1-м этапе работы. Содержит рекомендации и правила банка в формализованном
 * виде.
 */
@Repository
public class StarRepositoryPart01 {
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(StarRepositoryPart01.class);

    public StarRepositoryPart01(@Qualifier("transactionsJdbcTemplate") JdbcTemplate transactionsJdbcTemplate,
                                @Qualifier("rulesJdbcTemplatePart01") JdbcTemplate rulesJdbcTemplate) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
    }

    /**
     *
     * @param id Идентификатор клиента банка
     * @return Список транзакций, сгруппированный по типу транзакций и типу банковских продуктов.
     * На основании этого списка осуществляется проверка выполнения банковских правил
     */
    public List<Transaction> getAmountsByTypes(UUID id) {
        logger.info("id = {}", id);
        return transactionsJdbcTemplate.query(
                "SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, SUM(AMOUNT) AS AMOUNT FROM\n" +
                        "TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ?" +
                        " GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE ",
                (rs, rowNum)->new Transaction(
                        rs.getInt("AMOUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                        ),
                id
        );
    }

    /**
     * @return возвращает список всех банковских правил
     */
    public List<Rule> getAllRules() {
        logger.info("getRules");
        return rulesJdbcTemplate.query(
                "SELECT * FROM RULE",
                (rs, rowNum) -> new Rule(
                        rs.getObject("ID", UUID.class),
                        rs.getString("INSTRUCTION"),
                        rs.getString("ANNOTATION")
                )
        );
    }

    /**
     *
     * @param id Идентификатор банковской рекомендации (установлен банком в ТЗ).
     * @return Список идентификаторов банковских правил (присваиваются сервисом)
     */
    public List<UUID> getRulesById(UUID id){
        logger.info("getRules by ID: {}", id );
        return rulesJdbcTemplate.query(
                "SELECT RULE_ID FROM RECOMMENDATION_RULES WHERE RECOMMENDATION_ID = ?",
                (rs, rowNum)->rs.getObject("RULE_ID", UUID.class),
                id.toString()
        );
    }

    /**
     *
     * @return Список всех банковских рекомендаций
     */
    public List<Recommendation> getRecommendations() {
        logger.info("getRecommendations");
        return rulesJdbcTemplate.query(
                "SELECT * FROM RECOMMENDATION",
                (rs, rowNum) -> new Recommendation(
                        rs.getObject("ID", UUID.class),
                        rs.getString("NAME"),
                        rs.getString("ANNOTATION")
                )
        );
    }
}
