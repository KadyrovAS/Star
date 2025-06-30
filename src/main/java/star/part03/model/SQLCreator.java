package star.part03.model;

public class SQLCreator{
    public static final String SELECT_FROM_RULE = "SELECT * FROM RULE WHERE ID = ?";
    public static final String INSERT_INTO_RULE = "INSERT INTO RULE " +
            "(ID, QUERY, ARGUMENT01, ARGUMENT02, ARGUMENT03, ARGUMENT04, NEGATIVE) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ID_FROM_RECOMMENDATION = "SELECT ID FROM RECOMMENDATION";
    public static final String SELECT_RULE_ID =
            "SELECT RULE_ID FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    public static final String SELECT_ALL_FROM_RECOMMENDATION =
            "SELECT * FROM RECOMMENDATION WHERE ID = ?";
    public static final String INSERT_INTO_RECOMMENDATION = "INSERT INTO RECOMMENDATION " +
            "(ID, CONTRACT_ID, NAME, TEXT) " +
            "VALUES (?, ?, ?, ?)";
    public static final String INSERT_INTO_RULE_TO_RECOMMENDATION =
            "INSERT INTO RULE_TO_RECOMMENDATION " +
                    "(RECOMMENDATION_ID, RULE_ID) " +
                    "VALUES (?, ?)";
    public static final String DELETE_FROM_RULE = "DELETE FROM RULE WHERE ID = ?";
    public static final String SELECT_FROM_RECOMMENDATION_BY_CONTRACT_ID =
            "SELECT * FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
    public static final String SELECT_ALL_FROM_RULE_TO_RECOMMENDATION =
            "SELECT * FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    public static final String DELETE_FROM_RULE_TO_RECOMMENDATION =
            "DELETE FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
    public static final String DELETE_FROM_RECOMMENDATION =
            "DELETE FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
    public static final String SELECT_FROM_TRANSACTIONS =
            """
            SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, 
            SUM(AMOUNT) AS AMOUNT, COUNT(AMOUNT) AS COUNT FROM 
            TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ? 
            GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE""";
    public static final String DELETE_ALL_FROM_RECOMMENDATION = "DELETE FROM RECOMMENDATION";
    public static final String DELETE_ALL_FROM_RULE = "DELETE FROM RULE";
    public static final String DELETE_ALL_FROM_RULE_TO_RECOMMENDATION =
            "DELETE FROM RULE_TO_RECOMMENDATION";
    public static final  String SELECT_ID_FROM_USERS_BY_NAME =
            "SELECT ID FROM USERS WHERE FIRST_NAME = ? AND LAST_NAME = ?";

    public static final String SELECT_FROM_RULE_STATISTICS_BY_ID =
            "SELECT * FROM RULE_STATISTICS WHERE RULE_NAME = ?";
    public static final String UPDATE_RULE_STATISTICS =
            "UPDATE RULE_STATISTICS SET COUNT = ? WHERE RULE_NAME = ?";
    public static final String INSERT_INTO_RULE_STATISTICS =
            "INSERT INTO RULE_STATISTICS (RULE_NAME, COUNT) VALUES (?, ?)";
    public static final String SELECT_ALL_RECORDS_FROM_RULE_STATISTICS =
            "SELECT * FROM RULE_STATISTICS";
}
