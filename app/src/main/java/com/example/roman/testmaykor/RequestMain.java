package com.example.roman.testmaykor;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestMain {

    Boolean fetch(String baseURL, String Username, String password){
        HttpURLConnection connection = null;
        final String basicAuth = "Basic " + Base64.encodeToString((Username + ":" + password).getBytes(), Base64.NO_WRAP);

        try {
            connection = (HttpURLConnection) new URL(baseURL).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.setRequestProperty ("Authorization", basicAuth);

            connection.connect();
            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
