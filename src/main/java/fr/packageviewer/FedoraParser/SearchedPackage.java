package fr.packageviewer.FedoraParser;

public class SearchedPackage {
    String name;
    String repo;
    String description;

    public SearchedPackage(String name, String repo, String desciption) {
        this.name = name;
        this.repo = repo;
        this.description = desciption;
    }

}