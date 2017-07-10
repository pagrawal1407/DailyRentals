package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp = (Button) findViewById(R.id.signUp);
        Button signIn = (Button) findViewById(R.id.signIn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSignUp();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSignIn();
            }
        });
    }

    private void callSignIn() {
        Intent signIn = new Intent(getBaseContext(),signin.class);
        //signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signIn);
    }

    private void callSignUp() {
        Intent signUp = new Intent(getBaseContext(),signup.class);
        //signUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signUp);
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
    }*/
}
