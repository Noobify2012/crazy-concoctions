package com.concoctions.menus;

import com.concoctions.ConsoleController;
import com.concoctions.RequestBuilder;
import com.concoctions.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DrinkBuilder implements DrinkBuilderInt {
    /**
     * @param scan
     * @param gson
     * @return
     */
    @Override
    public NewDrink buildNewDrink(Scanner scan, Gson gson, User user, HttpClient client) throws IOException, InterruptedException {
        NewDrink temp = new NewDrink();
        RequestBuilder requestBuilder = new RequestBuilder();
        //set the user id to the current users id
        temp.setUserId(user.getUserId());
        System.out.println("userID: " + temp.getUserId());

        //get the drink name
        String drinkName = "";
        while (drinkName.isEmpty()) {
            System.out.println("What would you like to call this drink?");
            drinkName = getUserInput(scan);
        }
        System.out.println("drink name: " + drinkName);
        temp.setName(drinkName);
        //get catagories

        //get if drink is hot or cold
        temp.setHot(getDrinkTemp(scan));
        System.out.println("hot flag: " + temp.isHot());

        //get drink description
        System.out.println("Please enter a description for your drink");
        temp.setDescription(getUserInput(scan));

        //get possible ingredients
        String ingredientDir = "ingredients";
        String ingredientSubDir = "all";
        HttpResponse<String> response = requestBuilder.twoDirGet(ingredientDir,ingredientSubDir,"", "", client);
        Type ingredientListType = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> ingredientList = gson.fromJson(response.body(), ingredientListType);
        for (Ingredient d : ingredientList) {
            System.out.println("Ingredient: " + d.getName());
        }
//        String jsonDrinks = gson.toJson(ingredientList);

        Type drinkIngredientListType = new TypeToken<List<DrinkIngredient>>() {}.getType();
        List<DrinkIngredient> drinkIngredientList = gson.fromJson(response.body(), drinkIngredientListType);
        List<DrinkIngredientDto> drinkIngredientDtos = new ArrayList<>();
        //have user select ingredients
        boolean ingComplete = false;
        while(!ingComplete) {
            DrinkIngredient di = new DrinkIngredient();
            DrinkIngredientDto ingredientDto = new DrinkIngredientDto();
            //get the ingredient
            Ingredient ingredient = getIngredient(ingredientList, scan);
            di.setIngredient(ingredient);
            ingredientDto.setIngredientId(ingredient.getIngredientId());
            Uom uom = getUom(getAllUom(requestBuilder, client, gson), scan);
            ingredientDto.setUomId(uom.getUomId());
            di.setUom(uom);
            System.out.println("Please enter the number of " + di.getUom().getName() + " of " + di.getIngredient().getName() + " you would like to add to the drink.");
            double amount = getUserInputDouble(scan);
            di.setAmount(amount);
            ingredientDto.setAmount(amount);
            drinkIngredientList.add(di);
            drinkIngredientDtos.add(ingredientDto);

            ingComplete = isComplete(scan);
            for (DrinkIngredient dil : drinkIngredientList) {
                if (dil.getAmount() != 0.0) {
                    System.out.println("Ingredient: " + dil.getAmount() + " " + dil.getUom().getName() + " " + dil.getIngredient().getName());
                }
            }
        }
        //add ingredients to the new recipe
        temp.setDrinkIngredients(drinkIngredientDtos);

        //get possible food items
        String foodDir = "fooditem";
        String foodSubDir = "all";
        HttpResponse<String> foodResponse = requestBuilder.twoDirGet(foodDir,foodSubDir,"", "", client);
        Type foodListType = new TypeToken<List<FoodItem>>() {}.getType();
        List<FoodItem> foodList = gson.fromJson(foodResponse.body(), foodListType);
        for (FoodItem f : foodList) {
            System.out.println("Food Item: " + f.getName());
        }
        List<Long> foodIdList = new ArrayList<>();
        List<FoodItem> selectedFood = new ArrayList<>();
        //have user add possible food items
        boolean foodComplete = false;
        while(!foodComplete) {
            FoodItem fi = new FoodItem();
            //get the ingredient
            fi = getFoodItem(foodList, scan);
            selectedFood.add(fi);
            foodIdList.add(fi.getFoodItemId());
            foodComplete = isComplete(scan);
            for (FoodItem fil : selectedFood) {
                System.out.println("Food Item: " + fil.getName());
            }
        }
        //add ingredients to the new recipe
        temp.setFoodItemIds(foodIdList);

        //get drink categories
        String categoryDir = "category";
        String categorySubDir = "all";
        HttpResponse<String> categoryResponse = requestBuilder.twoDirGet(categoryDir,categorySubDir,"", "", client);
        Type categoryListType = new TypeToken<List<Category>>() {}.getType();
        List<Category> categoryList = gson.fromJson(categoryResponse.body(), categoryListType);
        for (Category c : categoryList) {
            System.out.println("Category: " + c.getName());
        }

        Long drinkCatId = getCategory(categoryList, scan);
        temp.setCategoryId(drinkCatId);

        return temp;
    }

    @Override
    public boolean isComplete(Scanner scan) {
        boolean comp = false;
        boolean valid = false;
        while (!valid) {
            System.out.println("Would you like to add another item? (Y)es or (N)");
            String temp = scan.nextLine();
            if(temp.equalsIgnoreCase("No") | temp.equalsIgnoreCase("N")) {
                comp = true;
                valid = true;
            } else if (temp.equalsIgnoreCase("Yes") | temp.equalsIgnoreCase("y")) {
                valid = true;
            }
        }
        return comp;
    }

    @Override
    public boolean isCompleteRemove(Scanner scan) {
        boolean comp = false;
        boolean valid = false;
        while (!valid) {
            System.out.println("Would you like to remove another item? (Y)es or (N)");
            String temp = scan.nextLine();
            if(temp.equalsIgnoreCase("No") | temp.equalsIgnoreCase("N")) {
                comp = true;
                valid = true;
            } else if (temp.equalsIgnoreCase("Yes") | temp.equalsIgnoreCase("y")) {
                valid = true;
            }
        }
        return comp;
    }

    @Override
    public boolean isCompleteEdit(Scanner scan) {
        boolean comp = false;
        boolean valid = false;
        while (!valid) {
            System.out.println("Would you like to edit anything else with this drink? (Y)es or (N)");
            String temp = scan.nextLine();
            if(temp.equalsIgnoreCase("No") | temp.equalsIgnoreCase("N")) {
                comp = true;
                valid = true;
            } else if (temp.equalsIgnoreCase("Yes") | temp.equalsIgnoreCase("y")) {
                valid = true;
            }
        }
        return comp;
    }


    private Long getCategory(List<Category> categoryList, Scanner scan) {
        Category temp = new Category();
        while (temp.getName() == null) {
            System.out.println("Please enter the name of the category that you would like to use from the list.");
            String ing = scan.nextLine();
            boolean nameMatch = false;
            for (Category i : categoryList) {
                if (ing.equalsIgnoreCase(i.getName())) {
                    temp = i;
                    System.out.println("Adding " + i.getName());
                    break;
                }
            }
        }
        return temp.getCategoryId();
    }
    @Override
    public Ingredient getIngredient(List<Ingredient> ingredientList, Scanner scan) {
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

    private FoodItem getFoodItem(List<FoodItem> foodList, Scanner scan) {
        FoodItem temp = new FoodItem();
        while (temp.getName() == null) {
            System.out.println("Please enter the name of the food that pairs nicely with our concoction from the list.");
            String ing = scan.nextLine();
            boolean nameMatch = false;
            for (FoodItem i : foodList) {
                if (ing.equalsIgnoreCase(i.getName())) {
                    temp = i;
                    System.out.println("Adding " + i.getName());
                    break;
                }
            }
        }
        return temp;
    }

    @Override
    public List<Uom> getAllUom(RequestBuilder requestBuilder, HttpClient client, Gson gson) throws IOException, InterruptedException {
        String ingredientDir = "uom";
        String ingredientSubDir = "all";
        HttpResponse<String> response = requestBuilder.twoDirGet(ingredientDir,ingredientSubDir,"", "", client);
        Type uomListType = new TypeToken<List<Uom>>() {}.getType();
        List<Uom> uomList = gson.fromJson(response.body(), uomListType);
        for (Uom u : uomList) {
            System.out.println("Unit of Measure: " + u.getName());
        }
        String jsonDrinks = gson.toJson(uomList);
        return uomList;
    }

    @Override
    public Uom getUom(List<Uom> ingredientList, Scanner scan) {
        Uom temp = new Uom();
        while (temp.getName()== null) {
            System.out.println("Please enter the name of the unit of measure that you would like to use from the list.");
            String ing = scan.nextLine();

            boolean nameMatch = false;
            for (Uom u : ingredientList) {
                if (ing.equalsIgnoreCase(u.getName())) {
                    temp = u;
                    System.out.println("Adding " + u.getName());
                    break;
                }
            }
        }
        return temp;
    }

    private String getUserInput(Scanner scan) {
        String temp = scan.nextLine();
        if (temp.isEmpty()) {
            System.out.println("I'm sorry, I didn't quite get that. Lets try again, please re enter your input.");
            getUserInput(scan);
        }
        return temp;
    }
    @Override
    public double getUserInputDouble(Scanner scan) {
        String temp = scan.nextLine();
        double tempD = 0;
        if (temp.isEmpty()) {
            System.out.println("I'm sorry, I didn't quite get that. Lets try again, please re enter your input.");
            getUserInput(scan);
        }
        try {
            tempD = Double.parseDouble(temp);
        } catch (NumberFormatException nfe) {
            System.out.println("I'm sorry please enter a legal number. ");
            getUserInputDouble(scan);
        }
        return tempD;
    }

    private boolean getDrinkTemp(Scanner scan) {
        boolean hot = false;
        boolean tempa = false;
        while (!tempa) {
            System.out.println("Is this drink hot or cold, Please enter either hot, cold, h for hot or c for cold");
            String userInput = getUserInput(scan);
            if (userInput.equalsIgnoreCase("hot") | userInput.equalsIgnoreCase("h")) {
                hot = true;
                tempa = true;
            } else if (userInput.equalsIgnoreCase("cold") | userInput.equalsIgnoreCase("c")) {
                tempa = true;
            } else {
                System.out.println("I'm sorry please enter either hot, cold, h, or c.");
            }
        }
        return hot;
    }

    protected List<DrinkIngredient> getDrinkIngredients(Scanner scan) {
        return null;
    }
}
