package fr.packageviewer.parser;

import fr.packageviewer.LoggerManager;
import fr.packageviewer.Pair;
import fr.packageviewer.pack.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * This abstract class defines the method that a distribution will use
 * in order to get a package and fill its dependency list. It does all that
 * in an asynchronous manner
 *
 * @author R.Thomas
 * @version 1.0
 */
public abstract class AsyncRequestsParser {
	/**
	 * Logger object used to split debug output and the application output
	 */
	private static final Logger logger = LoggerManager.getLogger("AsyncRequestsParser");

	/**
	 * This function returns a package from the distribution's api in the form
	 * of a Pair Composed of a Package object and a set of string containing
	 * the names of the dependencies of the package.
	 *
	 * @param packageName String, The package's exact name
	 * @return Pair of Package and Set of String
	 */
	protected abstract CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName);

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
	public CompletableFuture<Package> getPackageTree(String packageName, int depth) {
		//Wrapper for the package that we'll return
		var futurePackage = new CompletableFuture<Package>();

		logger.fine("Querying package %s from API... (depth=%s)".formatted(packageName, depth));

		CompletableFuture<Pair<Package, Set<String>>> futureRequest;
		try {
			futureRequest = getPackageFromAPI(packageName);
		} catch (IllegalArgumentException e) {
			// If we can't get the package from the api return a null object
			logger.warning("Caught exception for package %s :\n%s".formatted(packageName, e));
			return CompletableFuture.completedFuture(null);
		}
		// When we get the response from the request
		futureRequest.thenAccept(result -> {
			if (result == null) {
				// if there's no response return null object
				logger.fine("Completing callback INVALID for package %s (depth=%s)".formatted(packageName, depth));
				futurePackage.complete(null);
				return;
			}
			Package pack = result.getFirst();
			Set<String> dependenciesNames = result.getSecond();

			// if we're at the maximum depth, return the package without its dependencies
			if (depth == 0) {
				logger.fine("Completing callback NODEP for package %s (depth=%s)".formatted(packageName, depth));
				futurePackage.complete(pack);
				return;
			}

			// iterate for every package in the list
			List<CompletableFuture<Package>> futureDeps = new ArrayList<>();
			for (String depPackageName : dependenciesNames) {
				// convert object into String
				// add package into Package List
				futureDeps.add(getPackageTree(depPackageName, depth - 1));
			}
			// for each future in the list, get the actual package and store
			// into the deps list of the package
			for (CompletableFuture<Package> future : futureDeps) {
				Package dep;
				try {
					dep = future.get();
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
				if (dep != null) {
					pack.addDep(dep);
				}
			}
			logger.fine("Completing callback DEPS for package %s (depth=%s)".formatted(packageName, depth));
			futurePackage.complete(pack);
		}).exceptionally(error -> {
			error.printStackTrace();
			logger.warning(
					"Error while manipulating package %s (depth=%s) : \n%s".formatted(packageName, depth, error));
			futurePackage.complete(null);
			return null;
		});

		return futurePackage;
	}
}
