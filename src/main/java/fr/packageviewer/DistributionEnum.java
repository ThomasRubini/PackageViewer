package fr.packageviewer;

import fr.packageviewer.distribution.ArchDistribution;
import fr.packageviewer.distribution.Distribution;
import fr.packageviewer.distribution.FedoraDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum containing contructors for each distribution to get them by their name
 * @author Capelier-Marla
 */
public enum DistributionEnum {
    ARCH("arch", new ArchDistribution()),
    FEDORA("fedora", new FedoraDistribution()),
    ;

    private final String name;
    private final Distribution distributionConstructor;

    /**
     * Contructor for enums
     * @param name
     * @param distributionConstructor
     * @author Capelier-Marla
     */
    DistributionEnum(String name, Distribution distributionConstructor) {
        this.name = name;
        this.distributionConstructor = distributionConstructor;
    }

    /**
     * Get the distribution instance for the distribution requested in String
     * @param name name of the distribution requested
     * @return the instance of the distribution requested
     * @author Capelier-Marla
     */
    public static Distribution getDistributionContructorByName(String name) {
        // loop for all ditributions stored in enum
        for(var distrib : values()) {
            // return the instance if it's the same as enum name
            if(distrib.name.equals(name)) {
                return distrib.distributionConstructor;
            }
        }
        return null;
    }

    /**
     * Get all distribution instances available in this enum
     * @return the set of distribution instances
     */
    public static List<Distribution> getAllDistributionsInstances() {
        // create the set that will be returned
        List<Distribution> result = new ArrayList<>();
        // add all the distribution instances in the set
        for(var ditrib : values()) {
            result.add(ditrib.distributionConstructor);
        }
        return result;
    }
}
