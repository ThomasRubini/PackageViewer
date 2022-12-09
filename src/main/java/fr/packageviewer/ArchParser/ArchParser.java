package fr.packageviewer.ArchParser;

import java.util.List;

public class ArchParser {
    /**
     * Will parse the given json and store data in a Package
     * @param json the given json string
     * @param depth depth to search dependencies
     * @return new Package
     */
    
    public static Package getPackageTree(String json, int depth){
        String name, version, repo, description;
        List<Package> deps;
    }
}
