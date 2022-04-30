package com.concoctions.menus;

import com.concoctions.ConsoleController;
import com.concoctions.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommentBuilder implements CommentBuilderInt {
    /**
     * @param user
     * @param scan
     * @param client
     * @param gson
     */
    @Override
    public void CommentMenu(User user, Scanner scan, HttpClient client, Gson gson) throws IOException, InterruptedException {
        String userString ="";
        while (userString.isEmpty()) {
            System.out.println("Comments Menu: enter (R)ead to read comments, (W)rite to write a comment");
            userString = getUserInput(scan);
            if (userString.equalsIgnoreCase("R") | userString.equalsIgnoreCase("Read")) {
                //time to read
                readMenu(user, scan, client, gson);
            } else if (userString.equalsIgnoreCase("W") | userString.equalsIgnoreCase("Write")) {
                //time to write
                writeComment(user, scan, client, gson);
            } else {
                System.out.println("I'm sorry " + userString + " is not a valid, lets try again");
                userString = "";
            }
        }
    }

    private void readMenu(User user, Scanner scan, HttpClient client, Gson gson) throws IOException, InterruptedException {
        String drinkName = "";
        System.out.println("Which drink do you want to read comments about? Please enter the drink's name");
        //get all drinks and print to screen
        String subDir = "all";
        String dir = "drinks";

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
//        JsonNode node = mapper.readTree(drinkstring);
//        System.out.println("Value of node: " + node);
//        System.out.println("Before parsing: " + response.body());
        List<Drink> drinkList;
        try {
//            JSONArray drinkJson = new JSONArray(drinkstring);
//            JSONArray drinkJson = parser.parse(drinkstring);
            // have to make this 'Type' class because of type erasure for generics in Java
            Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
            drinkList = gson.fromJson(response.body(), drinkListType);
            for (Drink d : drinkList) {
                String name = d.getName();
                System.out.println("Drink Name: " + name);
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
        //get user input and check if drink is in list
        String drinkString = "";
        Drink drinkToRead = null;
        while(drinkString.isEmpty()) {
            drinkString = getUserInput(scan);
            for (Drink d : drinkList) {
                String name = d.getName();
                if (name.equalsIgnoreCase(drinkString)) {
                    //if in list get drink
                    System.out.println("Found " + d.getName() + " , lets get those comments for you.");
                    drinkToRead = d;
                    break;
                }
            }
            if (drinkToRead == null ) {
                System.out.println("I'm sorry I didn't find that drink, please enter another name.");
                drinkString = "";
            }
        }

        //get comments using drink ID and print to screen
        dir = "comments";
        subDir = "find";
        Long drinkID = drinkToRead.getDrinkId();
        List <Comment> drinkComments = new ArrayList<>();

        URIBuilder ub;
        try {
            ub = new URIBuilder("http://localhost:8080/" + dir + "/" + subDir);
            ub.addParameter("drinkId", drinkID.toString());
        } catch (URISyntaxException e) {
            System.out.println("Threw URIexception ");
            throw new RuntimeException(e);
        }


        var request2 = HttpRequest.newBuilder()
                .uri(URI.create(ub.toString()))
                .header("Content-Type", "application/json")
                .GET()
                .build();



        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        System.out.println(response2.statusCode());


        Type drinkCommentType = new TypeToken<List<Comment>>() {}.getType();
        drinkComments = gson.fromJson(response2.body(), drinkCommentType);
        for (Comment c : drinkComments) {
            String name = c.getCommentBody();
            System.out.println("Comment: " + name);
        }

    }

    /**
     * @param UID
     * @param scan
     * @param client
     * @param gson
     * @return
     */
    @Override
    public List<Comment> getCommentsByUserID(Long UID, Scanner scan, HttpClient client, Gson gson) {
        return null;
    }

    /**
     * @param DID
     * @param scan
     * @param client
     * @param gson
     * @return
     */
    @Override
    public List<Comment> getCommentsByDrinkID(Long DID, Scanner scan, HttpClient client, Gson gson) {
        return null;
    }

    /**
     * @param user
     * @param scan
     * @param client
     * @param gson
     */
    @Override
    public void writeComment(User user, Scanner scan, HttpClient client, Gson gson) throws IOException, InterruptedException {

        String drinkName = "";
        System.out.println("Which drink do you want to write comments about? Please enter the drink's name");
        //get all drinks and print to screen
        String subDir = "all";
        String dir = "drinks";

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();

        List<Drink> drinkList;
        try {
            // have to make this 'Type' class because of type erasure for generics in Java
            Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
            drinkList = gson.fromJson(response.body(), drinkListType);
            for (Drink d : drinkList) {
                String name = d.getName();
                System.out.println("Drink Name: " + name);
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
        //get user input and check if drink is in list
        String drinkString = "";
        Drink drinkToRead = null;
        while(drinkString.isEmpty()) {
            drinkString = getUserInput(scan);
            for (Drink d : drinkList) {
                String name = d.getName();
                if (name.equalsIgnoreCase(drinkString)) {
                    //if in list get drink
                    System.out.println("Found " + d.getName() + ".");
                    drinkToRead = d;
                    break;
                }
            }
            if (drinkToRead == null ) {
                System.out.println("I'm sorry I didn't find that drink, please enter another name.");
                drinkString = "";
            }
        }

        NewComment userComment = new NewComment();
        userComment.setUserId(user.getUserId());
        userComment.setDrinkId(drinkToRead.getDrinkId());

        System.out.println("How would you rank this drink between 1 and 10? Please enter a whole number value.");
        int userRanking = 0;
        while (userRanking == 0) {
            String userInput = getUserInput(scan);
            userRanking = Integer.parseInt(userInput);
            if (userRanking > 0 && userRanking < 11) {
                System.out.println("You ranked " + drinkToRead.getName() + " a " + userRanking + " out of 10");
                userComment.setRanking(userRanking);
            } else {
                System.out.println("I'm sorry that isn't a valid input. Please select a whole number from 1 to 10.");
                userRanking = 0;
            }
        }

        System.out.println("What are you thoughts on this drink?");
        String userWords = "";
        while (userWords.isEmpty()) {
            userWords = getUserInput(scan);
            if (!userWords.isEmpty()) {
                System.out.println("Thank you for your feedback");
                userComment.setCommentBody(userWords);
            }
        }

        //send drink comment to server
        dir = "comments";
        subDir = "save";
        var request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userComment)))
                .build();


        var response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("GREAT SUCCESS I LIKE YOUR COMMENT!!!!");
        } else {
            System.out.println(response2.body());
        }
    }

    private String getUserInput(Scanner scan) {
        String temp = scan.nextLine();
        if (temp.isEmpty()) {
            System.out.println("I'm sorry, I didn't quite get that. Lets try again, please re enter your input.");
            getUserInput(scan);
        }
        return temp;
    }
}
