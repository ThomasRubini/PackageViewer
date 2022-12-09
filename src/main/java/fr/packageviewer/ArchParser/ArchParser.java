package fr.packageviewer.ArchParser;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.http.*;
import org.json.*;

public class ArchParser {

/**
 * Will return the String json of the package from the Arch Linux API
 * @param packageName the package name to get the json from
 * @return json of the package
  */
    public String getPackageFromAPI(String packageName){
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://archlinux.org/packages/search/json/?name="+packageName)).build();
        // send its url and return the string given
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
        // if there's an error, return an empty string
        return "";
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
    public Package getPackageTree(String packageName, int depth){
        String name, version, repo, description;
        List<Package> deps = new ArrayList<>();

        // parse the json
        JSONObject json = new JSONObject(getPackageFromAPI(packageName));

        JSONArray resultsArrayJson = json.getJSONArray("results");
        if(resultsArrayJson.length()==0){
            // unknown package, probably an abstract dependency
            return null;
        }
        JSONObject resultJson = json.getJSONArray("results").getJSONObject(0);

        // get infos except dependencies
        name = resultJson.getString("pkgname");
        version = resultJson.getString("pkgver");
        repo = resultJson.getString("repo");
        description = resultJson.getString("pkgdesc");

        // if we're at the maximum depth, return the package without its dependencies
        if(depth==0){
            return new Package(name, version, repo, description, Collections.emptyList());
        } else {
            // iterate for every package in the list
            for (Object depPackageNameObj : resultJson.getJSONArray("depends")) {
                // convert object into String
                String depPackageName = (String) depPackageNameObj;
                // add package into Package List
                deps.add(getPackageTree(depPackageName, depth - 1));
            }

            // TODO this doesn't seem clean
            return new Package(name, version, repo, description, deps);
        }
    }
}
