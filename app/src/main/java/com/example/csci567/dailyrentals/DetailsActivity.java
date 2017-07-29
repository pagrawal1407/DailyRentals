package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.R.attr.data;

public class DetailsActivity extends AppCompatActivity {

    private Button next;
    private EditText licensePlateNumber, licensePlateState, carDescription, drivingLicenseNumberText, drivingLicenseStateText, drivingLicenseCountryText, licenseDOB;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();
        initializeEditText();
        onNextPressed();
    }

    private void onNextPressed() {

        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String licensePlateNumberText, licensePlateStateText, carDescriptionText, licenseNumberText, licenseStateText, licenseCountryText, licenseDOBText;
                licensePlateNumberText = licensePlateNumber.getText().toString();
                licensePlateStateText = licensePlateState.getText().toString();
                carDescriptionText = carDescription.getText().toString();
                licenseNumberText = drivingLicenseNumberText.getText().toString();
                licenseStateText = drivingLicenseStateText.getText().toString();
                licenseCountryText = drivingLicenseCountryText.getText().toString();
                licenseDOBText = licenseDOB.getText().toString();
                String formattedDate = "";
                if(licenseDOBText != "") {
                    String[] splitDOB = licenseDOBText.split("/");
                    formattedDate = splitDOB[2] + "-" + splitDOB[0] + "-" + splitDOB[1];
                }


                Geocoder gc = new Geocoder(getApplicationContext());
                List<Address> list = null;
                try {
                    list = gc.getFromLocationName(bundle.getString("address"), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String latitude = "";
                String longitude = "";
                Address add = null;
                if (list != null) {
                    add = list.get(0);
                    double lat = add.getLatitude();
                    double lgn = add.getLongitude();
                    latitude = Double.toString(lat);
                    longitude = Double.toString(lgn);
                }


/*
                String imagePath = bundle.getString("imagePath");
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
*/
                Toast.makeText(getApplicationContext(), licensePlateNumberText,Toast.LENGTH_SHORT).show();
                Map<String,String> jsonparams = new HashMap<String, String>();
                jsonparams.put("licensePlateNum",licensePlateNumberText);
                jsonparams.put("licenseState",licensePlateStateText);
                jsonparams.put("carDes", carDescriptionText);
                jsonparams.put("licenseNum", licenseNumberText);
                jsonparams.put("issuingState", licenseStateText);
                jsonparams.put("issuingCountry", licenseCountryText);
                jsonparams.put("fNameOnLic", bundle.getString("fName"));
                jsonparams.put("lNameOnLic", bundle.getString("lName"));
                jsonparams.put("dob", formattedDate);
                jsonparams.put("advNotice", bundle.getString("advancenotice"));
                jsonparams.put("shortPT", bundle.getString("shortesttrip"));
                jsonparams.put("longPT", bundle.getString("longesttrip"));
                jsonparams.put("latitude", latitude);
                jsonparams.put("longitude", longitude);
                jsonparams.put("zipcode", bundle.getString("zipcode"));
                jsonparams.put("year", bundle.getString("year"));
                jsonparams.put("make", bundle.getString("make"));
                jsonparams.put("model", bundle.getString("model"));
                jsonparams.put("transmission", bundle.getString("transmission"));
                jsonparams.put("odometer", bundle.getString("odometer"));
                jsonparams.put("trim", bundle.getString("trim"));
                jsonparams.put("style", bundle.getString("style"));
                volleycall(jsonparams);
                uploadCarImage(bundle.getString("imagePath"), licensePlateNumberText);
            }
        });
    }

    private void uploadCarImage(final String selectedPath, final String licensePlateNumber) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(selectedPath);
                String content_type = getMimeType(selectedPath);

                Bitmap bmp = BitmapFactory.decodeFile(selectedPath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, bos);


                OkHttpClient client = new OkHttpClient();
                RequestBody fileBody = RequestBody.create(MediaType.parse(content_type),bos.toByteArray());

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("fileName", licensePlateNumber)
                        .addFormDataPart("file", selectedPath.substring(selectedPath.lastIndexOf('/') + 1), fileBody)
                        .build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://45.79.76.22:9080/EasyRentals/image/upload")
                        .post(requestBody)
                        .build();


                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    if (!response.isSuccessful()){
                        throw new IOException("Error:"+response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    private void volleycall(Map jsonparams) {

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("JSON parameters","Json: " + new JSONObject(jsonparams));
        String URL = "http://45.79.76.22:9080/EasyRentals/car/listyourcar";

        Toast.makeText(DetailsActivity.this,"Sending data",Toast.LENGTH_SHORT).show();
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(jsonparams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response value", response.toString());
                        String msg = null;
                        try{
                            msg = (String) response.get("Value");
                            Log.i("Value of msg: ",msg);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if (msg.equals("Saved") ){
                            Intent intent = new Intent(getBaseContext(), WelcomePage.class);
                            Toast.makeText(DetailsActivity.this,"Success, data sent",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(DetailsActivity.this, "Data not sent. Look for error!", Toast.LENGTH_LONG).show();
                        }
                    }

                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e ("Error recieved","Error: " + error + "\n Message: " + error.getMessage());
            }
        }
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;

            }
        };
        queue.add(postRequest);
    }

    private void initializeEditText() {
        licensePlateNumber = (EditText) findViewById(R.id.acceptLicensePlate);
        licensePlateState = (EditText) findViewById(R.id.acceptLicensePlateState);
        carDescription = (EditText) findViewById(R.id.acceptCarDescription);
        drivingLicenseNumberText = (EditText) findViewById(R.id.drivingLicenseNumber);
        drivingLicenseStateText = (EditText) findViewById(R.id.drivingLicenseState);
        drivingLicenseCountryText = (EditText) findViewById(R.id.drivingLicenseCountry);
        licenseDOB = (EditText) findViewById(R.id.drivingLicenseDOB);
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
