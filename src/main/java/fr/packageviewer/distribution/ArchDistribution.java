package fr.packageviewer.distribution;

import fr.packageviewer.LoggerManager;
import fr.packageviewer.Pair;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.parser.AsyncRequestsParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * This class handles package requests for Arch linux. All return objects in
 * this class are wrapped by a CompletableFuture to ensure async workload.
 * 
 * @author C.Marla, R.Thomas
 * @version 1.0
 */
public class ArchDistribution extends AsyncRequestsParser implements Distribution {

    /**
     * Logger object used to split debug output and the application output
     */
    private static final Logger logger = LoggerManager.getLogger("ArchDistribution");

    /**
     * This method remove all characters in the first string passed as
     * parameter after one of the character in the second string if found
     * in the first string
     *
     * @param str                 String, the string to trim
     * @param trimAfterCharacters String, the character that delimits our string
     * @return the string after being trimmed
     */
    private static String trimAfterCharacters(String str, String trimAfterCharacters) {
        for (char c : trimAfterCharacters.toCharArray()) {
            int index = str.indexOf(c);
            if (index > 0)
                str = str.substring(index);
        }
        return str;
    }

    /**
     * This function return a package from arch package api in the form of a Pair
     * Composed of a Package object, and a set of string containing the names of
     * the dependencies of the package.
     * 
     * @param packageName String, The package's exact name
     * @return Pair of Package and Set of String
     */
    @Override
    public CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName) {
        // create a new http client and make a request to the arch research api
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create("https://archlinux.org/packages/search/json/?name=" + packageName)).build();

        CompletableFuture<Pair<Package, Set<String>>> futureResult = new CompletableFuture<>();
        // send the request and when there's a response
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result -> {
            //parse the json response
            JSONObject json = new JSONObject(result.body());
            
            // check if the response contains something
            JSONArray resultsArrayJson = json.getJSONArray("results");
            if (resultsArrayJson.length() == 0) {
                // unknown package, probably an abstract dependency
                futureResult.complete(null);
                return;
            }
            JSONObject resultJson = resultsArrayJson.getJSONObject(0);
            Set<String> dependenciesNames = new HashSet<>();
            // parse dependencies without version requirements (bash >= 3.0) -> (bash)
            for (Object dependency : resultJson.getJSONArray("depends")) {
                dependenciesNames.add(trimAfterCharacters((String) dependency, "<>="));
            }
            //Create the package and store it and its set of deps in a pair
            futureResult.complete(new Pair<>(
                    new Package(
                            resultJson.getString("pkgname"),
                            resultJson.getString("pkgver"),
                            resultJson.getString("repo"),
                            resultJson.getString("pkgdesc"),
                            "arch"),
                    dependenciesNames));
        }).exceptionally(error -> {
            error.printStackTrace();
            logger.warning("Error while fetching package %s from the API : \n%s".formatted(packageName, error));
            futureResult.complete(null);
            return null;
        });

        return futureResult;

    }

    /**
     * Search for a package matching a pattern and return a list of packages and
     * return a list of string matching this pattern.
     * 
     * @param packageName String, the pattern to search in the repositories
     * @return List of SearchedPackage objects
     */
    public CompletableFuture<List<SearchedPackage>> searchPackage(String packageName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://archlinux.org/packages/search/json/?q=" + packageName))
                .build();

        CompletableFuture<List<SearchedPackage>> futureSearchedPackages = new CompletableFuture<>();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result -> {

            List<SearchedPackage> searchedPackagesList = new ArrayList<>();
            JSONObject json = new JSONObject(result.body());

            // iterate for every package in the list
            for (Object searchResultObj : json.getJSONArray("results")) {
                // convert object into String
                JSONObject searchResultJson = (JSONObject) searchResultObj;
                // add package into to list
                searchedPackagesList.add(new SearchedPackage(
                        searchResultJson.getString("pkgname"),
                        searchResultJson.getString("pkgver"),
                        searchResultJson.getString("repo"),
                        searchResultJson.getString("pkgdesc"),
                        "arch"));
            }
            futureSearchedPackages.complete(searchedPackagesList);
        }).exceptionally(error -> {
            error.printStackTrace();
            futureSearchedPackages.complete(Collections.emptyList());
            return null;
        });

        return futureSearchedPackages;

    }
}
