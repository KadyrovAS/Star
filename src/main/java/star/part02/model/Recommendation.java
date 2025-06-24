package star.part02.model;

import java.util.Arrays;
import java.util.UUID;

public class Recommendation{
    private UUID id;
    private String name;
    private String text;
    private Rule[] rules;

    public Recommendation(UUID id, String name, String text, Rule[] rules) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.rules = rules;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Rule[] getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", rules=" + Arrays.toString(rules) +
                '}';
    }
}
