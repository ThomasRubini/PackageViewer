package fr.packageviewer;

import com.beust.jcommander.JCommander;


/**
 * Class to parse the command line arguments given by the user
 * @author Capelier-Marla
 */
public class ArgParse {

    /* distribution the user want */
	private static String distribution;
    private static String packet;

	/**
	 * Get the command line argument given by the user, parse it with the parser and store it in the corresponding variable
	 * @author Capelier-Marla
	 * @param args the command line arguments given by the user
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
            if(params.help) {
                jCommander.setProgramName("PackageViewer");
                jCommander.usage();
                System.exit(0);
            } else {
                // store the argument parsed in the variable
                packet = params.packet;
                distribution = params.distribution;
            }

        } catch (Exception e) {
            // if the parsing failed, print the error message and exit the program
            System.out.println("You forgot something, please enter the package name and the distribution name if you want to search in a specific one");
            jCommander.setProgramName("PackageViewer");
            jCommander.usage();
            System.exit(0);
        }
	}

    /**
     * Get the distribution name. If the user didn't give any or if we didn't parse it, return null
     * @author Capelier-Marla
     * @return String: the distribution name
     */
    public static String getDistribution() {
        return distribution;
    }

    /**
     * Get the packet name, this one isn't optional
     * @author Capelier-Marla
     * @return String: the packet name
     */
     public static String getPacket() {
         return packet;
     }
}
