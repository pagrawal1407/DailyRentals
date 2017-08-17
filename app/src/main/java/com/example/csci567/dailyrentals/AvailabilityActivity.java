package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AvailabilityActivity extends AppCompatActivity {

    private Button next;
    private Spinner advanceNotice, shortestTrip, longestDist;
    private Bundle bundle;
    private EditText setLimit;
    private String limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        bundle = getIntent().getExtras();
        intializeSpinners();
        setLimit = (EditText) findViewById(R.id.setlimit);
        ArrayAdapter<CharSequence> adapteradvNotice = ArrayAdapter.createFromResource(this, R.array.advNoticeArray, R.layout.spinner_layout);
        adapteradvNotice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        advanceNotice.setAdapter(adapteradvNotice);

        ArrayAdapter<CharSequence> adaptershortestTrip = ArrayAdapter.createFromResource(this, R.array.shortestTripArray, R.layout.spinner_layout);
        adaptershortestTrip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shortestTrip.setAdapter(adaptershortestTrip);

        ArrayAdapter<CharSequence> adapterlongestDist = ArrayAdapter.createFromResource(this, R.array.longestDistanceArray, R.layout.spinner_layout);
        adapterlongestDist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        longestDist.setAdapter(adapterlongestDist);
        onNextPressed();
    }

    private void intializeSpinners() {
        advanceNotice = (Spinner) findViewById(R.id.acceptAdvanceNotice);
        shortestTrip = (Spinner) findViewById(R.id.acceptShortestTrip);
        longestDist = (Spinner) findViewById(R.id.acceptLongestTrip);
    }

    private void onNextPressed() {
        next = (Button) findViewById(R.id.nextButton1);

        final String[] setLimitText = {""};
        final String[] selectedlongestDist = new String[1];
        longestDist.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedlongestDist[0] = parent.getItemAtPosition(position).toString();
                if (selectedlongestDist[0].equals("Set Limit")){
                    setLimit.setVisibility(View.VISIBLE);
                    setLimitText[0] = selectedlongestDist[0];
                }
                if (selectedlongestDist[0].equals("No Limit")) {
                    setLimit.setVisibility(View.INVISIBLE);
                    limit = selectedlongestDist[0];
                    setLimitText[0] = selectedlongestDist[0];
                    setLimit.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] selectedNotice = new String[1];
                advanceNotice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedNotice[0] = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final String[] selectedshortestTrip = new String[1];
                shortestTrip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedshortestTrip[0] = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (setLimitText[0].equals("Set Limit")){
                    limit = setLimit.getText().toString();
                }
                else if (setLimitText[0].equals("No Limit")){
                    limit = "No Limit";
                }
                Toast.makeText(getApplicationContext(), limit, Toast.LENGTH_LONG).show();

                bundle.putString("advancenotice", selectedNotice[0]);
                bundle.putString("shortesttrip", selectedshortestTrip[0]);
                bundle.putString("longesttrip", limit);

                Intent intent = new Intent(getBaseContext(),ImageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
