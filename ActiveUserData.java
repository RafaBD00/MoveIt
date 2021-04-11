package com.example.movet;

// Singleton class that keeps track of the active user's data.

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ActiveUserData {
    private String name;
    private String username;
    private String hometown;
    private String allWeights;
    private double height;
    private double weight;
    private static ActiveUserData instance = null;
    private ActiveUserData() {}
    public static ActiveUserData getInstance() {
        if (instance == null) {
            instance = new ActiveUserData();
        }
        return instance;
    }

    // Get's and Set's for the private values above.

    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return this.username;}
    public void setHometown(String hometown) {this.hometown = hometown;}
    public String getHometown() {return this.hometown;}
    public void setHeight(String height) {this.height = Double.parseDouble(height);}
    public double getHeight() {return this.height;}
    public void setWeight(String weight) {this.weight = Double.parseDouble(weight);}
    public double getWeight() {return this.weight;}

    // Gets all the different weights the user has given

    public String getAllWeights(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("WeightStorage", 0);
        allWeights = preferences.getString(username, "");
        return allWeights;
    }

    // Adds new weight to the users file

    public void addWeight(Context context, String newWeight) {
        this.weight = Double.parseDouble(newWeight);
        String number = "";
        // Updates users new weight to the UserInfo file in SharedPreference
        SharedPreferences SPreferences = context.getSharedPreferences("UserInfo", 0);
        Map<String, ?> keys = SPreferences.getAll();
        for (Map.Entry<String, ?> value : keys.entrySet()) {
            if (value.getValue().equals(username)) {
                String key = value.getKey();
                String[] split = key.split(":", 2);
                number = split[1];
                }
            }
        SharedPreferences shared = context.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("Weight:" + number, newWeight);
        edit.apply();
        // Gets all already added values
        SharedPreferences preferences = context.getSharedPreferences("WeightStorage", 0);
        String oldWeights = preferences.getString(username, "");
        // Checks how many weights have been given
        String[] arrWeights = oldWeights.split(";", 21);
        int len = arrWeights.length;
        // Adds the new one after the old ones
        SharedPreferences sharedPreferences = context.getSharedPreferences("WeightStorage", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (oldWeights.equals("")) {
            editor.putString(username, newWeight);
        } else {
            // Checks if the user has given it's weight 20 times and removes the first so the
            // maximum amount of weights stays at 20 or under
            if (len >= 20) {
                String[] tempWeights = Arrays.copyOfRange(arrWeights, 1, arrWeights.length);
                oldWeights = "";
                for (int i = 0; i < tempWeights.length; i++) {
                    if (i == 0) {
                        oldWeights = tempWeights[i];
                    } else {
                        oldWeights = oldWeights + ";" + tempWeights[i];
                    }
                }
                editor.putString(username, oldWeights + ";" + newWeight);
            } else {
                editor.putString(username, oldWeights + ";" + newWeight);
            }
        }
        editor.apply();
    }
}

