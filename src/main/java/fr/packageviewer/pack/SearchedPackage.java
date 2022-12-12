package fr.packageviewer.pack;

public class SearchedPackage {
    private final String name;
    private final String version;
    private final String repo;
    private final String description;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getRepo() {
        return repo;
    }

    public String getDescription() {
        return description;
    }

    public SearchedPackage(String name, String version, String repo, String desciption) {
        this.name = name;
        this.version = version;
        this.repo = repo;
        this.description = desciption;
    }

    @Override
    public String toString() {
        return "SearchedPackage{name=%s,version=%s,repo=%s,description=%s}".formatted(name, version, repo, description);
    }
}
