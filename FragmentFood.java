package com.example.movet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentFood extends Fragment {
    private boolean emission;
    private String diet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        Spinner spinnerGender = (Spinner) v.findViewById(R.id.spinnerEmission);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.emission, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();
                emission = choice.equals("Yes");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});

        Spinner spinnerDiet = (Spinner) v.findViewById(R.id.spinnerDiet);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.diet, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(arrayAdapter);
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diet = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});

        TextView textViewResult = v.findViewById(R.id.textViewResult);
        Button buttonCalculate = v.findViewById(R.id.buttonCalculate);
        EditText editTextBeef = v.findViewById(R.id.editTextBeef);
        EditText editTextPork = v.findViewById(R.id.editTextPork);
        EditText editTextFish = v.findViewById(R.id.editTextFish);
        EditText editTextDairy = v.findViewById(R.id.editTextDairy);
        EditText editTextCheese = v.findViewById(R.id.editTextCheese);
        EditText editTextRice = v.findViewById(R.id.editTextRice);
        EditText editTextEggs = v.findViewById(R.id.editTextEggs);
        EditText editTextSalad = v.findViewById(R.id.editTextSalad);
        EditText editTextRestaurant = v.findViewById(R.id.editTextRestaurant);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beef = editTextBeef.getText().toString();
                
            }});
        return v;
    }
}

