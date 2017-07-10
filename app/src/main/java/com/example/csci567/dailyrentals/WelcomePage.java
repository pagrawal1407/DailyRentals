package com.example.csci567.dailyrentals;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class WelcomePage extends AppCompatActivity {

    private ViewPager pageView1;
    private ViewPager pageView2;
    private ViewPager pageView3;
    private CustomSwipeAdapter adapter;
    private Button listCarButton;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily Rentals");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        setViewPager();
        listYourCar();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent newActivity = new Intent(getApplicationContext(), DisplaySearchResults.class);
                if(query != null) {
                    newActivity.putExtra("search_query", query);
                    View view = WelcomePage.this.getCurrentFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    startActivity(newActivity);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please enter search string first",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void listYourCar() {
        listCarButton = (Button) findViewById(R.id.listcar_button);
        listCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listCar = new Intent(WelcomePage.this, ListCarActivity.class);
                startActivity(listCar);
            }
        });
    }

    private void setViewPager() {
        pageView1 = (ViewPager) findViewById(R.id.viewpager1);
        pageView2 = (ViewPager) findViewById(R.id.viewpager2);
        pageView3 = (ViewPager) findViewById(R.id.viewpager3);
        adapter = new CustomSwipeAdapter(this);
        pageView1.setAdapter(adapter);
        pageView1.setPageMargin(30);
        pageView2.setAdapter(adapter);
        pageView3.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        searchView.setMenuItem(item);
        return true;
    }
}
