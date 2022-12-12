package fr.packageviewer;

import com.beust.jcommander.JCommander;


/**
 * Class to parse the command line arguments given by the user
 * @author Capelier-Marla
 */
public class ArgParse {

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
        try {
            // parse the argument from list of String
            jCommander.parse(args);
            // store the argument parsed in the variable
            distribution = params.distribution;
        } catch (Exception e) {
            // if the parsing failed, print the error message and exit the program
            System.out.println("You forgot the distribution name.");
            System.exit(0);
        }
	}

    /**
     * Get the distribution name. If the user didn't give any or we didn't parsed, return null
     * @author Capelier-Marla
     * @return String: the distribution name
     */
    public static String getDistribution() {
        return distribution;
    }
}
