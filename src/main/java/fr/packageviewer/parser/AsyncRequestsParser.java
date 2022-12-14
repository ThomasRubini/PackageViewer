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
 * in an asyncron manner
 * 
 * @author R.Thomas
 * @version 1.0
 * 
 */
public abstract class AsyncRequestsParser {
	/**
    * Logger object used to split debug output and the application output
    */
	private static final Logger logger = LoggerManager.getLogger("AsyncRequestsParser");

	/**
     * This function return a package from the distribution's api in the form 
	 * of a Pair Composed of a Package object, and a set of string containing 
	 * the names of the dependecies of the package.
     * 
     * @param packageName String, The package's exact name
     * @return Pair of Package and Set of String
     */
	protected abstract CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String name);

	/**
	 * 
	 */
	public CompletableFuture<Package> getPackageTree(String packageName, int depth) {
		// parse the json
		var futurePackage = new CompletableFuture<Package>();

		logger.fine("Querying package %s from API... (depth=%s)".formatted(packageName, depth));

		CompletableFuture<Pair<Package, Set<String>>> futureRequest;
		try {
			futureRequest = getPackageFromAPI(packageName);
		} catch (IllegalArgumentException e) {
			logger.warning("Caught exception for package %s :\n%s".formatted(packageName, e));
			return CompletableFuture.completedFuture(null);
		}
		futureRequest.thenAccept(result -> {
			if(result==null){
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

			// TODO this doesn't seem clean
			logger.fine("Completing callback DEPS for package %s (depth=%s)".formatted(packageName, depth));
			futurePackage.complete(pack);
		}).exceptionally(error -> {
			error.printStackTrace();
			logger.warning("Error while manipulating package %s (depth=%s) : \n%s".formatted(packageName, depth, error));
			futurePackage.complete(null);
			return null;
		});

		return futurePackage;
	}
}
