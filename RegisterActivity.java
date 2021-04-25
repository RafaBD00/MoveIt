package com.example.movet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

// This is a register window that allows users to make new accounts.

public class RegisterActivity extends AppCompatActivity {
    private ActiveUserData activeUserData = ActiveUserData.getInstance();
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmed;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private String gender;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmed = (EditText) findViewById(R.id.editTextConfirm);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        Spinner spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString(); }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});
    }

    // Checks if info is given correctly and password fulfills requirements
    // and then adds them to a SharedPreference "UserInfo".

    public void onRegister(View view) {
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirm = editTextConfirmed.getText().toString();
        String weight = editTextWeight.getText().toString();
        String height = editTextHeight.getText().toString();
        if (name.equals("")|password.equals("")|confirm.equals("")|weight.equals("")|height.equals("")) {
            Toast.makeText(this, "Some fields wasn't filled!",
                    Toast.LENGTH_LONG).show();
        } else {

            // Checks if password fulfills requirements. Booleans keep track of which requirements
            // are fulfilled and which aren't. If some requirements aren't fulfilled, a toast message will be shown on the screen.

            boolean number = false;
            boolean big = false;
            boolean small = false;
            boolean special = false;
            boolean notUsed = false;
            if (password.equals(confirm)) {
                if (password.length() >= 12) {
                    char[] check = password.toCharArray();
                    for (char c : check) {
                        if (!number) {
                            if (Character.isDigit(c)) {
                                number = true; } }
                        if (!big) {
                            if (c >= 'A' && c <= 'Z') {
                                big = true; } }
                        if (!small) {
                            if (c >= 'a' && c <= 'z') {
                                small = true; } }
                        if (!special) {
                            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                                special = true; } }
                        if (special && big && number && small) {
                            break; } }
                } else {
                    editTextPassword.setText("");
                    editTextConfirmed.setText("");
                    Toast.makeText(this, "Password is too short!",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                editTextPassword.setText("");
                editTextConfirmed.setText("");
                Toast.makeText(this, "Passwords doesn't match!",
                        Toast.LENGTH_LONG).show();
            }
            if (!(big && small && special && number)) {
                editTextPassword.setText("");
                editTextConfirmed.setText("");
                Toast.makeText(this, "Passwords doesn't fulfill given requirements!",
                        Toast.LENGTH_LONG).show();
                
                // Checks if the username is already used.
                
            } else if (!(notUsed = checkIfTaken(username))) {
                editTextUsername.setText("");
                Toast.makeText(this, "Username already used!", Toast.LENGTH_LONG).show();
            }
            if (notUsed && small && big && special && number) {
                String newPassword = encryptPassword(password);
                storeData(name, username, newPassword, weight, height, gender);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    // This method takes users password as parameter and returns new encrypted password.

    public static String encryptPassword(String oldPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] password = messageDigest.digest(oldPassword.getBytes());
            BigInteger no = new BigInteger(1, password);
            String newPassword = no.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // This method stores user's information to SharedPreferences called UserInfo. It also contains
    // a total number of accounts created.

    public void storeData(String name, String username, String password, String weight, String height, String gender) {
        // Checks how many accounts have been created.
        SharedPreferences preferences = getSharedPreferences("UserInfo", 0);
        String amount = preferences.getString("AmountOfUsers", "");
        if (amount.equals("")) {
            amount = "1";
        } else {
            int number = Integer.parseInt(amount);
            number += 1;
            amount = String.valueOf(number);
        }

        // Every user has a different number after it's data in the "UserInfo". For example
        // password:5 and username:5 are info given by the fifth user.

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name:" + amount, name);
        editor.putString("Username:" + amount, username);
        editor.putString("Password:" + amount, password);
        editor.putString("Weight:" + amount, weight);
        editor.putString("Height:" + amount, height);
        editor.putString("Gender:" + amount, gender);
        editor.putString("AmountOfUsers", amount);
        editor.apply();

        // Adds the users weight also to "WeightStorage" where will be all of the users weights

        activeUserData.addWeight(this, weight);
    }

    // This method reads user data file and checks if the username that the user gave
    // is already used. Returns true if username is free to use and false if it's already taken.

    public boolean checkIfTaken(String username) {
        boolean result = true;
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 0);
        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> value : keys.entrySet()) {
            if (value.getValue().equals(username)) {
                String key = value.getKey();
                String[] arrKey = key.split(":", 2);
                if (arrKey[0].equals("Username")) {
                    result = false;
                }
            }
        }
        return result;
    }
}
