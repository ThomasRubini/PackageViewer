package fr.packageviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.distribution.FedoraDistribution;
import fr.packageviewer.pack.SearchedPackage;

public class Searcher {
    public static List<SearchedPackage> searchForAll(String packet) {
		// init distribution  to search in it
		Distribution arch = new ArchDistribution();
		Distribution fedora = new ArchDistribution();
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
}
