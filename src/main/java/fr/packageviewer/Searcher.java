package fr.packageviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.distribution.FedoraDistribution;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.pack.Package;

public class Searcher {
    public static List<SearchedPackage> searchForAll(String packet) {
		// init distribution  to search in it
		Distribution arch = new ArchDistribution();
		Distribution fedora = new FedoraDistribution();
		// search for the package in the distribution
		Future<List<SearchedPackage>> archPackages = arch.searchPackage(packet);
		Future<List<SearchedPackage>> fedoraPackages = fedora.searchPackage(packet);
		// init the list of packages that will be returned
		List<SearchedPackage> archResult = new ArrayList<>();
		List<SearchedPackage> fedoraResult = new ArrayList<>();
		// try to get the searched packages to return it after
		try {
			archResult = archPackages.get();
			fedoraResult = fedoraPackages.get();
		} catch (Exception e) {
			/* TODO: handle exception */
		}
		archResult.addAll(fedoraResult);
		return archResult;
	}

	/**
	 * Search for the specified package in Arch repositories
	 * @param packet the package to search
	 * @return The list of packages that match the name
	 * @author Capelier-Marla
	 */
	public static List<SearchedPackage> searchForArch(String packet) {
		// init distribution  to search in it
		Distribution arch = new ArchDistribution();
		// search for the package in the distribution
		Future<List<SearchedPackage>> packages = arch.searchPackage(packet);
		// init the list of packages that will be returned
		List<SearchedPackage> result = new ArrayList<>();
		// try to get the searched packages to return it after
		try {
			result = packages.get();
		} catch (Exception e) {
			/* TODO: handle exception */
		}
		return result;
	}

	/**
	 * Search for the specified package in Fedora repositories
	 * @param packet the package to search
	 * @return The list of packages that match the name
	 * @author Capelier-Marla
	 */
	public static List<SearchedPackage> searchForFedora(String packet) {
		// init distribution  to search in it
		Distribution fedora = new FedoraDistribution();
		// search for the package in the distribution
		Future<List<SearchedPackage>> packages = fedora.searchPackage(packet);
		// init the list of packages that will be returned
		List<SearchedPackage> result = new ArrayList<>();
		// try to get the searched packages to return it after
		try {
			result = packages.get();
		} catch (Exception e) {
			/* TODO: handle exception */
		}
		return result;
	}

	/**
	 * Search for the specific package in the specific distribution, we need the searchedPackage to get informations about it like it name and distribution
	 * @param pSearchedPackage the package to search for
	 * @return a complete package with its dependencies
	 * @author Capelier-Marla
	 */
	public static Package getPackage(SearchedPackage pSearchedPackage) {
		// we get the name of the distribution of the package
		String distName = pSearchedPackage.getDistribution();
		// we create a distribution object
		Distribution distribution;
		switch (distName) {
			case "archlinux":
				distribution = new ArchDistribution();
				break;
			case "fedora":
				distribution = new FedoraDistribution();
				break;
			default:
				System.out.println("Error: Unknown");
				System.exit(1);
				return null;
		}
		// create the futue package we'll get from searching
		Future<Package> packet = distribution.getPackageTree(pSearchedPackage.getName(), 4);
		// object containing the package we're looking for
		Package result;
		try {
			result = packet.get();
		} catch (Exception e) {
			return null;
		}
		// return the package
		return result;
	}
}
