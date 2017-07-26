package com.example.csci567.dailyrentals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
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
        if (bundle != null) {
            jsonmyobject = bundle.getString("DataPOJO object");
        }
        DataPOJO data = new Gson().fromJson(jsonmyobject, DataPOJO.class);
        TextView textView = (TextView) findViewById(R.id.datapojo_display);

        if (!data.model.equals("") && !data.make.equals(""))
            textView.setText(data.make + " " + data.model);

        ImageView carImage = (ImageView) findViewById(R.id.car_image);
//        byte[] decodedImage = Base64.decode(data.image, Base64.DEFAULT);
  //      Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
    //    carImage.setImageBitmap(decodedByte);
    }
}
