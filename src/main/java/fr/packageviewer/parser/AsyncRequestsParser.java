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

public abstract class AsyncRequestsParser {

	private static final Logger logger = LoggerManager.getLogger("AsyncRequestsParser");

	protected abstract CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String name);

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
		}).exceptionally((e2 -> {
			logger.warning("Error while fetching package %s (depth=%s) from the API : \n%s".formatted(packageName, depth, e2));
			e2.printStackTrace();
			futurePackage.complete(null);
			return null;
		}));

		return futurePackage;
	}
}
