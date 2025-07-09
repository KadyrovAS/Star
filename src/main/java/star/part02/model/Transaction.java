package star.part02.model;

/**
 * Класс транзакции, содержащий сумму транзакций и количество транзакций определенного типа в отношении
 * банковского продукта определенного типа
 */
public class Transaction{
    private final int amount;
    private final int count;
    private final String transactionType;
    private final String productType;

    /**
     * @param amount          общие обороты по всем транзакциям с transactionType в отношении продуктов с productType;
     * @param count           количество транзакций с transactionType в отношении продуктов с productType;
     * @param transactionType тип транзакции
     * @param productType     тип продукта
     */
    public Transaction(int amount, int count, String transactionType, String productType) {
        this.amount = amount;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", count=" + count +
                ", transactionType='" + transactionType + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
