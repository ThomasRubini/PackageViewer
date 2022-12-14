package fr.packageviewer.distribution;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * This interface specifies the methods needed by a distribtion to be parsable.
 * 
 * @author R.Thomas 
 * @version 1.0 
 */
public interface Distribution {
	/**
     * Search for a package matching a pattern and return a list of packages and
     * return a list of string matching this pattern.
     * 
     * @param packageName String, the pattern to search in the repositories
     * @return List of SearchedPackage objects
     */
	Future<List<SearchedPackage>> searchPackage(String packageName);
	
	Future<Package> getPackageTree(String packageName, int depth);
}
