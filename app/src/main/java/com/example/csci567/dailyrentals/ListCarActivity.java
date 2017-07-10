package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListCarActivity extends AppCompatActivity {

    private Button getStartedButton;
    private Button learnMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_car);
        getStarted();
        learnMore();
    }

    private void learnMore() {
        learnMoreButton = (Button) findViewById(R.id.learnmore);
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getStarted() {
        getStartedButton = (Button) findViewById(R.id.getstarted);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getStartedIntent = new Intent(getBaseContext(),CarDetails.class);
                startActivity(getStartedIntent);
            }
        });
    }


}
