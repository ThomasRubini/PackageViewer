package fr.packageviewer.FedoraParser;

import java.util.List;

public class Package extends SearchedPackage {
    String version;
    public List<Package> deps;

    public Package(String name, String version, String repo, String description, List<Package> deps) {
        super(name, repo, description);
        this.version = version;
        this.deps = deps;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Package> getDeps() {
        return deps;
    }

    public void setDeps(List<Package> deps) {
        this.deps = deps;
    }

}