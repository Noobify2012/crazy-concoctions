package com.concoctions;


import com.concoctions.menus.*;
import com.concoctions.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minidev.json.JSONObject;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Pattern;

public class ConsoleController implements Controller {
    /**
     *
     */
    private final Appendable out;
    private final Scanner scan;
    private HttpClient client;
    private ObjectMapper mapper;
    private Gson gson;
    private DrinkMenuInt DrinkMenu;

    private User user;

    private Request request;

    private CommentBuilderInt commentMenu;

    private DrinkBuilderInt builder;

    public ConsoleController(Readable in, Appendable out, HttpClient client) {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Readable and Appendable can't be null");
        }
        this.out = out;
        this.scan = new Scanner(in);
        this.client = client;
        mapper = new ObjectMapper();
        this.gson = new GsonBuilder().setPrettyPrinting().create();;
        DrinkMenu = new DrinkMenu();
        this.request = new RequestBuilder();
        this.commentMenu = new CommentBuilder();
        this.builder = new DrinkBuilder();
    }

    private String getUserInput() {
        String nextInput = scan.nextLine();
        if (nextInput.isEmpty()) {
            System.out.println("Please enter a value");
            getUserInput();
        }
        return nextInput;
    }

    private void getRegStatus() throws IOException, InterruptedException, JSONException {
        System.out.println("Are you a registered user? if yes enter y or yes, if no enter no or n");
        String registered = getUserInput();
        if (registered.equalsIgnoreCase("Y")| registered.equalsIgnoreCase("yes")){
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
//        System.out.println(response.statusCode());
//        System.out.println(response.body());

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
            System.out.println("Please enter a " + field + ", empty " + field + "s not allowed");
            input = getUserInput();
        }
        object.put(field, input);
        return object;
    }


    /**
     *
     */
    @Override
    public void start() throws IOException, InterruptedException, JSONException {
        getRegStatus();
        mainMenu();
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

        String passwordString = "Please enter your password and press enter: ";
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
            String tempUser = response.body().toString();
            this.user = gson.fromJson(tempUser, User.class);
            mainMenu();
        }

    }

    protected void mainMenu() throws IOException, InterruptedException {

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
        }
        if (userOption.equalsIgnoreCase("s")) {
            getDrinks();
            mainMenu();
        } else if (userOption.equalsIgnoreCase("n")) {
            buildNewRecipe();
            mainMenu();
        } else if (userOption.equalsIgnoreCase("e")) {
            removeRecipe();
            mainMenu();
        } else if (userOption.equalsIgnoreCase("c")) {
            commentsMenu();
            mainMenu();
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
    public void buildNewRecipe() throws IOException, InterruptedException {
        Request send = new RequestBuilder();
        String dir = "drinks";
        String subDir = "save";
        System.out.println("Time to build some drinks");
        DrinkBuilder drinkBuilder = new DrinkBuilder();
        NewDrink newDrink = drinkBuilder.buildNewDrink(scan, gson, this.user, client);
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newDrink)))
                .build();
        System.out.println(gson.toJson(newDrink));

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();

        if (response.statusCode() == 200) {
            System.out.println("GREAT SUCCESS I LIKE YOUR DRINK!!!!");
        } else {
            System.out.println(response.body());

        }
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void removeRecipe() throws IOException, InterruptedException {
        Drink drink = new Drink();
        System.out.println("Time to remove some drinks");
        List<Drink> usersDrinks = getDrinkListByID("drinks", user.getUserId());
        System.out.println("Which drink would you like to remove or edit? ");
        String drinkName = getUserInput();
        System.out.println("Size of usersDrinks: " + usersDrinks.size());
        System.out.println("Drink name received: " + drinkName);
        for (int i = 0; i < usersDrinks.size(); i++){
            System.out.println("Current Drink Name: " + usersDrinks.get(i).getName());
            if (usersDrinks.get(i).getName().equalsIgnoreCase(drinkName)) {
                drink = usersDrinks.get(i);
                System.out.println("Bang");
                break;
            }
        }
        if (drink.getName() == null) {
            System.out.println("we didn't find your drink lets try again");
            removeRecipe();
        }
        System.out.println("Enter (R)emove or (E)dit to remove or edit the drink");
        String action = getUserInput();
        if (action.equalsIgnoreCase("R") | action.equalsIgnoreCase("Remove")) {
            var deleteResponse = request.removeDrinkDelete("drinks", "delete", drink.getDrinkId(), client);
            System.out.println(deleteResponse.statusCode());
            if (deleteResponse.statusCode() == 204) {
                System.out.println("Drink removed");
                mainMenu();
            } else {
                System.out.println("Drink removal failed");
                removeRecipe();
            }
        } else if (action.equalsIgnoreCase("E") | action.equalsIgnoreCase("Edit")) {


            NewDrink updatedDrink = new NewDrink();
            boolean complete = false;
            List<DrinkIngredient> drinkIngredients = drink.getDrinkIngredients();
            //while loop until complete
            while (!complete) {


                String ingredientChangeChoice = "";
                while (ingredientChangeChoice.isEmpty()) {
                    System.out.println("Do you want to add or remove an ingredient? (A)dd or (R)emove");
                    ingredientChangeChoice = getUserInput();
                    if (ingredientChangeChoice.equalsIgnoreCase("A") | (ingredientChangeChoice.equalsIgnoreCase("Add"))) {
                        //get all possible ingredients
                        String ingredientDir = "ingredients";
                        String ingredientSubDir = "all";
                        HttpResponse<String> response = request.twoDirGet(ingredientDir, ingredientSubDir, "", "", client);
                        Type ingredientListType = new TypeToken<List<Ingredient>>() {
                        }.getType();
                        //List of all possible ingredients
                        List<Ingredient> ingredientList = gson.fromJson(response.body(), ingredientListType);
                        for (Ingredient d : ingredientList) {
                            System.out.println("Ingredient: " + d.getName());
                        }

                        //
                        Type drinkIngredientListType = new TypeToken<List<DrinkIngredient>>() {
                        }.getType();
                        List<DrinkIngredient> drinkIngredientList = gson.fromJson(response.body(), drinkIngredientListType);


                        //have user select ingredients
                        boolean ingComplete = false;
                        while (!ingComplete) {
                            //make new drink ingredient to add
                            DrinkIngredient di = new DrinkIngredient();

                            //get the ingredient
                            Ingredient ingredient = getIngredient(ingredientList, scan);
                            //set the ingredient
                            di.setIngredient(ingredient);

                            //get the uom from the user
                            Uom uom = builder.getUom(builder.getAllUom((RequestBuilder) request, client, gson), scan);

                            di.setUom(uom);
                            //
                            System.out.println("Please enter the number of " + di.getUom().getName() + " of " + di.getIngredient().getName() + " you would like to add to the drink.");
                            //get the amount of ingredient
                            double amount = builder.getUserInputDouble(scan);
                            //set ingredient amount
                            di.setAmount(amount);

                            //add ingredient to the drink
                            drinkIngredientList.add(di);
                            drinkIngredients.add(di);

                            //do i want to add more
                            ingComplete = builder.isComplete(scan);
                            for (DrinkIngredient dil : drinkIngredients) {
                                if (dil.getAmount() != 0.0) {
                                    System.out.println("Ingredient: " + dil.getAmount() + " " + dil.getUom().getName() + " " + dil.getIngredient().getName());
                                }
                            }
                        }

                    } else if (ingredientChangeChoice.equalsIgnoreCase("R") | (ingredientChangeChoice.equalsIgnoreCase("Remove"))) {
                        //print ingredients in drink
                        // list ingredients

                        String ingredientToRemoveName = "";
                        DrinkIngredient ingredientToRemove = new DrinkIngredient();
                        while (ingredientToRemoveName.isEmpty()) {
                            for (DrinkIngredient di : drinkIngredients) {
                                System.out.println("Ingredient: " + di.getIngredient().getName());
                            }
                            //select ingredient to remove
                            System.out.println("Please select an ingredient from the list above to remove.");
                            ingredientToRemoveName = getUserInput();

                            for (DrinkIngredient di : drinkIngredients) {
                                if (ingredientToRemoveName.equalsIgnoreCase(di.getIngredient().getName())) {
                                    System.out.println("Found the ingredient");
                                    //remove from recipe
                                    drinkIngredients.remove(di);
                                    break;
                                }
                            }
                            if (ingredientToRemove == null) {
                                System.out.println("I'm sorry we couldn't find that ingredient, lets try again.");
                            }
                        }

                    } else {
                        System.out.println("I'm sorry that isn't a valid choice, please try again. ");
                        ingredientChangeChoice = "";
                    }
                }
                System.out.println("This is for the entire drink");
                complete = builder.isCompleteEdit(scan);

            }
            System.out.println("New List: " + drinkIngredients);
            NewDrink drinkToUpdate = new NewDrink();
            drinkToUpdate.setUserId(user.getUserId());
            drinkToUpdate.setName(drink.getName());
            drinkToUpdate.setCategoryId(drink.getCategory().getCategoryId());
            drinkToUpdate.setHot(drink.isHot());
            drinkToUpdate.setDescription(drink.getDescription());
            List <DrinkIngredientDto> ingredientsToAdd = new ArrayList<>();
            for (DrinkIngredient di : drinkIngredients) {
                DrinkIngredientDto temp = new DrinkIngredientDto();
                temp.setIngredientId(di.getIngredient().getIngredientId());
                temp.setUomId(di.getUom().getUomId());
                temp.setAmount(di.getAmount());
                ingredientsToAdd.add(temp);
            }
            drinkToUpdate.setDrinkIngredients(ingredientsToAdd);

            List <Long> foodToAdd = new ArrayList<>();
            for (FoodItem fi : drink.getPairings()) {
                Long temp = 0L;
                temp = fi.getFoodItemId();
                foodToAdd.add(temp);
            }
            drinkToUpdate.setFoodItemIds(foodToAdd);

            // put @ /update/{drinkId
            var updateResponse = request.updateDrinkPut("drinks", "update", drinkToUpdate, drink.getDrinkId(), client, gson);

            int updateStatus = 0;
            updateStatus = updateResponse.statusCode();
            System.out.println("This is the update status : " + updateStatus);
            if(updateStatus == 200) {
                System.out.println("This seems new and fresh. Thanks for the update!");
                mainMenu();
            } else {
                System.out.println("Boo it blew up, lets try that again.");
                removeRecipe();
            }
        }
    }


    private Ingredient getIngredient(List<Ingredient> ingredientList, Scanner scan) {
        Ingredient temp = new Ingredient();
        while (temp.getName() == null) {
            System.out.println("Please enter the name of an ingredient that you would like to use from the list.");
            String ing = scan.nextLine();

            boolean nameMatch = false;
            for (Ingredient i : ingredientList) {
                if (ing.equalsIgnoreCase(i.getName())) {
                    temp = i;
                    System.out.println("Adding " + i.getName());
                    break;
                }
            }
        }
        return temp;
    }


    /**
     *
     */
    @Override
    public void getDrinks() throws IOException, InterruptedException {
        String drinkSearch = "Time to search for some drinks";
        String drinkDir = "drinks";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        String userOption = "";
        while (!userOption.equalsIgnoreCase("a") && !userOption.equalsIgnoreCase("n") && !userOption.equalsIgnoreCase("i") && !userOption.equalsIgnoreCase("c")) {
//            (parseInt(userOption) != 1 | parseInt(userOption) != 2 | parseInt(userOption) != 3 | parseInt(userOption) != 4 | parseInt(userOption) != 5) {
            String menuString = "Drink Search, Please select from one of the following options:\n A - All Drinks\n N - Drinks by Name\n I - Drinks by UserId\n C - Drinks by Category";
            try {
                //String element = scan.next();
                out.append(menuString + "\n");
            } catch (IOException ioe) {
                throw new IllegalStateException("Append failed", ioe);
            }
            userOption = getUserInput();
        }
        if (userOption.equalsIgnoreCase("a")) {
            getAllDrinks(drinkDir);
        } else if (userOption.equalsIgnoreCase("n")) {
            getDrinksByName(drinkDir);
        } else if (userOption.equalsIgnoreCase("i")) {
            getDrinksByID(drinkDir);
        } else if (userOption.equalsIgnoreCase("c")) {
            getDrinksByCategory(drinkDir);
        }

        mainMenu();
    }

    protected void getDrinksByCategory(String dir) throws IOException, InterruptedException {
        String userOption = "";
        Category userCategory = null;
        while (userOption.isEmpty()) {
            var allResponse = request.twoDirGet("category", "all","", "", client);
            Type categoryListType = new TypeToken<List<Category>>() {}.getType();
            List<Category> categoryList = gson.fromJson(allResponse.body(), categoryListType);
            for (Category c : categoryList) {
                System.out.println(c.getName());
            }
            String menuString = "Please enter a category to search by from the above list";
            try {
                //String element = scan.next();
                out.append(menuString + "\n");
            } catch (IOException ioe) {
                throw new IllegalStateException("Append failed", ioe);
            }
            userOption = getUserInput();
            for (Category c : categoryList) {
                if (userOption.equalsIgnoreCase(c.getName())) {
                    userCategory = c;
                }
            }

            if (userCategory == null) {
                System.out.println("I'm sorry, we didn't find your category. Lets start over.");
                getDrinksByCategory(dir);
            }
        }

        String subDir = "find";
        URIBuilder ub;
        try {
            ub = new URIBuilder("http://localhost:8080/" + dir + "/" + subDir);
            ub.addParameter("categoryName", userOption);
            String possibleOutput = ub.toString();
//            System.out.println("Possible output1: " + possibleOutput);
        } catch (URISyntaxException e) {
            System.out.println("Threw URIexception ");
            throw new RuntimeException(e);
        }


        var request = HttpRequest.newBuilder()
                .uri(URI.create(ub.toString()))
                .header("Content-Type", "application/json")
                .GET()
//                .POST(HttpRequest.BodyPublishers.ofString(drinkObj.toString()))
                .build();



        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
        List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
        for (Drink drink : drinkList) {
            System.out.println("User Id: " + drink.getUserId() + " Name: " + drink.getName() + " Category: " + drink.getCategory().getName() + " Drink Description: " + drink.getDescription());
            for (int i = 0; i < drink.getDrinkIngredients().size(); i++){
                System.out.println("Ingredient: " + drink.getDrinkIngredients().get(i).getIngredient().getName() + " amount " + drink.getDrinkIngredients().get(i).getAmount() + " " + drink.getDrinkIngredients().get(i).getUom().getName());
            }
            for (int i = 0; i < drink.getPairings().size(); i++){
                System.out.println("Pairs with : " + drink.getPairings().get(i).getName());
            }
        }
//        System.out.println(request);
////        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.statusCode());
//        String drinkstring = response.body();
//        System.out.println(drinkstring);
    }

    protected void getDrinksByID(String dir) throws IOException, InterruptedException {
        String userOption = "";
        String menuString = "Please enter a user ID to search by";
        try {
            //String element = scan.next();
            out.append(menuString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        userOption = getUserInput();
        String subDir = "find";
        URIBuilder ub;
        try {
            ub = new URIBuilder("http://localhost:8080/" + dir + "/" + subDir);
            ub.addParameter("userId", userOption);
            String possibleOutput = ub.toString();
//            System.out.println("Possible output2: " + possibleOutput);
        } catch (URISyntaxException e) {
            System.out.println("Threw URIexception ");
            throw new RuntimeException(e);
        }


        var request = HttpRequest.newBuilder()
                .uri(URI.create(ub.toString()))
                .header("Content-Type", "application/json")
                .GET()
//                .POST(HttpRequest.BodyPublishers.ofString(drinkObj.toString()))
                .build();



        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
        List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
        for (Drink drink : drinkList) {
            System.out.println("User Id: " + drink.getUserId() + " Name: " + drink.getName() + " Category: " + drink.getCategory().getName() + " Drink Description: " + drink.getDescription());
            for (int i = 0; i < drink.getDrinkIngredients().size(); i++){
                System.out.println("Ingredient: " + drink.getDrinkIngredients().get(i).getIngredient().getName() + " amount " + drink.getDrinkIngredients().get(i).getAmount() + " " + drink.getDrinkIngredients().get(i).getUom().getName());
            }
            for (int i = 0; i < drink.getPairings().size(); i++){
                System.out.println("Pairs with : " + drink.getPairings().get(i).getName());
            }
        }
//        System.out.println(request);
////        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.statusCode());
//        String drinkstring = response.body();
//        System.out.println(drinkstring);
    }

    protected List<Drink> getDrinkListByID(String dir, Long UID) throws IOException, InterruptedException {
        List<Drink> userList = new ArrayList<>();
        String subDir = "find";
        URIBuilder ub;
        try {
            ub = new URIBuilder("http://localhost:8080/" + dir + "/" + subDir);
            ub.addParameter("userId", UID.toString());
            String possibleOutput = ub.toString();
//            System.out.println("Possible output3: " + possibleOutput);
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

        Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
        List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
        for (Drink d : drinkList) {
            userList.add(d);
            System.out.println("Drink: " + d.getName());
        }

        return userList;
    }


    protected void getDrinksByName(String dir) throws IOException, InterruptedException {
        String userOption = "";
        String menuString = "Please enter a drink name to search by";
        try {
            //String element = scan.next();
            out.append(menuString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        userOption = getUserInput();
        String subDir = "find";
        String possibleOutput = "";

        try {
            URIBuilder ub = new URIBuilder("http://localhost:8080/" + dir + "/" + subDir);
            ub.addParameter("drinkName", userOption);
            possibleOutput = ub.toString();
        } catch (URISyntaxException e) {
            System.out.println("Threw URIexception ");
            throw new RuntimeException(e);
        }

        var request = HttpRequest.newBuilder()
                .uri(URI.create(possibleOutput))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
        List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
        for (Drink drink : drinkList) {
            System.out.println("User Id: " + drink.getUserId() + " Name: " + drink.getName() + " Category: " + drink.getCategory().getName() + " Drink Description: " + drink.getDescription());
            for (int i = 0; i < drink.getDrinkIngredients().size(); i++){
                System.out.println("Ingredient: " + drink.getDrinkIngredients().get(i).getIngredient().getName() + " amount " + drink.getDrinkIngredients().get(i).getAmount() + " " + drink.getDrinkIngredients().get(i).getUom().getName());
            }
            for (int i = 0; i < drink.getPairings().size(); i++){
                System.out.println("Pairs with : " + drink.getPairings().get(i).getName());
            }
        }


    }

    protected void getAllDrinks(String dir) throws IOException, InterruptedException {
        String subDir = "all";

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        String drinkstring = response.body();
        JsonNode node = mapper.readTree(drinkstring);

        try {
            JSONArray drinkJson = new JSONArray(drinkstring);
//            JSONArray drinkJson = parser.parse(drinkstring);
            // have to make this 'Type' class because of type erasure for generics in Java
            Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
            List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
            for (Drink d : drinkList) {
                Long dID = d.getDrinkId();
                Long userId = d.getUserId();
                String name = d.getName();
                Category category = d.getCategory();
                String hotString;
                boolean isHot = d.isHot();
                if (isHot) {
                    hotString = "Hot";
                } else {
                    hotString = "Cold";
                }
                String description = d.getDescription();
                System.out.println("Drink : " + name + "\nCategory: " + category.getName() + "\nHot or Cold: " + hotString + "\nDescription: " + description);
                List<DrinkIngredient> drinkIngredients = d.getDrinkIngredients();
                for (DrinkIngredient di : drinkIngredients) {
                    String alcString = "No";
                    if (di.getIngredient().isAlcoholic()) {
                        alcString = "Yes";
                    }
                    System.out.println("Ingredient: " + di.getIngredient().getName() + " Contains Alcohol: " + alcString);
                }
                List<FoodItem> pairingsList = d.getPairings();
                if (!pairingsList.isEmpty()) {
                    System.out.println("Pairings: ");
                    for (FoodItem pairing : pairingsList) {
                        System.out.println(pairing.getName() + "\n");
                    }
                } else {
                    System.out.println("");
                }
                //System.out.println(d);
            }
            String jsonDrinks = gson.toJson(drinkList);
            for (int i = 0; i < drinkJson.length(); i++) {
//                Drink drink = gson.fromJson(response.body(), Drink.class);
//                System.out.println("Current value of drink: " + drink);
                try {
                    org.json.JSONObject currDrink = drinkJson.getJSONObject(i);
                    String D = currDrink.getString("drinkId");
                    String U = currDrink.getString("userId");
                    String N = currDrink.getString("name");
                    String C = currDrink.getString("category");
//                    System.out.println("Value of C before unpack: " + C);
                    C = unpack(C);
//                    System.out.println("Value of C after unpack: " + C);
//                    String T = currDrink.getString("name");
                    String DE = currDrink.getString("description");
                    String DI = currDrink.getString("drinkIngredients");
//                    System.out.println("before unpacking array before unpacking: " +  DI);
                    DI = unpackArray(DI);
//                    System.out.println("after unpacking array before unpacking: " +  DI);
                    DI = unpack(DI);
//                    System.out.println("after unpacking array after unpacking: " +  DI);

//                    String CI = currDrink.getString("categoryId");
                    //System.out.println("Drink ID: " + D + " User ID: " + U + " Drink Name: " + N + " Drink Category: " + C + " Type Name: " + " Type Description: " + DE + "\nDrink Ingredients: " + DI + "\n");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
//            Iterator<String> keys = drinkJson.;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        if (response.statusCode() != 200) {
            System.out.println("It looks like we find can't any drinks lets try again, try again");
            getDrinks();
        } else {
            mainMenu();
        }
    }

    private String unpack(String string) throws JSONException {
        org.json.JSONObject unpacked = new org.json.JSONObject(string);
//        System.out.println("Value of unpacked: " + unpacked);
        String outbound = "";
        Map<Object,Object> unpackedMap = new HashMap<>();
        unpacked.keys().forEachRemaining(key -> {
            Object value = null;
            try {
                value = unpacked.get((String) key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            unpackedMap.put(key, value);
//            System.out.println("nested objects key: " + key + " value: " + value + "\n");
                });
//        for (int i = 0; i < unpacked.keys(); i++) {
//            outbound += unpacked.toString(i) + "\n";
//        }
        for (Object key: unpackedMap.keySet()) {
            outbound += " " + key + ": " + unpackedMap.get(key) + "\n";
            //System.out.println("Current key value pair: " + key + " " + unpackedMap.get(key));
        }
        return outbound;
//        return outbound;
    }

    private String unpackArray (String string) throws JSONException, IOException, InterruptedException {
        JSONArray array = new JSONArray(string);
        String outbound = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                org.json.JSONObject currDrink = array.getJSONObject(i);
                outbound += currDrink  + "\n";

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

    }
        return outbound;}

    /**
     *
     */
    @Override
    public void editRecipe() throws IOException, InterruptedException {
        String drinkSearch = "Time to edit some drinks ";
        try {
            //String element = scan.next();
            out.append(drinkSearch + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        mainMenu();
    }

    protected void commentsMenu() throws IOException, InterruptedException {
        commentMenu.CommentMenu(user, scan, client, gson);
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void readComments() throws IOException, InterruptedException {
        String menuString = "read comments";
        try {
            //String element = scan.next();
            out.append(menuString + "\n");
        } catch (IOException ioe) {
            throw new IllegalStateException("Append failed", ioe);
        }
        commentMenu.CommentMenu(user, scan, client, gson);
        mainMenu();
    }

    /**
     *
     */
    @Override
    public void leaveComments() throws IOException, InterruptedException {
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
