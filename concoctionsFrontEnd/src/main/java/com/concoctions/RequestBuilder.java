package com.concoctions;

import com.google.gson.JsonObject;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestBuilder implements Request {
    /**
     * @param dir
     * @param query
     * @return
     */
    @Override
    public HttpResponse<String> singleDirGet(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException {
        URIBuilder ub;
        try {
            ub = new URIBuilder("http://localhost:8080/");
            ub.addParameter(query, userString);
            String possibleOutput = ub.toString();
            System.out.println("output: " + possibleOutput);
        } catch (URISyntaxException e) {
            System.out.println("Threw URIexception ");
            throw new RuntimeException(e);
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(ub.toString()))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
        if (response.statusCode() != 200) {
            System.out.println("looks like there was an issue heres what we got back: " + response);
        } else {

        }
        return response;
    }

    /**
     * @param dir
     * @param subDir
     * @param query
     * @return
     */
    @Override
    public HttpResponse<String> twoDirGet(String dir, String subDir, String query, String userString, HttpClient client) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
        return response;
    }

    /**
     * @param dir
     * @param query
     * @return
     */
    @Override
    public HttpResponse<String> singleDirPost(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
        return null;
    }

    /**
     * @param dir
     * @param subDir
     * @param query
     * @return
     */
    @Override
    public HttpResponse<String> twoDirPost(String dir, String subDir, String object, String userString, HttpClient client) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(object))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
        return response;
    }


    /**
     * @param string
     * @return
     */
    @Override
    public String userInput2URL(String string) {
        return null;
    }
}
