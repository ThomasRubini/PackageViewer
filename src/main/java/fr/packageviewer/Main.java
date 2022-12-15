package fr.packageviewer;

import java.util.List;

import fr.packageviewer.frontend.Frontend;
import fr.packageviewer.frontend.FrontendFactory;
import fr.packageviewer.pack.SearchedPackage;

public class Main {
	public static void main(String[] args) {

		// create the frontend : what the user will see in its terminal
		Frontend frontend = FrontendFactory.get("terminal");

		// send the command line arguments to the parser
		ArgParse.parseArguments(args);
		String packet = ArgParse.getPacket();
		String distribution = ArgParse.getDistribution();

		// we create an object to search the packages in the distribution
		Searcher searcher = new Searcher(distribution);

		// we get the packages list
		List<SearchedPackage> packets = searcher.searchPackages(packet);

		// ask the user to select the package to see in details
		SearchedPackage searchedPacket = frontend.askUserToChoosePackage(packets);


	}
}
