package star.part02.model;

public class Transaction {
    private final int amount;
    private final int count;
    private final String transactionType;
    private final String productType;


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
