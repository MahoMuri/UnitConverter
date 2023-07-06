package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    HashMap<String, UnitConverters> conversionsMap = new HashMap<>();

    // Declare spinner
    private Spinner conversionSpinner;
    // Unit labels on the side of the numbers
    private TextView unitTop;
    private TextView unitBottom;

    // Unit values
    private EditText input;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unitTop = findViewById(R.id.unitTop);
        unitBottom = findViewById(R.id.unitBottom);
        input = findViewById(R.id.numberInputTop);
        result = findViewById(R.id.numberInputBottom);

        // Put the methods of the conversion map into a hashmap

        // kg → lbs and lbs → kg
        conversionsMap.put("kg → lbs", (value -> value / 0.45359237));
        conversionsMap.put("lbs → kg", (value -> value * 0.45359237));

        // ft → in and in → ft
        conversionsMap.put("ft → in", (value -> value * 12));
        conversionsMap.put("in → ft", (value -> value / 12));

        // in → cm and cm → in
        conversionsMap.put("in → cm", (value -> value * 2.54));
        conversionsMap.put("cm → in", (value -> value / 2.54));

        // C° → F° and F° → C°
        conversionsMap.put("C° → F°", (value -> value * 1.8 + 32));
        conversionsMap.put("F° → C°", (value -> (value - 32) / 1.8));

        // You can put more here :>

        // Take the spinner and apply the item selected listener
        conversionSpinner = findViewById(R.id.spinner);
        conversionSpinner.setOnItemSelectedListener(this);

        // Get the keys of the HashMap as an array and create an adapter to set to the spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conversionsMap.keySet().toArray(new String[0]));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conversionSpinner.setAdapter(arrayAdapter);
    }


    /**
     * Switches the conversion values from each other.
     *
     * @param view The view that called this function
     */
    public void switchUnits(View view) {
        // Switch the labels on the right
        String bottom = unitBottom.getText().toString();
        String top = unitTop.getText().toString();
        unitTop.setText(bottom);
        unitBottom.setText(top);

        // Switch the values
        String reversedConversion = bottom + " → " + top;
        String[] conversionKeys = conversionsMap.keySet().toArray(new String[0]);
        conversionSpinner.setSelection(Arrays.asList(conversionKeys).indexOf(reversedConversion));

        // Reset the values
        input.setText("");
        result.setText("");
    }

    /**
     * Converts the given value to the selected conversion
     *
     * @param view The view that called this function
     */
    public void convertUnits(View view) {
        if (input.getText().toString().equals("")) {
            Toast.makeText(this, "Value cannot be null!", Toast.LENGTH_LONG).show();
            return;
        }

        String conversion = conversionSpinner.getSelectedItem().toString();

        double toConvert = Double.parseDouble(input.getText().toString());

        double converted = Objects.requireNonNull(conversionsMap.get(conversion)).convert(toConvert);

        result.setText(String.format(Locale.US, "%.2f", converted));

    }

    // Override methods for OnItemSelectedListener implementation
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] selectedItems = adapterView.getSelectedItem().toString().split(" → ");
        unitTop.setText(selectedItems[0]);
        unitBottom.setText(selectedItems[1]);

        // Reset the values
        input.setText("");
        result.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto-generated override method
    }

}