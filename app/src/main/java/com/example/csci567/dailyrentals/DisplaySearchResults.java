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
        String URL;
        Log.d("Search query","Query: " + query);
        if (query.equals("") ) {
            URL = "http://45.79.76.22:9080/EasyRentals/car/getCarList/" + query;
        }
        else
            URL = "http://45.79.76.22:9080/EasyRentals/car/getCarList";
        final TextView nullServerData = (TextView) findViewById(R.id.serverData);

        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONArray response) {
                Log.i("response data", response.toString());
                final ArrayList<DataPOJO> list = new ArrayList<>();
                try {
                    JSONArray jArray = response;
                    Log.i("jArray data", jArray.toString());
                    for (int i = 0; i < jArray.length(); i++){
                        JSONObject singleItem = jArray.getJSONObject(i);
                        DataPOJO data = new DataPOJO();
                        data.zipcode = singleItem.optInt("zipcode");
                        data.latitude = singleItem.optDouble("latitude");
                        data.longitude = singleItem.optDouble("longitude");
                        data.year = singleItem.optInt("year");
                        data.make = singleItem.optString("make");
                        data.model = singleItem.optString("model");
                        data.transmission = singleItem.optString("transmission");
                        data.odometer = singleItem.optDouble("odometer");
                        data.style = singleItem.optString("style");
                        data.carDescription = singleItem.optString("carDes");
                        data.advanceNotice = singleItem.optString("advNotice");
                        data.trim = singleItem.optString("trim");
                        //data.image = singleItem.optString("carPic");
                        //data.licenseNumber = singleItem.optString("licenseNo");
                        //data.issuingCountry = singleItem.optString("issuingCountry");
                        //data.issuingState = singleItem.optString("issuingState");
                        //data.licensePlateNumber = singleItem.optString("licensePlateNum");
                        //data.licenseState = singleItem.optString("licenseState");
                        //data.lName = singleItem.optString("lNameOnLic");
                        //data.fName = singleItem.optString("fNameOnLic");
                        data.minimumDuration = singleItem.optString("shortPT");
                        data.longestDistance = singleItem.optString("longPT");
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error in request: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(postRequest);
    }
}
