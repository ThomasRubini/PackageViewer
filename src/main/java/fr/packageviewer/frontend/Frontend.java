package fr.packageviewer.frontend;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;

public interface Frontend {
    SearchedPackage askUserToChoosePackage(List<SearchedPackage> packets);
    void showPackageTree(Package packet, int depth);
}
