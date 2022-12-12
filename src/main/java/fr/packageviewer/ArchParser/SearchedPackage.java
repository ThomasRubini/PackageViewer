package fr.packageviewer.ArchParser;

public class SearchedPackage {
    String name;
    String version;
    String repo;
    String description;

    public SearchedPackage(String name, String version, String repo, String desciption) {
        this.name = name;
        this.version = version;
        this.repo = repo;
        this.description = desciption;
    }

}
