package com.concoctions;

import com.sun.source.tree.WhileLoopTree;
import net.minidev.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

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

    private void getRegStatus() throws IOException, InterruptedException, JSONException {
        System.out.println("Are you a registered user? if yes enter y or yes, if no enter no or n");
        String registered = getUserInput();
        if (registered.equalsIgnoreCase("Y")){
            System.out.println("Welcome Back!");
            login();
        } else if (registered.equalsIgnoreCase("N") | registered.equalsIgnoreCase("NO")) {
            System.out.println("Welcome to Concoctions! Lets get you registered");
            registerUser();
        } else {
            System.out.println("Hmmm I don't recognize your response lets try that again");
            getRegStatus();
        }
    }

    private void registerUser() throws IOException, InterruptedException, JSONException {
        JSONObject newUser = new JSONObject();
        // get email, username, password, firstname, last name, bio
        String userRegex = "[^A-Za-z0-9]+";
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        newUser = getFieldWithRegex(newUser,emailRegex,"email");
        newUser = getNonEmptyField(newUser,"username");
        newUser = getNonEmptyField(newUser, "password");
        newUser = getNonEmptyField(newUser, "firstName");
        newUser = getNonEmptyField(newUser,"lastName");
        newUser = getNonEmptyField(newUser, "bio");

        System.out.println(newUser.toString());

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/users/save"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(newUser.toString()))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        if (response.statusCode() != 200) {
            System.out.println("It looks there was an issue getting registered, try again");
            registerUser();
        } else {
            login();
        }
    }

    private JSONObject getFieldWithRegex(JSONObject object, String regex, String field){
        String userString = "";
        while(!patternMatches(userString, regex)) {
            System.out.println("Please enter a valid " + field);
            userString = getUserInput();
        }
        object.put(field, userString);
        return object;
    }

    private JSONObject getNonEmptyField(JSONObject object, String field){
        String input = "";
        while(input.isEmpty()) {
            System.out.println("Please enter a " + field + ", empty" + field + "s not allowed");
            input = getUserInput();
        }
        object.put(field, input);
        return object;
    }
//    private JSONObject getEmail(JSONObject newUser) {
//        String email = "";
//        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//        while(!patternMatches(email, emailRegex)) {
//            System.out.println("Please enter a valid email");
//            email = getUserInput();
//        }
//        newUser.put("email", getUserInput());
//        return newUser;
//    }
//
//    private JSONObject getUserName(JSONObject newUser) {
//        String username = "";
//        String usernameRegex = "[^A-Za-z0-9]+";
//        while(!patternMatches(username, usernameRegex)) {
//            System.out.println("Please enter a username, letters and numbers only, no spaces");
//            username = getUserInput();
//        }
//        newUser.put("username", getUserInput());
//        return newUser;
//    }
//
//    private JSONObject getPassword(JSONObject newUser) {
//        String password = "";
//        while(password.isEmpty()) {
//            System.out.println("Please enter a password, empty passwords not allowed");
//            password = getUserInput();
//        }
//        newUser.put("password", getUserInput());
//        return newUser;
//    }

    /**
     *
     */
    @Override
    public void start() throws IOException, InterruptedException, JSONException {
        getRegStatus();
//        System.out.println("Are you a registered user? if yes enter y or yes, if no enter no or n");
//        String registered = getUserInput();
//
//
//        login();

    }



    @Override
    public void login() throws IOException, InterruptedException, JSONException {
        String usernameString = "Please enter your username and press enter: ";
        try {
            //String element = scan.next();
            out.append(usernameString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        String userString = getUserInput();
//        System.out.println("This is what I got for userName: "+ userString);

        String passwordString = "Please enter your username and press enter: ";
        try {
            //String element = scan.next();
            out.append(passwordString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }

        String password = getUserInput();
//        System.out.println("This is what I got for password: "+ password);

        JSONObject loginObj = new JSONObject();
        loginObj.put("username", userString);
        loginObj.put("password", password);
//        System.out.println("login obj: " + loginObj.toString());

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .header("Content-Type", "application/json")
//                .GET()
                .POST(HttpRequest.BodyPublishers.ofString(loginObj.toString()))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        if (response.statusCode() != 200) {
            System.out.println("It looks like we can't find you, try again");
            login();
        } else {
            mainMenu();
        }

    }

    protected void mainMenu() {

        String userOption = "";
        while (!userOption.equalsIgnoreCase("s") && !userOption.equalsIgnoreCase("n") && !userOption.equalsIgnoreCase("e") && !userOption.equalsIgnoreCase("c") && !userOption.equalsIgnoreCase("q")) {
//            (parseInt(userOption) != 1 | parseInt(userOption) != 2 | parseInt(userOption) != 3 | parseInt(userOption) != 4 | parseInt(userOption) != 5) {
            String menuString = "Concoctions Menu, Please select from one of the following options:\n S - Search for Drinks\n N - Create New Drinks\n E - Edit or remove your drinks\n C - Leave/Read Comments a Comment\n Q - Quit";
            try {
                //String element = scan.next();
                out.append(menuString + "\n");
            } catch (IOException ioe) {
                throw new IllegalStateException("Append failed", ioe);
            }
            userOption = getUserInput();
            System.out.println(userOption);
        }
        if (userOption.equalsIgnoreCase("s")) {
            getDrinks();
        } else if (userOption.equalsIgnoreCase("n")) {
            buildNewRecipe();
        } else if (userOption.equalsIgnoreCase("e")) {
            removeRecipe();
        } else if (userOption.equalsIgnoreCase("c")) {
            commentsMenU();
        } else if (userOption.equalsIgnoreCase("q")) {
            quit();
        }
    }

    private void quit() {
        String quitString = "Thank you for using concoctions";
        try {
            out.append(quitString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        System.exit(0);
    }

    /**
     *
     */
    @Override
    public void createProfile() {

    }


    /**
     *
     */
    @Override
    public void buildNewRecipe() {
        String drinkSearch = "Time to build some drinks";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void removeRecipe() {
        String drinkSearch = "Time to remove some drinks";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void getDrinks() {
        String drinkSearch = "Time to search for some drinks";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void editRecipe() {
        String drinkSearch = "Time to edit some drinks ";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    protected void commentsMenU() {
        String userOption = "";
        while (!userOption.equalsIgnoreCase("r") && !userOption.equalsIgnoreCase("l")) {
            String menuString = "Comment Menu, Please select from one of the following options:\n R - Read Comments\n L - Leave a Comment";
            try {
                //String element = scan.next();
                out.append(menuString + "\n");
            } catch (IOException ioe) {
                throw new IllegalStateException("Append failed", ioe);
            }
            userOption = getUserInput();
        }
        if (userOption.equalsIgnoreCase("r")) {
            readComments();
        } else if (userOption.equalsIgnoreCase("l")) {
            leaveComments();
        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void readComments() {
        String menuString = "read comments";
        try {
            //String element = scan.next();
            out.append(menuString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void leaveComments() {
        String menuString = "leave comments";
        try {
            //String element = scan.next();
            out.append(menuString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    public static boolean patternMatches(String userString, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(userString)
                .matches();
    }
}
