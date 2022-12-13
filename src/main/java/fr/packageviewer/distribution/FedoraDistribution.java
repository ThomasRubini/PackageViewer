package fr.packageviewer.distribution;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.net.http.*;
import org.json.*;
import java.util.concurrent.CompletableFuture;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.LoggerManager;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

public class FedoraDistribution implements Distribution {

    private String getPackageFromAPI(String packageName) {
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        String url = "https://mdapi.fedoraproject.org/rawhide/pkg/"+packageName+"";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
        // send its url and return the string given
        try {
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            if(response.contains("404: Not Found")) return "";
            return response;
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        
        }
        // if there's an error, return an empty string
        return "";
    }
    
    @Override
    public List<SearchedPackage> searchPackage(String packageName) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://src.fedoraproject.org/api/0/projects?namepace=rpms&per_page=100&short=true&pattern=*"
                                + packageName + "*"))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject json = new JSONObject(response.body());

        List<SearchedPackage> searchedPackagesList = new ArrayList<>();

        // iterate for every package in the list
        for (Object searchResultObj : json.getJSONArray("projects")) {
            // convert object into String
            JSONObject searchResultJson = (JSONObject) searchResultObj;
            // add package into to list
            searchedPackagesList.add(new SearchedPackage(
                    searchResultJson.getString("neofetch"),
                    null,
                    searchResultJson.getString("fullname"),
                    searchResultJson.getString("description")));
        }
        return searchedPackagesList;
    }

    public Package getPackageTreeInternal(String packageName, int depth) {
        String name, version, repo, description;
        List<Package> deps = new ArrayList<>();

        // parse the json
        String response = getPackageFromAPI(packageName);
        if (response == "") {
            return new Package(packageName + "(not found)", "N/A", "N/A", "N/A", Collections.emptyList());
        }
        JSONObject json = new JSONObject(response);
        // get infos except dependencies
        name = json.getString("basename");
        version = json.getString("version");
        repo = "rpms/" + packageName;
        description = json.getString("description");

        // if we're at the maximum depth, return the package without its dependencies
        if (depth == 0) {
            return new Package(name, version, repo, description, Collections.emptyList());
        } else {
            // iterate for every package in the list
            for (Object depPackageNameObj : json.getJSONArray("requires")) {
                // convert object into String
                JSONObject depPackageJSONObj = (JSONObject) depPackageNameObj;
                // add package into Package List
                String depName = depPackageJSONObj.getString("name");
                if (depName.contains(".so"))
                    continue;
                if (depName.contains("/"))
                    continue;
                deps.add(getPackageTree(depName, depth - 1));
            }
            return new Package(name, version, repo, description, deps);
        }
    }

    public CompletableFuture<Package> getPackageTree(String packageName, int depth){
        return  CompletableFuture.supplyAsync(()->{
            return getPackageTreeInternal(packageName, depth);
        });
    }
}
