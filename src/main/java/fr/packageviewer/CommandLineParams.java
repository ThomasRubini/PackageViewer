package fr.packageviewer;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * Class to store and get the command line arguments given by the user
 * @author Capelier-Marla
 */
public class CommandLineParams {
    /**
     * List of parameters given by the user
     */
    @Parameter
    public List<String> parameters = new ArrayList<>();

    /**
     * Distribution the user want to search packages in
     */
    @Parameter(names = {"--distro", "-d"},
               description = "Linux distribution to search in",
               required = false)
    public String distribution;
}
