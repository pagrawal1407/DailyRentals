package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.framed.FrameReader;
import utility.DataPOJO;

public class CarChoice extends AppCompatActivity {
    private android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_choice);

        String jsonmyobject = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonmyobject = bundle.getString("DataPOJO object");
        }
        final DataPOJO data = new Gson().fromJson(jsonmyobject, DataPOJO.class);
        TextView textView = (TextView) findViewById(R.id.datapojo_display);

        //Toast.makeText(this,data.make + " " + data.model, Toast.LENGTH_SHORT).show();
        if (!data.model.equals("") && !data.make.equals(""))
            textView.setText(data.make + " " + data.model);

        TextView name = (TextView) findViewById(R.id.carChoiceName);

        //Toast.makeText(this,data.fName + " " + data.lName, Toast.LENGTH_SHORT).show();
        if (data.fName != ""  && data.lName != "")
            name.setText(data.fName + " " + data.lName);

        if (data.latitude != 0 && data.longitude != 0) {
            Geocoder gc = new Geocoder(getApplicationContext());
            List<Address> addList = null;
            try {
                addList = gc.getFromLocation(data.latitude, data.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address add = addList.get(0);
            String location = add.getLocality();
            TextView locationView = (TextView) findViewById(R.id.carChoiceLocation);
            locationView.setText(location);
        }

        TextView details = (TextView) findViewById(R.id.carChoiceDetails);

        if (data.transmission != ""  && data.odometer != 0)
            details.setText(data.transmission + " " + ((int) data.odometer) + " miles");

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String URL = "http://45.79.76.22:9080/EasyRentals/image/download";

        handler = new android.os.Handler();
        final Map<String,String> jsonparams = new HashMap<String, String>();
        Toast.makeText(getApplicationContext(), data.licensePlateNumber, Toast.LENGTH_LONG).show();
        jsonparams.put("fileName",data.licensePlateNumber);
        final String[] fileName = new String[1];
        final String[] image = new String[1];

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
               // RequestBody fileBody = RequestBody.create(MediaType.parse(content_type),bos.toByteArray());
                String URL = "http://45.79.76.22:9080/EasyRentals/image/download" + "?fileName="+data.licensePlateNumber;

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(URL)
                        .build();

                okhttp3.Response response = null;

                try {
                    response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    fileName[0] = jsonObject.getString("name");
                    image[0] = jsonObject.getString("data");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView imageName = (TextView) findViewById(R.id.carImageName);
                            imageName.setText(fileName[0]);

                            ImageView carImage = (ImageView) findViewById(R.id.car_image);
                            byte[] decodedImage = Base64.decode(image[0], Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                            carImage.setImageBitmap(decodedByte);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();

    }
}
