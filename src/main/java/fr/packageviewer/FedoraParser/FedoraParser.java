package fr.packageviewer.FedoraParser;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.http.*;
import org.json.*;

public class FedoraParser {

    public List<SearchedPackage> searchPackage(String packageName){
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://src.fedoraproject.org/api/0/projects?namepace=rpms&per_page=100&short=true&pattern="+packageName))
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
        for (Object searchResultObj : json.getJSONArray("projects")) {
            // convert object into String
            JSONObject searchResultJson = (JSONObject) searchResultObj;
            // add package into to list
            searchedPackagesList.add(new SearchedPackage(
                searchResultJson.getString("neofetch"),
                searchResultJson.getString("fullname"),
                searchResultJson.getString("description")
            ));
        }
        return searchedPackagesList;
    }
}