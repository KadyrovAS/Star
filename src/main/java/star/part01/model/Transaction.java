package star.part01.model;

/**
 * Класс содержит результат запроса к базе данных банка, сгруппированный по типу транзакций и типу банковского продукта.
 * Используется для проверки выполнения правил банка для клиента
 */

public class Transaction{
    private final int amount;
    private final String transactionType;
    private final String productType;


    public Transaction(int amount, String transactionType, String productType) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.productType = productType;
    }

    public int getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getProductType() {
        return productType;
    }

    public boolean transactionTypeEquals(String type) {
        return type.equals("AMOUNT") || type.equals("DIFFERENCE") || type.equals(transactionType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }

}
