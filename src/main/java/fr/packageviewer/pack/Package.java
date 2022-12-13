package fr.packageviewer.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Package extends SearchedPackage {
    private final List<Package> deps;

    public List<Package> getDeps() {
        return deps;
    }

    public void addDep(Package pack) {
        deps.add(pack);
    }

    public Package(String name, String version, String repo, String description) {
        this(name, version, repo, description, new ArrayList<>());
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
