package com.concoctions;

import com.concoctions.model.NewDrink;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public interface Request {

    HttpResponse<String> singleDirGet(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> twoDirGet(String dir, String subDir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> singleDirPost(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> twoDirPost(String dir, String subDir, String query, String userString, HttpClient client, Gson gson) throws IOException, InterruptedException;

    HttpResponse<String> removeDrinkDelete(String dir, String subDir, Long DID, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> updateDrinkPut(String dir, String subDir, NewDrink object, Long DID, HttpClient client, Gson gson) throws IOException, InterruptedException;

    String userInput2URL(String string);
}
