package com.concoctions.menus;

import com.concoctions.model.Drink;
import com.concoctions.model.NewDrink;
import com.concoctions.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Scanner;

public interface DrinkBuilderInt {

    NewDrink buildNewDrink(Scanner scan, Gson gson, User user, HttpClient client) throws IOException, InterruptedException;



}
