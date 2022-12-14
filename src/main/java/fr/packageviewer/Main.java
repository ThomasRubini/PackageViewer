package fr.packageviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.pack.SearchedPackage;

public class Main {

	/**
	 * Check if the given distribution is supported
	 * @param distribution distribution name
	 * @return the distribution wanted if found, or null if there is none, or stop the program if it is not supported
	 * @author Capelier-Marla
	 */
	private static String processDistribution(String distribution) {
		// check if the user asked a distribution
		if(distribution == null || distribution.length() == 0) {
			return null;
		}
		// create a map with the distribution names
		Map<String, String> distributionMap = new HashMap<String, String>();
		distributionMap.put("ARCH", "archlinux");
		distributionMap.put("ARCHLINUX", "archlinux");
		distributionMap.put("FEDORA", "fedora");
		distribution = distribution.toUpperCase();

		// check if we support the distribtion
		if(distributionMap.containsKey(distribution)) {
			// give the distribution name
			return distributionMap.get(distribution);
		} else {
			// stop the program as the user want a non-supported distribution
			System.out.println("Cette ditribution n'a pas été trouvée");
			System.exit(0);
			return null;
		}
	}


	private static List<SearchedPackage> searchForAll(String packet) {
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
	private static List<SearchedPackage> searchForArch(String packet) {
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
	private static List<SearchedPackage> searchForFedora(String packet) {
		// init distribution  to search in it
		Distribution fedora = new ArchDistribution();
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

	public static void main(String[] args) {
		// send the command line arguments to the parser
		ArgParse.parseArguments(args);
		String packet = ArgParse.getPacket();
		String distribution = ArgParse.getDistribution();
		distribution = processDistribution(distribution);

		System.out.println(packet);
		System.out.println(distribution);

		// the list of packages we will show later
		List<SearchedPackage> packets = new ArrayList<>();

		// distribution is null when no distribution is specified
		if(distribution == null) {
			packets = searchForAll(packet);
		} else {
			switch (distribution) {
				case "archlinux":
					packets = searchForArch(packet);
					break;
				case "fedora":
					packets = searchForFedora(packet);
					break;
				default:
					System.out.println("Error: Unknown");
					System.exit(0);
					break;
			}
		}
	}
}
