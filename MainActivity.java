package com.example.movet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private final ActiveUserData activeUserData = ActiveUserData.getInstance();

    // Opens home fragment by default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Adds the users name and username to the navigation view header

        View headerView = navigationView.getHeaderView(0);
        TextView textViewHeaderName = (TextView) headerView.findViewById(R.id.textViewHeadlineName);
        TextView textViewHeaderUsername = (TextView) headerView.findViewById(R.id.textViewHeadlineUsername);
        textViewHeaderName.setText(activeUserData.getName());
        textViewHeaderUsername.setText(activeUserData.getUsername());
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            SharedPreferences preferences = getSharedPreferences("LastOpenWindow", 0);
            String lastOpened = preferences.getString(activeUserData.getUsername(), "nav_progress");

            // Here the program opens the window that was the last one open before the app was closed

            switch (lastOpened) {
                case "nav_home":
                    navigationView.setCheckedItem(R.id.nav_home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentHome()).commit();
                    break;
                case "nav_workout":
                    navigationView.setCheckedItem(R.id.nav_workout);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentWorkout()).commit();
                    break;
                case "nav_notes":
                    navigationView.setCheckedItem(R.id.nav_notes);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentNotes()).commit();
                    break;
                case "nav_diet":
                    navigationView.setCheckedItem(R.id.nav_diet);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentDiets()).commit();
                    break;
                case "nav_food":
                    navigationView.setCheckedItem(R.id.nav_food);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentFood()).commit();
                    break;
                case "nav_progress":
                    navigationView.setCheckedItem(R.id.nav_progress);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FragmentProgress()).commit();
                    break;


            }
        }
    }

    // Checks what window will be opened when some of the buttons is pressed in the navigation view

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("LastOpenWindow", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Here the program keeps track of which window is the latest on to be opened

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome()).commit();
                editor.putString(activeUserData.getUsername(), "nav_home");
                editor.apply();
                break;
            case R.id.nav_notes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentNotes()).commit();
                editor.putString(activeUserData.getUsername(), "nav_notes");
                editor.apply();
                break;
            case R.id.nav_diet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDiets()).commit();
                editor.putString(activeUserData.getUsername(), "nav_diet");
                editor.apply();
                break;
            case R.id.nav_workout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentWorkout()).commit();
                editor.putString(activeUserData.getUsername(), "nav_workout");
                editor.apply();
                break;
            case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentProgress()).commit();
                editor.putString(activeUserData.getUsername(), "nav_progress");
                editor.apply();
                break;
            case R.id.nav_food:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentFood()).commit();
                editor.putString(activeUserData.getUsername(), "nav_food");
                editor.apply();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Keeps track of the navigation views state and opens or closes it accordingly

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
