package com.example.csci567.dailyrentals;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utility.DataPOJO;

/**
 * Created by Parag on 7/5/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    ArrayList<DataPOJO> mlist = new ArrayList<DataPOJO>();
    Context mcontext;

    static class ViewHolder{
        public TextView title;
        public TextView location;
        public TextView zipcode;
        public TextView year;
    }

    public ListViewAdapter(ArrayList<DataPOJO> list, Context context){
        this.mlist = list;
        this.mcontext = context;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_item,null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.carTitle);
            viewHolder.zipcode = (TextView) rowView.findViewById(R.id.zipcode);
            viewHolder.location = (TextView) rowView.findViewById(R.id.car_location);
            viewHolder.year = (TextView) rowView.findViewById(R.id.car_year);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        DataPOJO data = mlist.get(position);
        String title = data.make + " " + data.model;
        holder.title.setText(title);

        String year = Integer.toString(data.year);
        holder.year.setText(year);

        String zipcode = Integer.toString(data.zipcode);
        holder.zipcode.setText(zipcode);

        Geocoder gc = new Geocoder(mcontext);
        List <Address> addList = null;
        try {
            addList = gc.getFromLocation(data.latitude, data.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address add = addList.get(0);
        String location = add.getLocality();
        holder.location.setText(location);
        return rowView;
    }
}
