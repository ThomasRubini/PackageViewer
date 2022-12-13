package fr.packageviewer.distribution;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.net.http.*;

import fr.packageviewer.Pair;
import fr.packageviewer.parser.AsyncRequestsParser;
import org.json.*;
import java.util.concurrent.CompletableFuture;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;
import fr.packageviewer.LoggerManager;
import fr.packageviewer.pack.Package;
import fr.packageviewer.pack.SearchedPackage;

public class FedoraDistribution extends AsyncRequestsParser implements Distribution {

    protected CompletableFuture<Pair<Package, Set<String>>> getPackageFromAPI(String packageName) {
        // create a new http client
        HttpClient client = HttpClient.newHttpClient();
        // and create its url
        String url = "https://mdapi.fedoraproject.org/rawhide/pkg/"+packageName+"";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();

        CompletableFuture<Pair<Package, Set<String>>> futureResult = new CompletableFuture<>();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(result->{

            String body = result.body();

            if(body.contains("404: Not Found")) {
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
                            json.getString("description")
                    ),
                    dependenciesNames
            ));
        });
        // if there's an error, return an empty string
        return futureResult;
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
}