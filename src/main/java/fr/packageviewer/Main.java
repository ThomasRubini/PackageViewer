package fr.packageviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		// else we get the list of packages from the distribution
		if(distribution == null) {
			packets = Searcher.searchForAll(packet);
		} else {
			switch (distribution) {
				case "archlinux":
					packets = Searcher.searchForArch(packet);
					break;
				case "fedora":
					packets = Searcher.searchForFedora(packet);
					break;
				default:
					System.out.println("Error: Unknown");
					System.exit(0);
					break;
			}
		}
	}
}
