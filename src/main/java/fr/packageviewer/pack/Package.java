package fr.packageviewer.pack;

import java.util.List;

public class Package extends SearchedPackage {
    private final List<Package> deps;

    public List<Package> getDeps() {
        return deps;
    }

    public Package(String name, String version, String repo, String description, List<Package> deps) {
        super(name, version, repo, description);
        this.deps = deps;
    }

    @Override
    public String toString() {
        return "Package{%s,deps=%s}".formatted(super.toString(), deps);
    }
}
