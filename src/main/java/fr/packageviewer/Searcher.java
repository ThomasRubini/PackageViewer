package fr.packageviewer;

import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Searcher {

	private String distributionName;

	public Searcher(String distributionName) {
		this.distributionName = distributionName;
	}

	/**
	 * Get the list of all packages in the distribution setted before
	 * @param packageName the name of the package wanted
	 * @return the list of all packages found
	 * @author Capelier-Marla
	 */
	public List<SearchedPackage> searchPackages(String packageName) {

		// we add all instanced constructors in a list, only one if defined at creation of the object
		List<Distribution> distributions;
		if(distributionName == null) {
			distributions = DistributionEnum.getAllDistributionsInstances();
		} else {
			distributions = Collections.singletonList(DistributionEnum.getDistributionContructorByName(distributionName));
			if(distributions.get(0) == null) {
				System.out.println("Distribution non trouvée");
				System.exit(0);
			}
		}
		// this is the list we will return containing all packages
		List<SearchedPackage> allPackages = new ArrayList<>();
		// this contains all future list of packages to get them after
		List<Future<List<SearchedPackage>>> listFuturePackagesList = new ArrayList<>();

		// we add all future packages in a list
		for (Distribution distribution : distributions) {
			listFuturePackagesList.add(distribution.searchPackage(packageName));
		}

		// we get all packages waiting for them to be received
		for(Future<List<SearchedPackage>> futurePackageList : listFuturePackagesList ) {
			try {
				List<SearchedPackage> tempList = futurePackageList.get();
				allPackages.addAll(tempList);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return allPackages;
	}


	public Package getPackage(SearchedPackage packetInput) {
		if(distributionName == null) {
			distributionName = packetInput.getDistribution();
		}
		String packageName = packetInput.getName();
		Distribution distribution = DistributionEnum.getDistributionContructorByName(distributionName);
		Future<Package> futurePacket = distribution.getPackageTree(packageName, 4);
		Package packet = null;
		try {
			packet = futurePacket.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return packet;
	}
}
