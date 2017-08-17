package com.example.csci567.dailyrentals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import utility.DataPOJO;

public class CustomSwipeAdapter extends PagerAdapter {

    private int[] imageResources = { R.drawable.th1, R.drawable.th2, R.drawable.th3, R.drawable.th4, R.drawable.th5};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(){}

    public CustomSwipeAdapter (Context ctx){

        this.ctx = ctx;
    }



    @Override
    public int getCount() {
        return imageResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.activity_custom_swipe_adapter, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageview);
        //TextView imageText = (TextView) itemView.findViewById(R.id.imagetext);
        imageView.setImageResource(imageResources[position]);
        //imageText.setText("Image: "+ position);
        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);

    }

}
