package fr.packageviewer.FedoraParser;

import java.util.List;

public class Package extends SearchedPackage {
    String version;
    List<Package> deps;

    public Package(String name, String version, String repo, String description, List<Package> deps) {
        super(name, repo, description);
        this.version = version;
        this.deps = deps;
    }

}