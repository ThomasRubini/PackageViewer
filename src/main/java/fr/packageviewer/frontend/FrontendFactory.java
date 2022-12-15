package fr.packageviewer.frontend;

public class FrontendFactory {

	public static Frontend get(String name) {
		if (name.equals("terminal")) {
			return new FrontendTerminal();
		}
		throw new IllegalArgumentException("Invalid frontend");
	}

}
