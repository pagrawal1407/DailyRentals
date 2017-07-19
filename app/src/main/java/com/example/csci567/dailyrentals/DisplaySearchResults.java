package com.example.csci567.dailyrentals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import utility.DataPOJO;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplaySearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        Bundle bundle = getIntent().getExtras();
        final String query = bundle.getString("search_query");
        volleyCall(query);
    }

    private void volleyCall(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "http://45.79.76.22:9080/EasyRentals/car/getCarList/"+ query;
        final TextView nullServerData = (TextView) findViewById(R.id.serverData);
        Log.d("Search query","Query: " + query);

        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONArray response) {
                Log.i("response data", response.toString());
                if (response == null) {
                    nullServerData.setText("No cars found in your location");
                    nullServerData.setVisibility(View.VISIBLE);
                } else {
                    final ArrayList<DataPOJO> list = new ArrayList<DataPOJO>();
                    try {
                        JSONArray jArray = response;
                        Log.i("jArray data", jArray.toString());
                        for (int i = 0; i < jArray.length(); i++){
                            JSONObject singleItem = jArray.getJSONObject(i);
                            DataPOJO data = new DataPOJO();
                            data.zipcode = singleItem.getInt("zipcode");
                            data.latitude = singleItem.getDouble("latitude");
                            data.longitude = singleItem.getDouble("longitude");
                            data.location = singleItem.getString("location");
                            data.year = singleItem.getInt("year");
                            data.make = singleItem.getString("make");
                            data.model = singleItem.getString("model");
                            data.transmission = singleItem.getString("transmission");
                            data.odometer = singleItem.getDouble("odometer");
                            data.style = singleItem.getString("style");
                            data.carDescription = singleItem.getString("carDes");
                            data.advanceNotice = singleItem.getString("advNotice");
                            data.trim = singleItem.getString("trim");
                            data.image = singleItem.getString("image");
                            data.issuingCountry = singleItem.getString("issuing_Country");
                            //data.issuingState = singleItem.getString("issuing_State");
                            data.licenseNumber = singleItem.getString("licenseNum");
                            data.licenseState = singleItem.getString("licenseState");
                            //data.lName = singleItem.getString("lname");
                            //data.fName = singleItem.getString("fname");
                            data.minimumDuration = singleItem.getString("shortPT");
                            data.longestDistance = singleItem.getString("longPT");
                            list.add(data);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException: ", e.getMessage());
                        e.printStackTrace();
                    }
                    Log.i("Array list data:",list.toString());
                    ListView lst = (ListView) findViewById(R.id.car_list);
                    ListViewAdapter adapter = new ListViewAdapter(list,getApplicationContext());
                    lst.setAdapter(adapter);

                    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent carChoiceIntent = new Intent(getApplicationContext(), CarChoice.class);
                            carChoiceIntent.putExtra("DataPOJO object", new Gson().toJson(list.get(position)));
                            startActivity(carChoiceIntent);
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in request: "+ error.getMessage(), Toast.LENGTH_LONG);
            }
        });

        queue.add(postRequest);
    }
}
