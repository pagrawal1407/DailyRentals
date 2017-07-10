package com.example.csci567.dailyrentals;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class CarDetails extends AppCompatActivity {

    private Button next;
    private EditText address, zipCode, year, make, model, transmission, odometer, trim, style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        intitializeEditText();
        onNextPressed();
    }

    private void intitializeEditText() {
        address = (EditText) findViewById(R.id.acceptAddress);
        zipCode = (EditText) findViewById(R.id.acceptZipCode);
        year = (EditText) findViewById(R.id.acceptYear);
        make = (EditText) findViewById(R.id.acceptMake);
        model = (EditText) findViewById(R.id.acceptModel);
        transmission = (EditText) findViewById(R.id.acceptTransmission);
        odometer = (EditText) findViewById(R.id.acceptOdometer);
        trim = (EditText) findViewById(R.id.acceptTrim);
        style = (EditText) findViewById(R.id.acceptStyle);
    }

    private void onNextPressed() {
        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressText, zipCodeText, yearText, makeText, modelText, transmissionText, odometerText, trimText, styleText;
                addressText = address.getText().toString();
                zipCodeText = zipCode.getText().toString();
                yearText = year.getText().toString();
                makeText = make.getText().toString();
                modelText = model.getText().toString();
                transmissionText = transmission.getText().toString();
                odometerText = odometer.getText().toString();
                trimText = trim.getText().toString();
                styleText = style.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("address",addressText);
                bundle.putString("zipcode",zipCodeText);
                bundle.putString("year",yearText);
                bundle.putString("make",makeText);
                bundle.putString("model",modelText);
                bundle.putString("transmission",transmissionText);
                bundle.putString("odometer",odometerText);
                bundle.putString("trim",trimText);
                bundle.putString("style",styleText);
                
                Intent intent = new Intent(getBaseContext(),AvailabilityActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
