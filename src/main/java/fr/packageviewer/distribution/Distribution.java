package fr.packageviewer.distribution;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;
import java.util.concurrent.Future;

/**
 * This interface specifies the methods needed by a distribution to be parsable.
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

	/**
	 * This function returns a fully completed package containing all
	 * information about the package identified by its exact name passed as
	 * parameter, the package contains in its dependency list fully formed
	 * packages that also contains its dependencies, the dependency depth is
	 * specified by the parameter with the same name.
	 *
	 * @param packageName String, The package's exact name
	 * @param depth       int, the depth of the dependency tree
	 * @return Package, the fully completed package
	 */
	Future<Package> getPackageTree(String packageName, int depth);
}
