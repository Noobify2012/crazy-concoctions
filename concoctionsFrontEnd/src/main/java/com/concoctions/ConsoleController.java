package com.concoctions;

import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleController implements Controller {
    /**
     *
     */

    private final Appendable out;
    private final Scanner scan;

    private HttpClient client;

    public ConsoleController(Readable in, Appendable out, HttpClient client) {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Readable and Appendable can't be null");
        }
        this.out = out;
        this.scan = new Scanner(in);
        this.client = client;
    }

    protected String getUserInput() {
        String nextInput = scan.next();
        if (nextInput.isEmpty()) {
            System.out.println("Please enter a value");
            getUserInput();
        }
        return nextInput;
    }

    /**
     *
     */
    @Override
    public void start() throws IOException, InterruptedException {
        login();

    }

    @Override
    public void login() throws IOException, InterruptedException {
        String usernameString = "Please enter your username and press enter: ";
        try {
            //String element = scan.next();
            out.append(usernameString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        String userString = getUserInput();
        System.out.println("This is what I got for userName: "+ userString);

        String passwordString = "Please enter your username and press enter: ";
        try {
            //String element = scan.next();
            out.append(passwordString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }

        String password = getUserInput();
        System.out.println("This is what I got for password: "+ password);

        String inputJson = "/users/all";

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/all"))
                .header("Content-Type", "application/json")
                .GET()
//                .POST(HttpRequest.BodyPublishers.ofString(inputJson))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());


//        JSONObject loginObject = new JSONObject();
//
//        loginObject.put("username",userString).toString();
//        loginObject.put("password", password).toString();
//
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("localhost:8080"))
//                .POST(HttpRequest.BodyPublishers.ofString(loginObject))
//                .build();
//
//        HttpResponse<String> response = client.send(request,
//                HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.body());



    }


    /**
     *
     */
    @Override
    public void buildNewRecipe() {

    }

    /**
     *
     */
    @Override
    public void removeRecipe() {

    }

    /**
     *
     */
    @Override
    public void getDrinks() {

    }
}
