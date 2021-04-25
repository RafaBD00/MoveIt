
package com.example.movet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentProgress extends Fragment {
    private final ActiveUserData activeUserData = ActiveUserData.getInstance();
    private EditText editTextAddWeight;
    private EditText editTextUpdateWeight;
    private TextView textViewBMI;
    private TextView textViewNormalBMI;
    private BarChart barChart;

    // This page shows how the users weight has changed, allows the user to update their weight and height and also
    // counts the users BMI.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        textViewBMI = v.findViewById(R.id.textViewBMI);
        textViewNormalBMI = v.findViewById(R.id.textViewNormalBMI);
        editTextAddWeight = (EditText) v.findViewById(R.id.editTextAddWeight);
        editTextUpdateWeight = (EditText) v.findViewById(R.id.editTextUpdateHeight);
        barChart = (BarChart) v.findViewById(R.id.barChart);
        createChart();
        Button buttonUpdate = (Button) v.findViewById(R.id.buttonUpdateWeight);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the users current weight. Adds it to the ActiveUserData as the current weight
                // and also adds it to the "WeightStorage".
                String newWeight = editTextAddWeight.getText().toString();
                editTextAddWeight.setText("");
                boolean result = true;
                
                // Checks that the given weight is not empty abd it contains only numbers
                
                if (newWeight.equals("")) {
                    result = false;
                }
                char[] weightArray = newWeight.toCharArray();
                for (char c : weightArray) {
                    if (!Character.isDigit(c)) {
                        result = false;
                    }
                }
                if (result) {
                    activeUserData.addWeight(Objects.requireNonNull(getContext()), newWeight);
                    // Also updates the graph in progress window
                    createChart();
                } else {
                    Toast.makeText(getContext(),"Weight field was empty",Toast.LENGTH_LONG).show();
                }
            }});
        Button buttonUpdateHeight = (Button) v.findViewById(R.id.buttonUpdateHeight);
        buttonUpdateHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the users height. Adds the height to ActiveUserData as current height.
                String height = editTextUpdateWeight.getText().toString();
                editTextUpdateWeight.setText("");
                
                // Checks that the given height is not empty and it contains only numbers
                
                boolean ifNumber = true;
                if (height.equals("")) {
                    ifNumber = false;
                }
                char[] heightArray = height.toCharArray();
                for (char s : heightArray) {
                    if (!Character.isDigit(s)) {
                        ifNumber = false;
                    }
                }
                if (ifNumber) {
                    activeUserData.setHeight(height);
                    activeUserData.changeHeight(Objects.requireNonNull(getContext()), height);
                } else {
                    Toast.makeText(getContext(),"Height field was empty",Toast.LENGTH_LONG).show();
                }
            }});
        Button buttonBMI = (Button) v.findViewById(R.id.buttonBMI);
        buttonBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Counts the users BMI and displays it to the textViews
                double weight = activeUserData.getWeight();
                double height = activeUserData.getHeight();
                height = height / 100.0;
                double bmi = ((weight) / (Math.pow(height, 2.0)));
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                bmi = Double.parseDouble(decimalFormat.format(bmi));
                textViewBMI.setText("Your BMI is: " + bmi);
                textViewNormalBMI.setText("18,5 - 24,9 is considered normal");
            }});
        return v;
    }

    // Creates the BarChart that displays the users weight changes. The chart displays a max 20 values.

    public void createChart() {
        barChart.invalidate();
        barChart.clear();
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        String allWeights = activeUserData.getAllWeights(Objects.requireNonNull(getContext()));
        String[] arrWeights = allWeights.split(";", 21);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float value;
        for(int i = 0; i < arrWeights.length; i++){
            value = Float.parseFloat(arrWeights[i]);
            barEntries.add(new BarEntry(i, value));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        BarData barData = new BarData(barDataSet, barDataSet);
        barChart.setData(barData);
    }
}
