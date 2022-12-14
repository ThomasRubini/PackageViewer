package fr.packageviewer.pack;

import java.util.ArrayList;
import java.util.List;

/**
 * The Package class stores all metadata needed for a fully completed package.
 *
 * @author C.Marla, R.Thomas, S.Djalim
 * @version 1.0
 */
public class Package extends SearchedPackage {
	/**
	 * List of package storing all the dependencies of the package
	 */
	private final List<Package> deps;

	/**
	 * Getter for the deps attribute
	 *
	 * @return List, List of package storing all the dependencies of the package
	 */
	public List<Package> getDeps() {
		return deps;
	}

	/**
	 * This method adds to the dependency list the package passed as parameter.
	 *
	 * @param pack Package, the package to add as dependency
	 */
	public void addDep(Package pack) {
		deps.add(pack);
	}

	/**
	 * Second constructor for the Package class, allows to create a package
	 * without supplying a list of dependencies.
	 *
	 * @param name         String, name of the package
	 * @param version      String, version of the package
	 * @param repo         String, repository where the package is located
	 * @param description  String, description of the package
	 * @param distribution String, the distribution where this specific package
	 *                     belongs
	 */
	public Package(String name, String version, String repo, String description, String distribution) {
		this(name, version, repo, description, distribution, new ArrayList<>());
	}

	/**
	 * Main constructor for the Package class
	 *
	 * @param name         String, name of the package
	 * @param version      String, version of the package
	 * @param repo         String, repository where the package is located
	 * @param description  String, description of the package
	 * @param distribution String, the distribution where this specific package
	 *                     belongs
	 * @param deps         List of Package, dependencies of the package
	 */
	public Package(String name, String version, String repo, String description, String distribution,
				   List<Package> deps) {
		super(name, version, repo, description, distribution);
		this.deps = deps;
	}

	/**
	 * Returns a string representation of the package
	 *
	 * @return String, string representation of the package
	 */
	@Override
	public String toString() {
		return "Package{%s,deps=%s}".formatted(super.toString(), deps);
	}
}
