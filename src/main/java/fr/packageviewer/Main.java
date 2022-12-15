package fr.packageviewer;

import fr.packageviewer.frontend.Frontend;
import fr.packageviewer.frontend.FrontendFactory;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

import java.util.List;

public class Main {
	public static void main(String[] args) {

		// create the frontend : what the user will see in its terminal
		Frontend frontend = FrontendFactory.get("terminal");

		// send the command line arguments to the parser
		ArgParse.parseArguments(args);
		String packetName = ArgParse.getPacket();
		String distribution = ArgParse.getDistribution();

		// we create an object to search the packages in the distribution
		Searcher searcher = new Searcher(distribution);

		// we get the packages list
		List<SearchedPackage> packets = searcher.searchPackages(packetName);

		// ask the user to select the package to see in details and store its name
		SearchedPackage searchedPacketName = frontend.askUserToChoosePackage(packets);

		// get all informations about the package by searching it in details
		Package packet = searcher.getPackage(searchedPacketName);

		// show all informations about a packet
		frontend.showPackageTree(packet, 0);
	}
}
