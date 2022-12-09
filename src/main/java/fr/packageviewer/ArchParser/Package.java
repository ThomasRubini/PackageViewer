package fr.packageviewer.ArchParser;

import java.util.List;

public class Package {
    String name;
    String version;
    String repo;
    String description;
    List<Package> deps;
    
    public Package(String name, String version, String repo, String desciption) {
        this.name = name;
        this.version = version;
        this.repo = repo;
        this.description = desciption;
    }
    
}
