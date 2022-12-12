package fr.packageviewer;

import com.beust.jcommander.JCommander;

public class Main {

	/* distribution the user want */
	private static String distribution;

	/**
	 * Get the command line argument given by the user, parse it with the parser and store it in the corresponding variable
	 * @author Capelier-Marla
	 * @param args the command line arguments given by the user
	 * @return void
	 */
	static void parseArguments(String[] args) {
		// create JCommander and CommandLineParams objects
		JCommander jCommander = new JCommander();
		CommandLineParams params = new CommandLineParams();
		// add argument required by the params to the JCommander object
		jCommander.addObject(params);
		// parse the argument from list of String
		jCommander.parse(args);
		// store the argument parsed in the variable
		distribution = params.distribution;
	}

	public static void main(String[] args) {
		// send the command line arguments to the parser
		parseArguments(args);
		System.out.println(distribution);
	}
}
