package star.part03.model;

public class InformationAboutPackage{
    private String name;
    private String version;

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
