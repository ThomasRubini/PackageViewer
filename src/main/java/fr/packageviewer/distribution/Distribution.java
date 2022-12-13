package fr.packageviewer.distribution;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface Distribution {
	Future<List<SearchedPackage>> searchPackage(String packageName);
	Future<Package> getPackageTree(String packageName, int depth);
}
