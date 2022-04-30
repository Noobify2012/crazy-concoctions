package com.concoctions.menus;

import com.concoctions.RequestBuilder;
import com.concoctions.model.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Scanner;

public interface DrinkBuilderInt {

    NewDrink buildNewDrink(Scanner scan, Gson gson, User user, HttpClient client) throws IOException, InterruptedException;

    public List<Uom> getAllUom(RequestBuilder requestBuilder, HttpClient client, Gson gson) throws IOException, InterruptedException;

    Ingredient getIngredient(List<Ingredient> ingredientList, Scanner scan);

    public Uom getUom(List<Uom> ingredientList, Scanner scan);

    double getUserInputDouble(Scanner scan);

    boolean isComplete(Scanner scan);

    boolean isCompleteRemove(Scanner scan);



}
