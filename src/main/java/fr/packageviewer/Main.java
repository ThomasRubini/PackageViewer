package fr.packageviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

		// list all packages in reverse order
		for (int i = packets.size(); i-- > 0; ) {
			SearchedPackage searchedPacket = packets.get(i);
			System.out.printf("%s - %s/%s/%s %s%n\t%s%n",
							  i,
							  searchedPacket.getDistribution(),
							  searchedPacket.getRepo(),
							  searchedPacket.getName(),
							  searchedPacket.getVersion()==null?"":searchedPacket.getVersion(),
							  searchedPacket.getDescription());
		}

		System.out.printf("Pick a package to see in details (0-%s) : %n", packets.size()-1);
		Scanner input = new Scanner(System.in);

		// we create vars for the loop
		String packetNumberString;
		int packetNumber;
		boolean notValid;
		// we ask input and check if the user input is correct
		do {
			packetNumberString = input.nextLine();
			// reset notValid to false, we set it in true only if something is wrong
			notValid = false;
			if(isNumeric(packetNumberString)) {
				packetNumber = Integer.parseInt(packetNumberString);
				if(packetNumber < 0 || packetNumber >= packets.size()) {
					// this number is too big or too small
					System.out.println("Enter a valid number");
					notValid = true;
				}
			} else {
				// this is not a number
				System.out.println("Enter a valid number");
				notValid = true;
			}
		} while(notValid);

		input.close();


	}


	/**
	 * Check if the String given is a number
	 * @param i the String given
	 * @return true if the String is a number
	 * @author Capelier-Marla
	 */
	 private static boolean isNumeric(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
