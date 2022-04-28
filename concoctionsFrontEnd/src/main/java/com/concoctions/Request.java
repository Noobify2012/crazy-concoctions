package com.concoctions;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public interface Request {

    HttpResponse<String> singleDirGet(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> twoDirGet(String dir, String subDir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> singleDirPost(String dir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    HttpResponse<String> twoDirPost(String dir, String subDir, String query, String userString, HttpClient client) throws IOException, InterruptedException;

    String userInput2URL(String string);
}
