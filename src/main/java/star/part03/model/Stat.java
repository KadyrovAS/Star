package star.part03.model;

/**
 * Используется для получения статистической информации
 */
public class Stat {
    private String ruleName;
    private int count;

    public Stat(String ruleName, int count) {
        this.ruleName = ruleName;
        this.count = count;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "ruleName='" + ruleName + '\'' +
                ", count=" + count +
                '}';
    }
}
