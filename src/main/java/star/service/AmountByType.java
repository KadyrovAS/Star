package star.service;

public class AmountByType{
    private int amount;
    private String transactionType;
    private String productType;

    public AmountByType(int amount, String transactionType, String productType) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.productType = productType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "AmountByType{" +
                "amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
