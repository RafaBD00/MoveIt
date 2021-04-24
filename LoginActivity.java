import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private ActiveUserData activeUserData = ActiveUserData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    // Checks if given data is correct and opens main window.

    public void onLogin(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Encrypts given password with the same encryption that it was encrypted during
        // registrations and checks if they match.

        String testPassword = RegisterActivity.encryptPassword(password);

        if (checkIfMatch(username, testPassword)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            editTextPassword.setText("");
            Toast.makeText(getApplicationContext(),"Username and password doesn't match",Toast.LENGTH_LONG).show();
        }
    }

    // Finds the username from the file and compares passwords at that place. If passwords match
    // returns true and if not returns false.

    public boolean checkIfMatch(String username, String password) {
        boolean result = false;
        int i = 1;
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 0);
        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> value : keys.entrySet()) {
            if (value.getValue().equals(username)) {
                String key = value.getKey();
                String[] split = key.split(":", 2);
                String rightPassword = sharedPreferences.getString("Password:" + split[1], "");
                if (rightPassword.equals(password)) {
                    activeUserData.setName(sharedPreferences.getString("Name:" + split[1], ""));
                    activeUserData.setUsername(username);
                    activeUserData.setWeight(sharedPreferences.getString("Weight:" + split[1], ""));
                    activeUserData.setHeight(sharedPreferences.getString("Height:" + split[1], ""));
                    activeUserData.setCurrentUser(split[1]);
                    result = true;
                }
            }
        }
        return result;
    }

    // Opens Registering window for new users.

    public void onSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
