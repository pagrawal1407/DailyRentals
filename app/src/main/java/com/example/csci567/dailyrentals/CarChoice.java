package com.example.csci567.dailyrentals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import utility.DataPOJO;

public class CarChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_choice);

        String jsonmyobject = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            jsonmyobject = bundle.getString("DataPOJO object");
        }
        DataPOJO data = new Gson().fromJson(jsonmyobject,DataPOJO.class);
        TextView textView = (TextView) findViewById(R.id.datapojo_display);
        textView.setText(data.make + " " + data.model);

    }
}
