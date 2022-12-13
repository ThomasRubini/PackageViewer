package fr.packageviewer.distribution;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Distribution {
	CompletableFuture<List<SearchedPackage>> searchPackage(String packageName);
	CompletableFuture<Package> getPackageTree(String packageName, int depth);
}
