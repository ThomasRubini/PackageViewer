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

public class ArchDistribution extends AsyncRequestsParser implements Distribution {

    private static final Logger logger = LoggerManager.getLogger("ArchDistribution");

/**
 * Will return the String json of the package from the Arch Linux API
 * @param packageName the package name to get the json from
 * @return json of the package
  */

@Override
public CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName) {
    // create a new http client
    HttpClient client = HttpClient.newHttpClient();
    // and create its url
    HttpRequest request = HttpRequest.newBuilder(URI.create("https://archlinux.org/packages/search/json/?name="+packageName)).build();

    CompletableFuture<Pair<Package, Set<String>>> futureResult = new CompletableFuture<>();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result ->{

        JSONObject json = new JSONObject(result.body());

        JSONArray resultsArrayJson = json.getJSONArray("results");
        if(resultsArrayJson.length()==0){
            // unknown package, probably an abstract dependency
            futureResult.complete(null);
            return;
        }
        JSONObject resultJson = resultsArrayJson.getJSONObject(0);

        // get infos

        Set<String> dependenciesNames = new HashSet<>();
        for(Object dependency : resultJson.getJSONArray("depends")){
            dependenciesNames.add((String)dependency);
        }
        futureResult.complete(new Pair<>(
                new Package(
                        resultJson.getString("pkgname"),
                        resultJson.getString("pkgver"),
                        resultJson.getString("repo"),
                        resultJson.getString("pkgdesc")
                ),
                dependenciesNames
        ));
    });

    return futureResult;

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
}
