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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}