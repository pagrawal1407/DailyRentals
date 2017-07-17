package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AvailabilityActivity extends AppCompatActivity {

    private Button next;
    private EditText advanceNotice, shortestTrip, longestTrip;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        bundle = getIntent().getExtras();
        intializeEditText();
        onNextPressed();
    }

    private void intializeEditText() {
        advanceNotice = (EditText) findViewById(R.id.acceptAdvanceNotice);
        shortestTrip = (EditText) findViewById(R.id.acceptShortestTrip);
        longestTrip = (EditText) findViewById(R.id.acceptLongestTrip);
    }

    private void onNextPressed() {
        next = (Button) findViewById(R.id.nextButton1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advanceNoticeText, shortestTripText, longestTripText;

                advanceNoticeText = advanceNotice.getText().toString();
                shortestTripText = shortestTrip.getText().toString();
                longestTripText = longestTrip.getText().toString();

                bundle.putString("advancenotice", advanceNoticeText);
                bundle.putString("shortesttrip", shortestTripText);
                bundle.putString("longesttrip", longestTripText);

                Intent intent = new Intent(getBaseContext(),ImageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
