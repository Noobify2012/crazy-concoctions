package com.concoctions.menus;

import com.concoctions.model.Drink;

import java.util.List;
import java.util.Scanner;

public interface DrinkMenuInt {

    void getDrinkSearch(Scanner scan);

    List<Drink> getAllDrinks(String dir);

    List<Drink> getDrinksByName(String dir);

    List<Drink> getDrinksByID(String dir);

    List<Drink> getDrinksByCategory(String dir);
}
