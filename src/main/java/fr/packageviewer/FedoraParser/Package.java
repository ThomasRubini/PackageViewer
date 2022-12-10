package fr.packageviewer.FedoraParser;

import java.util.List;

public class Package extends SearchedPackage {
    List<Package> deps;

    public Package(String name, String version, String repo, String description, List<Package> deps) {
        super(name, version, repo, description);
        this.deps = deps;
    }

}