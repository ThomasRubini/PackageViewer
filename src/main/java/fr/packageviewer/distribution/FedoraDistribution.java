package fr.packageviewer.distribution;

import java.net.URI;
import java.util.*;
import java.net.http.*;

import fr.packageviewer.Pair;
import fr.packageviewer.parser.AsyncRequestsParser;
import org.json.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.LoggerManager;

/**
 * This class handles package requests for Fedora. All return objects in
 * this class are wrapped by a CompletableFuture to ensure async workload.
 * 
 * @author S.Djalim, R.Thomas
 * @version 1.0
 */
public class FedoraDistribution extends AsyncRequestsParser implements Distribution {

    /**
     * Logger object used to split debug output and the application output
     */
    private static final Logger logger = LoggerManager.getLogger("FedoraDistribution");

    /**
     * This function return a package from Fedora metadata api in the form of a
     * Pair Composed of a Package object, and a set of string containing the
     * names of the dependecies of the package.
     * 
     * @param packageName String, The package's exact name
     * @return Pair of Package and Set of String
     */
    protected CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName) {
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        String url = "https://mdapi.fedoraproject.org/rawhide/pkg/" + packageName + "";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();

        CompletableFuture<Pair<Package, Set<String>>> futureResult = new CompletableFuture<>();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result -> {

            String body = result.body();

            if (body.contains("404: Not Found")) {
                futureResult.complete(null);
                return;
            }

            JSONObject json = new JSONObject(result.body());

            // get infos
            Set<String> dependenciesNames = new HashSet<>();

            for (Object depPackageObj : json.getJSONArray("requires")) {
                // convert object into String
                JSONObject depPackageJson = (JSONObject) depPackageObj;
                // add package into Package List
                String depName = depPackageJson.getString("name");
                if (depName.contains(".so"))
                    continue;
                if (depName.contains("/"))
                    continue;
                dependenciesNames.add(depName);
            }

            futureResult.complete(new Pair<>(
                    new Package(
                            json.getString("basename"),
                            json.getString("version"),
                            json.getString("repo"),
                            json.getString("description"),
                            "fedora"
                    ),
                    dependenciesNames
            ));
        }).exceptionally(error->{
            error.printStackTrace();
            logger.warning("Error while fetching package %s from the API : \n%s".formatted(packageName, error));
            futureResult.complete(null);
            return null;
        });
        // if there's an error, return an empty string
        return futureResult;
    }

    /**
     * Search for a package matching a pattern and return a list of packages and
     * return a list of string matching this pattern.
     * 
     * @param packageName String, the pattern to search in the repositories
     * @return List of SearchedPackage objects
     */
    @Override
    public CompletableFuture<List<SearchedPackage>> searchPackage(String packageName) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://src.fedoraproject.org/api/0/projects?namepace=rpms&per_page=100&short=true&pattern=*"
                                + packageName + "*"))
                .build();

        CompletableFuture<List<SearchedPackage>> futureSearchedPackages = new CompletableFuture<>();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result -> {
            JSONObject json = new JSONObject(result.body());

            List<SearchedPackage> searchedPackagesList = new ArrayList<>();

            // iterate for every package in the list
            for (Object searchResultObj : json.getJSONArray("projects")) {
                // convert object into String
                JSONObject searchResultJson = (JSONObject) searchResultObj;
                // add package into to list
                searchedPackagesList.add(new SearchedPackage(
                        searchResultJson.getString("name"),
                        null,
                        null,
                        searchResultJson.getString("description"),
                        "fedora"
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
