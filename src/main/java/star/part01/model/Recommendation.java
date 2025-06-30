package star.part01.model;

import java.util.UUID;

/**
 * Класс рекомендации, соответствующий первому этапу рабты
 */
public class Recommendation {
    private final UUID id;
    private final String name;
    private final String annotation;

    public Recommendation(UUID id, String name, String annotation) {
        this.id = id;
        this.name = name;
        this.annotation = annotation;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAnnotation() {
        return annotation;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
