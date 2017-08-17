package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import utility.CustomAddress;

public class DetailsActivity extends AppCompatActivity {

    private Button next;
    private EditText licensePlateNumber, carDescription, drivingLicenseNumberText, drivingLicenseCountryText, licenseDOB;
    private Bundle bundle;
    private Spinner licensePlateState, drivingLicenseStateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundle = getIntent().getExtras();

        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(this, R.array.statesArray, R.layout.spinner_layout);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        licensePlateState = (Spinner) findViewById(R.id.acceptLicensePlateState);
        licensePlateState.setAdapter(adapterState);

        drivingLicenseStateText = (Spinner) findViewById(R.id.drivingLicenseState);
        drivingLicenseStateText.setAdapter(adapterState);

        initializeEditText();
        onNextPressed();
    }

    private void onNextPressed() {

        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String licensePlateNumberText, carDescriptionText, licenseNumberText, licenseStateText, licenseCountryText, licenseDOBText;
                licensePlateNumberText = licensePlateNumber.getText().toString();
               // licensePlateStateText = licensePlateState.getText().toString();
                carDescriptionText = carDescription.getText().toString();
                licenseNumberText = drivingLicenseNumberText.getText().toString();
               // licenseStateText = drivingLicenseStateText.getText().toString();
                licenseCountryText = drivingLicenseCountryText.getText().toString();
                licenseDOBText = licenseDOB.getText().toString();

                final String[] selectedPlateState = new String[1];
                licensePlateState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedPlateState[0] = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final String[] selectedLicenseState = new String[1];
                drivingLicenseStateText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedLicenseState[0] = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                String formattedDate = "";
                if(!Objects.equals(licenseDOBText, "")) {
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
                double lat = 0;
                double lgn = 0;
                Address add = null;
                if (list != null) {
                    add = list.get(0);
                    lat = add.getLatitude();
                    lgn = add.getLongitude();
                    latitude = Double.toString(lat);
                    longitude = Double.toString(lgn);
                }

                JSONObject location = new JSONObject();
                JSONObject address = new JSONObject();
                try {
                    location.put("latitude",latitude);
                    location.put("longitude", longitude);
                    address.put("geoLocation", location);
                    address.put("city", bundle.getString("city"));
                    address.put("street1",bundle.getString("address"));
                    address.put("zipcode",bundle.getString("zipcode"));
                    address.put("state", bundle.getString("state"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                CustomAddress address = new CustomAddress();
                address.street1 = bundle.getString("address");
                address.City = bundle.getString("city");
                address.State = bundle.getString("state");
                Location location = new Location("");
                location.setLatitude(lat);
                location.setLongitude(lgn);
                address.geoLocation = location;*/
/*
                String imagePath = bundle.getString("imagePath");
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
*/
                Toast.makeText(getApplicationContext(), licensePlateNumberText,Toast.LENGTH_SHORT).show();
                Map<String,Object> jsonparams = new HashMap<String, Object>();
                jsonparams.put("licensePlateNum",licensePlateNumberText);
                jsonparams.put("licenseState",selectedPlateState[0]);
                jsonparams.put("carDes", carDescriptionText);
                jsonparams.put("licenseNum", licenseNumberText);
                jsonparams.put("issuingState", selectedLicenseState[0]);
                jsonparams.put("issuingCountry", licenseCountryText);
                jsonparams.put("fNameOnLic", bundle.getString("fName"));
                jsonparams.put("lNameOnLic", bundle.getString("lName"));
                jsonparams.put("gps", bundle.getString("gps"));
                jsonparams.put("hybrid", bundle.getString("hybrid"));
                jsonparams.put("bluetooth", bundle.getString("bluetooth"));
                jsonparams.put("petFriendly", bundle.getString("petFriendly"));
                jsonparams.put("audioPlayer", bundle.getString("audioPlayer"));
                jsonparams.put("sunRoof", bundle.getString("sunRoof"));
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
                jsonparams.put("address", address);
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
                Bitmap compressedImageFile = null;
                try {
                    compressedImageFile = new Compressor(DetailsActivity.this).compressToBitmap(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String content_type = getMimeType(selectedPath);

                /*Bitmap bmp = BitmapFactory.decodeFile(selectedPath);
                int height = bmp.getHeight();
                int width= bmp.getWidth();
                Bitmap resized = bmp;
                if (width > 4096) {
                   resized  = Bitmap.createScaledBitmap(bmp, 4050, height, true);
                }
                if (height > 4096){
                    int newWidth = resized.getWidth();
                    resized  = Bitmap.createScaledBitmap(bmp, newWidth, 4050, true);
                }*/
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                if (compressedImageFile != null) {
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }


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
        carDescription = (EditText) findViewById(R.id.acceptCarDescription);
        drivingLicenseNumberText = (EditText) findViewById(R.id.drivingLicenseNumber);
        drivingLicenseCountryText = (EditText) findViewById(R.id.drivingLicenseCountry);
        licenseDOB = (EditText) findViewById(R.id.drivingLicenseDOB);
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
