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
         * Handle 404 errors lol
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

    public JSONObject parseSpecFile(String spec){
        JSONObject json = new JSONObject();

        // resolve macros
        int baseindex = spec.indexOf("%define");
        while(baseindex != -1){
            baseindex += 8;
            while(spec.charAt(baseindex) == ' ')baseindex++;
            String macroName = spec.substring(baseindex, spec.indexOf(" ", baseindex));
            String macroValue = spec.substring(spec.indexOf(" ", baseindex),spec.indexOf("\n", baseindex)).trim();
            spec = spec.replaceAll("%\\{"+ macroName +"\\}", macroValue);
            baseindex = spec.indexOf("%define",baseindex);
        }
        
        // parse version
        int index = spec.indexOf("Version:")+8;
        String version = spec.substring(index, spec.indexOf("\n",index)).trim();

        // parse description
        index = spec.indexOf("%description")+13;
        String description = spec.substring(index,spec.indexOf("%",index));
        
        // parse dependencies
        baseindex = spec.indexOf("\nRequires:");
        JSONArray depedencies = new JSONArray();
        while(baseindex != -1){
            baseindex += 10;
            while(spec.charAt(baseindex) == ' ')baseindex++;
            String dep = spec.substring(baseindex,spec.indexOf("\n", baseindex));
            if(dep.contains(" ")) dep = dep.substring(0, dep.indexOf(" "));
            depedencies.put(dep);
            baseindex = spec.indexOf("\nRequires:",baseindex);
        }

        json.put("depedencies", depedencies);
        json.put("description", description);
        json.put("version",version);
        System.out.println(json);
        return json;
    }

    public Package getPackageTree(String packageName, int depth) {
        String name, version, repo, description;
        List<Package> deps = new ArrayList<>();

        // parse the json
        String spec = getPackageFromAPI(packageName);
        return new Package("name", "version", "repo", "description", deps);
    }

}