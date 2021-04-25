
package com.example.movet;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentFood extends Fragment {
    private boolean emission;
    private String diet;
    
    // This page estimates the users yearly emission.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

        TextView textViewResult1 = v.findViewById(R.id.textViewResult1);
        TextView textViewResult2 = v.findViewById(R.id.textViewResult2);
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
        
        // After the button is pressed, it takes all the values in the editTexts and creates an URL. If some of the editTexts are empty or have too big values,
        // toast message is shown.
        
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beef = editTextBeef.getText().toString();
                String pork = editTextPork.getText().toString();
                String fish = editTextFish.getText().toString();
                String dairy = editTextDairy.getText().toString();
                String cheese = editTextCheese.getText().toString();
                String rice = editTextRice.getText().toString();
                String egg = editTextEggs.getText().toString();
                String salad = editTextSalad.getText().toString();
                String restaurant = editTextRestaurant.getText().toString();

                if (beef.equals("") || pork.equals("") || fish.equals("") ||
                        dairy.equals("") || cheese.equals("") || rice.equals("") || egg.equals("")
                        || salad.equals("") || restaurant.equals("")) {
                    Toast.makeText(getContext(), "Fill all the boxes!", Toast.LENGTH_LONG).show();
                } else if ((Double.parseDouble(beef) > 200.0) || (Double.parseDouble(pork) > 200.0) || (Double.parseDouble(fish) > 200.0)
                        || (Double.parseDouble(dairy) > 200.0) || (Double.parseDouble(cheese) > 200.0) || (Double.parseDouble(rice) > 200.0)
                        || (Double.parseDouble(egg) > 200.0) || (Double.parseDouble(salad) > 200.0) || (Double.parseDouble(restaurant) > 800.0)) {
                    Toast.makeText(getContext(), "Value can't be more than 200 hundred", Toast.LENGTH_LONG).show();
                }else{
                    String total = null;
                    try {
                        total = findTotal(diet, String.valueOf(emission), beef, pork, fish, dairy, cheese, rice, egg, salad, restaurant);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textViewResult1.setText("Your emission estimate is:");
                    textViewResult2.setText( total + " kg CO2 / year");
                }

            }});
        return v;
    }
    
    // This function takes all the values needed for the URL as parameters and creates it. Then reads the XML page and return the total emission estimate
    // rounded for 2 decimals.

    public String findTotal(String diet, String emission, String beef, String pork, String fish, String dairy, String cheese, String rice, String egg, String salad, String restaurant) throws IOException {
        String url = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator?query.diet="
                + diet + "&query.lowCarbonPreference=" + emission + "&query.beefLevel=" + beef + "&query.fishLevel="
                + fish + "&query.porkPoultryLevel=" + pork + "&query.dairyLevel=" +  dairy + "&query.cheeseLevel="
                + cheese + "&query.riceLevel=" + rice + "&query.eggLevel=" + egg + "&query.winterSaladLevel=" +
                salad + "&query.restaurantSpending=" + restaurant;
        URL page = new URL(url);
        Scanner sc = new Scanner(page.openStream());
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next());
        }
        
        //Retrieves the String from the String Buffer object
        
        String result = sb.toString();
        
        //Removes the HTML tags
        
        result = result.replaceAll("<[^>]*>", "");
        String[] arr = result.split(":", 6);
        String total = arr[5];
        
        // Removes the "}" at the end of the number
        
        total = total.replace("}", "");
        double absoluteTotal = Double.parseDouble(total);
        
        // Rounds the double.
        
        absoluteTotal = Math.round(absoluteTotal * 100.0) / 100.0;
        return(String.valueOf(absoluteTotal));
    }
}
