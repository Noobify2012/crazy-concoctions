package com.concoctions.menus;

import com.concoctions.ConsoleController;
import com.concoctions.model.Category;
import com.concoctions.model.Drink;
import com.concoctions.model.DrinkIngredient;
import com.concoctions.model.FoodItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class DrinkMenu implements DrinkMenuInt {

    private String getUserInput(Scanner scan) {
        String nextInput = scan.nextLine();
        if (nextInput.isEmpty()) {
            System.out.println("Please enter a value");
            getUserInput(scan);
        }
        return nextInput;
    }
    /**
     * @return
     */
    @Override
    public void getDrinkSearch(Scanner scan) {
        String userOption = "";
        String dir = "drinks";
        while (!userOption.equalsIgnoreCase("a") && !userOption.equalsIgnoreCase("n") && !userOption.equalsIgnoreCase("i") && !userOption.equalsIgnoreCase("c")) {
//            (parseInt(userOption) != 1 | parseInt(userOption) != 2 | parseInt(userOption) != 3 | parseInt(userOption) != 4 | parseInt(userOption) != 5) {
            String menuString = "Drink Search, Please select from one of the following options:\n A - All Drinks\n N - Drinks by Name\n I - Drinks by UserId\n C - Drinks by Category";
            System.out.println(menuString + "\n");
            userOption = getUserInput(scan);
            if (userOption.equalsIgnoreCase("a")) {
                getAllDrinks(dir);
            } else if (userOption.equalsIgnoreCase("n")) {
                getDrinksByName(dir);
            } else if (userOption.equalsIgnoreCase("i")) {
                getDrinksByID(dir);
            } else if (userOption.equalsIgnoreCase("c")) {
                getDrinksByCategory(dir);
            }
        }
    }

    /**
     * @param dir
     * @return
     */
    @Override
    public List<Drink> getAllDrinks(String dir) {

            String subDir = "all";
//        JSONObject drinkObj = new JSONObject();
//        System.out.println("login obj: " + loginObj.toString());

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/" + dir + "/" + subDir))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

//            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.statusCode());
//            String drinkstring = response.body();
//            JsonNode node = mapper.readTree(drinkstring);
////        System.out.println("Value of node: " + node);
////        System.out.println("Before parsing: " + response.body());
//            try {
//                JSONArray drinkJson = new JSONArray(drinkstring);
////            JSONArray drinkJson = parser.parse(drinkstring);
//                // have to make this 'Type' class because of type erasure for generics in Java
//                Type drinkListType = new TypeToken<List<Drink>>() {}.getType();
//                List<Drink> drinkList = gson.fromJson(response.body(), drinkListType);
//                for (Drink d : drinkList) {
//                    Long dID = d.getDrinkId();
//                    Long userId = d.getUserId();
//                    String name = d.getName();
//                    Category category = d.getCategory();
//                    String hotString;
//                    boolean isHot = d.isHot();
//                    if (isHot) {
//                        hotString = "Hot";
//                    } else {
//                        hotString = "Cold";
//                    }
//                    String description = d.getDescription();
//                    System.out.println("Drink ID: " + dID + "\nUser ID: " + userId + "\nDrink Name: " + name + "\nCategory: " + category.getName() + "\nHot or Cold: " + hotString + "\nDescription: " + description + "\nIngredients ");
//                    List<DrinkIngredient> drinkIngredients = d.getDrinkIngredients();
//                    for (DrinkIngredient di : drinkIngredients) {
//                        String alcString = "No";
//                        if (di.getIngredient().isAlcoholic()) {
//                            alcString = "Yes";
//                        }
//                        System.out.println("Ingredient: " + di.getIngredient().getName() + " description: " + di.getIngredient().getDescription() + " Contains Alcohol: " + alcString + " is this actually alcholic: " + di.getIngredient().isAlcoholic() + " " + di.getIngredient());
//                    }
//                    List<FoodItem> pairingsList = d.getPairings();
//                    if (!pairingsList.isEmpty()) {
//                        System.out.println("Pairings: ");
//                        for (FoodItem pairing : pairingsList) {
//                            System.out.println(pairing.getName() + "\n");
//                        }
//                    } else {
//                        System.out.println("");
//                    }
//                    //System.out.println(d);
//                }
//                String jsonDrinks = gson.toJson(drinkList);
//                for (int i = 0; i < drinkJson.length(); i++) {
////                Drink drink = gson.fromJson(response.body(), Drink.class);
////                System.out.println("Current value of drink: " + drink);
//                    try {
//                        org.json.JSONObject currDrink = drinkJson.getJSONObject(i);
//                        String D = currDrink.getString("drinkId");
//                        String U = currDrink.getString("userId");
//                        String N = currDrink.getString("name");
//                        String C = currDrink.getString("category");
////                    System.out.println("Value of C before unpack: " + C);
//                        C = unpack(C);
////                    System.out.println("Value of C after unpack: " + C);
////                    String T = currDrink.getString("name");
//                        String DE = currDrink.getString("description");
//                        String DI = currDrink.getString("drinkIngredients");
////                    System.out.println("before unpacking array before unpacking: " +  DI);
//                        DI = unpackArray(DI);
////                    System.out.println("after unpacking array before unpacking: " +  DI);
//                        DI = unpack(DI);
////                    System.out.println("after unpacking array after unpacking: " +  DI);
//
////                    String CI = currDrink.getString("categoryId");
//                        //System.out.println("Drink ID: " + D + " User ID: " + U + " Drink Name: " + N + " Drink Category: " + C + " Type Name: " + " Type Description: " + DE + "\nDrink Ingredients: " + DI + "\n");
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
////            Iterator<String> keys = drinkJson.;
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//
//
//            if (response.statusCode() != 200) {
//                System.out.println("It looks like we can't any drinks lets try again, try again");
//                getDrinks();
//            } else {
//                ConsoleController.mainMenu();
//            }
        return null;
    }

    /**
     * @param dir
     * @return
     */
    @Override
    public List<Drink> getDrinksByName(String dir) {
        return null;
    }

    /**
     * @param dir
     * @return
     */
    @Override
    public List<Drink> getDrinksByID(String dir) {
        return null;
    }

    /**
     * @param dir
     * @return
     */
    @Override
    public List<Drink> getDrinksByCategory(String dir) {
        return null;
    }
}
