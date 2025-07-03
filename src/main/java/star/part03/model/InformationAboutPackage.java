package star.part03.model;

/**
 * Содержит информацию о сборке: наименование и версия
 */
public class InformationAboutPackage{
    private final String name;
    private final String version;

    public InformationAboutPackage(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
