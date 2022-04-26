package com.concoctions;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.net.http.HttpClient.newHttpClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException, JSONException {
        java.net.http.HttpClient client = newHttpClient();
        System.out.println( "Welcome to Concoctions!" );
        Scanner in = new Scanner((System.in));
        Readable inputs = new InputStreamReader(System.in);
        Appendable output = System.out;
        new ConsoleController(inputs, output, client).start();
    }
}
