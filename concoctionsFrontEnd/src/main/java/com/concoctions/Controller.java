package com.concoctions;

import java.io.IOException;

public interface Controller {
    void start() throws IOException, InterruptedException;
    void login() throws IOException, InterruptedException;

    void buildNewRecipe();

    void removeRecipe();

    void getDrinks();



}
