package fr.packageviewer.FedoraParser;

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

public class FedoraParser {

    public String getPackageFromAPI(String packageName) {
        /* TODO
         *
         * Ok, to retrieve all the data we need about a package we get the 
         * .spec file at the repo's root. The thing is that for pacakges
         * that didn't had an update for a long time, the branch "main" doesn't
         * exist, it's "master", so in the future we'll need to address this 
         * case
         * 
         */
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://src.fedoraproject.org/rpms/"+packageName+"/raw/main/f/"+packageName+".spec")).build();
        // send its url and return the string given
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
        // if there's an error, return an empty string
        return "";
    }

    public List<SearchedPackage> searchPackage(String packageName) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://src.fedoraproject.org/api/0/projects?namepace=rpms&per_page=100&short=true&pattern="
                                + packageName))
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
                    searchResultJson.getString("fullname"),
                    searchResultJson.getString("description")));
        }
        return searchedPackagesList;
    }

    public Map<String,String> parseSpecFile(String spec){
        Map<String,String> results = new HashMap<>();

        // parse description
        String descriptionStart = spec.substring(spec.indexOf("%description")+13);
        String description = descriptionStart.substring(0,descriptionStart.indexOf("%"));
        
        // parse dependencies
        int baseindex = spec.indexOf("\nRequires:");
        while(baseindex != -1){
            baseindex += 10;
            while(spec.charAt(baseindex) == ' ')baseindex++;
            String dep = spec.substring(baseindex,spec.indexOf("\n", baseindex));
            if(dep.contains(" ")) dep = dep.substring(0, dep.indexOf(" "));
            System.out.println(dep);
            baseindex = spec.indexOf("\nRequires:",baseindex);
        }
        return results;
    }

    public Package getPackageTree(String packageName, int depth) {
        String name, version, repo, description;
        List<Package> deps = new ArrayList<>();

        // parse the json
        String spec = getPackageFromAPI(packageName);
        return new Package("name", "version", "repo", "description", deps);
    }

}