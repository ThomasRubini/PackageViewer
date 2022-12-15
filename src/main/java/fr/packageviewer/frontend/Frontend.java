package fr.packageviewer.frontend;

import java.util.List;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

public interface Frontend {
    SearchedPackage askUserToChoosePackage(List<SearchedPackage> packets);
    void showPackageTree(Package packet, int depth);
}
