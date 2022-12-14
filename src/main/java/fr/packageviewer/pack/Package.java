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

    public Package(String name, String version, String repo, String description, String distribution) {
        this(name, version, repo, description, distribution, new ArrayList<>());
    }
    public Package(String name, String version, String repo, String description, String distribution, List<Package> deps) {
        super(name, version, repo, description, distribution);
        this.deps = deps;
    }

    @Override
    public String toString() {
        return "Package{%s,deps=%s}".formatted(super.toString(), deps);
    }
}
