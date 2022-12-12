package fr.packageviewer;

public class Main {

	public static void main(String[] args) {
		// send the command line arguments to the parser
		ArgParse.parseArguments(args);
		System.out.println(ArgParse.getDistribution());
		System.out.println(ArgParse.getPacket());
	}
}
