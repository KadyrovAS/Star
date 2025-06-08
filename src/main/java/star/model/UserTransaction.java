package star.model;

public class UserTransaction {
    private int amount;
    private String transactionType;
    private String productType;

    public UserTransaction(int amount, String transactionType, String productType) {
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

    @Override
    public String toString() {
        return "UserTransaction{" +
                "amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
