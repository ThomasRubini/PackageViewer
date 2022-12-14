package fr.packageviewer.distribution;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import java.net.http.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import fr.packageviewer.LoggerManager;
import fr.packageviewer.Pair;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.parser.AsyncRequestsParser;
import org.json.*;

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

    private static String trimAfterCharacters(String str, String trimAfterCharacters){
        for(char c : trimAfterCharacters.toCharArray()){
            int index = str.indexOf(c);
            if(index>0)str = str.substring(index);
        }
        return str;
    }

    /**
     * This function return a package from arch package api in the form of a Pair
     * Composed of a Package object, and a set of string containing the names of
     * the dependecies of the package.
     * 
     * @param packageName String, The package's exact name
     * @return Pair of Package and Set of String
     */
    @Override
    public CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName) {
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        HttpRequest request = HttpRequest
                .newBuilder(URI.create("https://archlinux.org/packages/search/json/?name=" + packageName)).build();

        CompletableFuture<Pair<Package, Set<String>>> futureResult = new CompletableFuture<>();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result -> {

            JSONObject json = new JSONObject(result.body());

            JSONArray resultsArrayJson = json.getJSONArray("results");
            if (resultsArrayJson.length() == 0) {
                // unknown package, probably an abstract dependency
                futureResult.complete(null);
                return;
            }
            JSONObject resultJson = resultsArrayJson.getJSONObject(0);

            // get infos

            Set<String> dependenciesNames = new HashSet<>();
            for(Object dependency : resultJson.getJSONArray("depends")){
                dependenciesNames.add(trimAfterCharacters((String)dependency, "<>="));
            }
            futureResult.complete(new Pair<>(
                    new Package(
                            resultJson.getString("pkgname"),
                            resultJson.getString("pkgver"),
                            resultJson.getString("repo"),
                            resultJson.getString("pkgdesc"),
                            "arch"
                    ),
                    dependenciesNames
            ));
        }).exceptionally(error ->{
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
                    "arch"
                ));
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
