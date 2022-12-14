package fr.packageviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.pack.Package;

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

		System.out.println(packet);
		distribution = processDistribution(distribution);
		System.out.println(distribution);
	}
}
