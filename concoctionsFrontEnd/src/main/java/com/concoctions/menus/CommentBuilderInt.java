package com.concoctions.menus;

import com.concoctions.model.Comment;
import com.concoctions.model.Drink;
import com.concoctions.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Scanner;

public interface CommentBuilderInt {

    void CommentMenu(User user, Scanner scan, HttpClient client, Gson gson) throws IOException, InterruptedException;

    List<Comment> getCommentsByUserID(Long UID, Scanner scan, HttpClient client, Gson gson);

    List<Comment> getCommentsByDrinkID(Long DID, Scanner scan, HttpClient client, Gson gson);

    void writeComment(User user, Drink drink, Scanner scan, HttpClient client, Gson gson);
}
