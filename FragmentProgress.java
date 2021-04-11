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
    private ActiveUserData activeUserData = ActiveUserData.getInstance();
    private EditText editTextAddWeight;
    private TextView textViewBMI;
    private TextView textViewNormalBMI;
    private BarChart barChart;
    private ArrayList<BarEntry> barEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        textViewBMI = v.findViewById(R.id.textViewBMI);
        textViewNormalBMI = v.findViewById(R.id.textViewNormalBMI);
        editTextAddWeight = (EditText) v.findViewById(R.id.editTextAddWeight);
        barChart = (BarChart) v.findViewById(R.id.barChart);
        createChart(activeUserData.getUsername());
        Button buttonUpdate = (Button) v.findViewById(R.id.buttonUpdateWeight);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the users current weight. Adds it to the ActiveUserData as the current weight
                // and also adds it to "WeightStorage".
                String newWeight = editTextAddWeight.getText().toString();
                editTextAddWeight.setText("");
                activeUserData.addWeight(Objects.requireNonNull(getContext()), newWeight);
                // Also updates the graph in progress window
                createChart(activeUserData.getUsername());
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

    // Creates the BarChart that displays the users weight changes

    public void createChart(String username) {
        barChart.invalidate();
        barChart.clear();
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(
                "WeightStorage", 0);
        String allWeights = sharedPreferences.getString(username, "");
        String[] arrWeights = allWeights.split(";", 21);
        barEntries = new ArrayList<>();
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
