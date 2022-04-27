package com.concoctions;

import org.json.JSONException;

import java.io.IOException;

public interface Controller {
    void start() throws IOException, InterruptedException, JSONException;
    void login() throws IOException, InterruptedException, JSONException;

    void createProfile();

    void buildNewRecipe();

    void removeRecipe();

    void getDrinks();

    void editRecipe();

    void readComments();

    void leaveComments();



}
