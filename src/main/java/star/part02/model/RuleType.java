package star.part02.model;

/**
 * Список команд, которые используются для формализации правил во второй части
 */
public enum RuleType {
    /**
     * Команда USER_OF
     */
    USER_OF,
    /**
     * Команда ACTIVE_USER_OF
     */
    ACTIVE_USER_OF,
    /**
     * Команда TRANSACTION_SUM_COMPARE
     */
    TRANSACTION_SUM_COMPARE,
    /**
     * Команда TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
     */
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
}
