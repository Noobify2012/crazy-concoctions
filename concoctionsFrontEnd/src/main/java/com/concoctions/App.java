package com.concoctions;

import com.concoctions.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minidev.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.net.http.HttpClient.newHttpClient;

/**
 * Hello world!
 *
 */
public class App 
{
    private User currentUser;
    private static Gson gson;
    private static HttpClient client;
    private static Scanner in;
    public static void main( String[] args ) throws IOException, InterruptedException, JSONException {
        client = newHttpClient();
        System.out.println( "Welcome to Concoctions!" );
        Scanner in = new Scanner((System.in));
        Readable inputs = new InputStreamReader(System.in);
        Appendable output = System.out;
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        getRegStatus(in);
        new ConsoleController(inputs, output, client).start();
    }


//    private static String getUserInput(Scanner in) {
//        String nextInput = in.nextLine();
//        if (nextInput.isEmpty()) {
//            System.out.println("Please enter a value");
//            getUserInput(in);
//        }
//        return nextInput;
//    }
//
//    private static void getRegStatus(Scanner in) throws IOException, InterruptedException, JSONException {
//        System.out.println("Are you a registered user? if yes enter y or yes, if no enter no or n");
//        String registered = getUserInput(in);
//        if (registered.equalsIgnoreCase("Y")){
//            System.out.println("Welcome Back!");
//            login(in, client, gson);
//        } else if (registered.equalsIgnoreCase("N") | registered.equalsIgnoreCase("NO")) {
//            System.out.println("Welcome to Concoctions! Lets get you registered");
//            registerUser(client);
//        } else {
//            System.out.println("Hmmm I don't recognize your response lets try that again");
//            getRegStatus(in);
//        }
//    }
//
//    private static void registerUser(HttpClient client) throws IOException, InterruptedException, JSONException {
//        JSONObject newUser = new JSONObject();
//        // get email, username, password, firstname, last name, bio
//        String userRegex = "[^A-Za-z0-9]+";
//        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//        newUser = getFieldWithRegex(newUser,emailRegex,"email", in);
//        newUser = getNonEmptyField(newUser,"username", in);
//        newUser = getNonEmptyField(newUser, "password", in);
//        newUser = getNonEmptyField(newUser, "firstName", in);
//        newUser = getNonEmptyField(newUser,"lastName", in);
//        newUser = getNonEmptyField(newUser, "bio", in);
//
//        System.out.println(newUser.toString());
//
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/users/save"))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(newUser.toString()))
//                .build();
//
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
////        System.out.println(response.statusCode());
////        System.out.println(response.body());
//
//        if (response.statusCode() != 200) {
//            System.out.println("It looks there was an issue getting registered, try again");
//            registerUser(client);
//        } else {
//            login(in,client,gson);
//        }
//    }
//
//    private static JSONObject getFieldWithRegex(JSONObject object, String regex, String field, Scanner in){
//        String userString = "";
//        while(!patternMatches(userString, regex)) {
//            System.out.println("Please enter a valid " + field);
//            userString = getUserInput(in);
//        }
//        object.put(field, userString);
//        return object;
//    }
//
//    private static JSONObject getNonEmptyField(JSONObject object, String field, Scanner in){
//        String input = "";
//        while(input.isEmpty()) {
//            System.out.println("Please enter a " + field + ", empty " + field + "s not allowed");
//            input = getUserInput(in);
//        }
//        object.put(field, input);
//        return object;
//    }
//
//
//
//    private void start() throws IOException, InterruptedException, JSONException {
//        getRegStatus(in);
//    }
//
//
//    private static void login(Scanner in, HttpClient client, Gson gson) throws IOException, InterruptedException, JSONException {
//        String usernameString = "Please enter your username and press enter: ";
//        System.out.println(usernameString + "\n");
//
//        String userString = getUserInput(in);
////        System.out.println("This is what I got for userName: "+ userString);
//
//        String passwordString = "Please enter your username and press enter: ";
//        System.out.println(passwordString + "\n");
//
//        String password = getUserInput(in);
////        System.out.println("This is what I got for password: "+ password);
//
//        JSONObject loginObj = new JSONObject();
//        loginObj.put("username", userString);
//        loginObj.put("password", password);
////        System.out.println("login obj: " + loginObj.toString());
//
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/login"))
//                .header("Content-Type", "application/json")
////                .GET()
//                .POST(HttpRequest.BodyPublishers.ofString(loginObj.toString()))
//                .build();
//
//        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//
//        if (response.statusCode() != 200) {
//            System.out.println("It looks like we can't find you, try again");
//            login(in, client, gson);
//        } else {
////            JSONArray userJson = new JSONArray(response.toString());
////            JSONArray drinkJson = parser.parse(drinkstring);
//            // have to make this 'Type' class because of type erasure for generics in Java
//            Type userType = new TypeToken<User>() {}.getType();
//            currentUser = gson.fromJson(response.body(), userType);
//        }
//
//    }

    public static boolean patternMatches(String userString, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(userString)
                .matches();
    }

}
