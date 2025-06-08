package star.model;

public class Rule{
    private String name;
    private String productType;
    private String transactionType;
    private int amount;
    private String condition;
    private String annotation;

    public Rule(String name,
                String productType,
                String transactionType,
                int amount,
                String condition,
                String annotation) {
        this.name = name;
        this.productType = productType;
        this.transactionType = transactionType;
        this.amount = amount;
        this.condition = condition;
        this.annotation = annotation;
    }

    public String getName() {
        return name;
    }

    public String getProductType() {
        return productType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public String getCondition() {
        return condition;
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
                "ruleName='" + name + '\'' +
                ", productType='" + productType + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", condition='" + condition + '\'' +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
