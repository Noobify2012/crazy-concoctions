package com.concoctions;

import org.json.JSONException;

import java.io.IOException;

public interface Controller {
    void start() throws IOException, InterruptedException, JSONException;
    void login() throws IOException, InterruptedException, JSONException;

    void createProfile();

    void buildNewRecipe() throws IOException, InterruptedException;

    void removeRecipe() throws IOException, InterruptedException;

    void getDrinks() throws IOException, InterruptedException;

    void editRecipe() throws IOException, InterruptedException;

    void readComments() throws IOException, InterruptedException;

    void leaveComments() throws IOException, InterruptedException;



}
