package star.model;

public class Rule{
    private String ruleName;
    private String productType;
    private String transactionType;
    private int amount;
    private String condition;
    private String annotation;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleName='" + ruleName + '\'' +
                ", productType='" + productType + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", condition='" + condition + '\'' +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
