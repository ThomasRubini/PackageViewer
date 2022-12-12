package fr.packageviewer.distribution;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import fr.packageviewer.LoggerManager;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import org.json.*;

public class ArchDistribution implements Distribution {

    private static final Logger logger = LoggerManager.getLogger("ArchParser");

/**
 * Will return the String json of the package from the Arch Linux API
 * @param packageName the package name to get the json from
 * @return json of the package
  */
    public CompletableFuture<HttpResponse<String>> getPackageFromAPI(String packageName) {
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://archlinux.org/packages/search/json/?name="+packageName)).build();
        // send its url and return the string given
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }


/**
 * Search for a package and return a list of packages
 * @param packageName the package name to search
 * @return
 */
    public List<SearchedPackage> searchPackage(String packageName){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://archlinux.org/packages/search/json/?q="+packageName))
                .build();

        HttpResponse<String> response;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch(IOException|InterruptedException e){
            e.printStackTrace();
            return null;
        }

        JSONObject json = new JSONObject(response.body());

        List<SearchedPackage> searchedPackagesList = new ArrayList<>();

        // iterate for every package in the list
        for (Object searchResultObj : json.getJSONArray("results")) {
            // convert object into String
            JSONObject searchResultJson = (JSONObject) searchResultObj;
            // add package into to list
            searchedPackagesList.add(new SearchedPackage(
                searchResultJson.getString("pkgname"),
                searchResultJson.getString("pkgver"),
                searchResultJson.getString("repo"),
                searchResultJson.getString("pkgdesc")
            ));
        }

        return searchedPackagesList;

    }


    /**
     * Will generate a dependencies tree of depth 'depth' given the package name
     * @param packageName the package name to search
     * @param depth depth to search dependencies
     * @return new Package
     */
    public CompletableFuture<Package> getPackageTree(String packageName, int depth) {

        // parse the json
        var futurePackage = new CompletableFuture<Package>();

        logger.fine("Querying package %s from API... (depth=%s)".formatted(packageName, depth));

        CompletableFuture<HttpResponse<String>> futureRequest;
        try{
            futureRequest = getPackageFromAPI(packageName);
        }catch(IllegalArgumentException e){
            logger.warning("Caught exception for package %s :\n%s".formatted(packageName, e));
            return CompletableFuture.completedFuture(null);
        }
        futureRequest.thenAccept(result->{
            List<Package> deps = new ArrayList<>();
            String name, version, repo, description;

            JSONObject json = new JSONObject(result.body());

            JSONArray resultsArrayJson = json.getJSONArray("results");
            if(resultsArrayJson.length()==0){
                // unknown package, probably an abstract dependency
                logger.fine("Completing callback INVALID for package %s (depth=%s)".formatted(packageName, depth));
                futurePackage.complete(null);
            }
            JSONObject resultJson = json.getJSONArray("results").getJSONObject(0);

            // get infos except dependencies
            name = resultJson.getString("pkgname");
            version = resultJson.getString("pkgver");
            repo = resultJson.getString("repo");
            description = resultJson.getString("pkgdesc");

            // if we're at the maximum depth, return the package without its dependencies
            if(depth==0){
                logger.fine("Completing callback NODEP for package %s (depth=%s)".formatted(packageName, depth));
                futurePackage.complete(new Package(name, version, repo, description, Collections.emptyList()));
            } else {
                // iterate for every package in the list
                List<CompletableFuture<Package>> futureDeps = new ArrayList<>();
                for (Object depPackageNameObj : resultJson.getJSONArray("depends")) {
                    // convert object into String
                    String depPackageName = (String) depPackageNameObj;
                    // add package into Package List
                    futureDeps.add(getPackageTree(depPackageName, depth - 1));
                }
                for(CompletableFuture<Package> future : futureDeps){
                    try {
                        deps.add(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }

                // TODO this doesn't seem clean
                logger.fine("Completing callback DEPS for package %s (depth=%s)".formatted(packageName, depth));
                futurePackage.complete(new Package(name, version, repo, description, deps));
            }
        }).exceptionally((e2->{
            logger.warning("Error while fetching package %s (depth=%s) from the API : \n%s".formatted(packageName, depth, e2));
            e2.printStackTrace();
            futurePackage.complete(null);
            return null;
        }));

        return futurePackage;
    }
}
