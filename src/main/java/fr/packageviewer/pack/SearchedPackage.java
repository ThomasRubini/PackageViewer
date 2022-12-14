package fr.packageviewer.pack;

/**
 * The SearchedPackage class stores metadata found when searching for a
 * package.
 * 
 * @author C.Marla, R.Thomas, S.Djalim
 * @version 1.0
 */
public class SearchedPackage {
    /**
     * Name of the package
     */
    private final String name;
    /**
     * Version of the package
     */
    private final String version;
    /**
     * Repository where the package is located
     */
    private final String repo;
    /**
     * Description of the package
     */
    private final String description;

    /**
     * Getter for the name attribute
     * 
     * @return String, the name of the package
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the version attribute
     * 
     * @return String, the version of the package
     */
    public String getVersion() {
        return version;
    }

    /**
     * Getter for the repo attribute
     * 
     * @return String, repository where the package is located
     */
    public String getRepo() {
        return repo;
    }

    /**
     * Getter for the description attribute
     * 
     * @return String, Description of the package
     */
    public String getDescription() {
        return description;
    }

    /**
     * Constructor for the SearchedPackage class
     * 
     * @param name        String, name of the package
     * @param version     String, version of the package
     * @param repo        String, repository where the package is located
     * @param description String, description of the package
     * 
     */
    public SearchedPackage(String name, String version, String repo, String desciption) {
        this.name = name;
        this.version = version;
        this.repo = repo;
        this.description = desciption;
    }

    /**
     * Returns a string reprensentation of the package
     * 
     * @return String, string reprensentation of the package
     */
    @Override
    public String toString() {
        return "SearchedPackage{name=%s,version=%s,repo=%s,description=%s}".formatted(name, version, repo, description);
    }
}
