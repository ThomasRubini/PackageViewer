package fr.packageviewer;

import com.beust.jcommander.Parameter;

/**
 * Class to store and get the command line arguments given by the user
 * @author Capelier-Marla
 */
public class CommandLineParams {

    /**
     * Packet the user want to search, only parameter without names
     */
    @Parameter(description = "Packet to search",
                required = true)
    public String packet;

    /**
     * Distribution the user want to search packages in
     */
    @Parameter(names = {"--distro", "-d"},
               description = "Linux distribution to search in",
               required = false)
    public String distribution;

    /**
     * Displays the help
     */
    @Parameter(names = {"--help", "-h"},
               help = true,
               required = false)
    public boolean help = false;
}
