package com.example.csci567.dailyrentals;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CarDetails extends AppCompatActivity {

    private Button next;
    private EditText address, zipCode, make, model, odometer, trim, style, fName, lName;
    private Spinner carYear, carTransmission, carStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        ArrayAdapter<CharSequence> adapterTransmission = ArrayAdapter.createFromResource(this, R.array.transmissionArray, R.layout.spinner_layout);
        adapterTransmission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carTransmission = (Spinner) findViewById(R.id.acceptTransmission);
        carTransmission.setAdapter(adapterTransmission);


        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2007; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_layout, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carYear = (Spinner) findViewById(R.id.acceptYear);
        carYear.setAdapter(adapterYear);

        ArrayAdapter<CharSequence> adapterStyle = ArrayAdapter.createFromResource(this, R.array.styleArray, R.layout.spinner_layout);
        adapterStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        carStyle = (Spinner) findViewById(R.id.acceptStyle);
        carStyle.setAdapter(adapterStyle);

        intitializeEditText();
        onNextPressed();
    }

    private void onNextPressed() {

        final String[] style = new String[1];
        style[0] = "";
        carStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                style[0] = carStyle.getSelectedItem().toString();
                // Toast.makeText(getApplicationContext(), style[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] selectedYear = new String[1];
        carYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] transmission = new String[1];
        carTransmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transmission[0] = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(), transmission[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressText, zipCodeText, yearText, makeText, modelText, transmissionText, odometerText, trimText, styleText, fNameText, lNameText;


                fNameText = fName.getText().toString();
                lNameText = lName.getText().toString();
                addressText = address.getText().toString();
                zipCodeText = zipCode.getText().toString();
                //yearText = year.getText().toString();
                makeText = make.getText().toString();
                modelText = model.getText().toString();
                //transmissionText = transmission.getText().toString();
                odometerText = odometer.getText().toString();
                trimText = trim.getText().toString();
                //styleText = style.getText().toString();

                Bundle bundle = new Bundle();

                Toast.makeText(getApplicationContext(), "year "+ selectedYear[0]+ " transmission "+ transmission[0]+ " style "+style[0],Toast.LENGTH_LONG).show();

                bundle.putString("fName", fNameText);
                bundle.putString("lName", lNameText);
                bundle.putString("address", addressText);
                bundle.putString("zipcode", zipCodeText);
                bundle.putString("year", selectedYear[0]);
                bundle.putString("make", makeText);
                bundle.putString("model", modelText);
                bundle.putString("transmission", transmission[0]);
                bundle.putString("odometer", odometerText);
                bundle.putString("trim", trimText);
                bundle.putString("style", style[0]);

                Intent intent = new Intent(getBaseContext(), AvailabilityActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void intitializeEditText() {
        fName = (EditText) findViewById(R.id.fName);
        lName = (EditText) findViewById(R.id.lName);
        address = (EditText) findViewById(R.id.acceptAddress);
        zipCode = (EditText) findViewById(R.id.acceptZipCode);
        make = (EditText) findViewById(R.id.acceptMake);
        model = (EditText) findViewById(R.id.acceptModel);
        odometer = (EditText) findViewById(R.id.acceptOdometer);
        trim = (EditText) findViewById(R.id.acceptTrim);
        //style = (EditText) findViewById(R.id.acceptStyle);
    }
}