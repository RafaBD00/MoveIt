package com.example.movet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class FragmentWorkout extends Fragment {
    private EditText editTextWorkout;

    // This page is a workout page that allows the user to read and update their workouts.

    @Nullable
    @Override
    //This function creates the workoutpage where the user can make his/her workouts.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);
        editTextWorkout = (EditText) v.findViewById(R.id.editTextWorkout);
        // Put's all the workouts to the editText for the user to read.
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                "WorkoutPlans", Context.MODE_PRIVATE);
        String work = sharedPreferences.getString("Workouts", "Workouts");
        editTextWorkout.setText(work);
        // Saves the changes made by the user automatically to the file.
        editTextWorkout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String workouts = editTextWorkout.getText().toString();
                SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                        "WorkoutPlans", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Workouts", workouts);
                editor.apply();
            }
        });
        return v;
    }
}
