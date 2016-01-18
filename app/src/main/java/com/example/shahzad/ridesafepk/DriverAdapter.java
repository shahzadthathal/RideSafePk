package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shahzad on 1/18/2016.
 */
public class DriverAdapter extends ArrayAdapter<DriverModel> {

    public Resources _res;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView distance;
        TextView phone;
        RatingBar rating;
        ImageView image;
    }

    public DriverAdapter(Context context, ArrayList<DriverModel> users, Resources res) {
        super(context, R.layout.item_driver, users);
        _res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DriverModel user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_driver, parent, false);

            viewHolder.image=(ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.tvDistance);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.tvPhone);
            viewHolder.rating = ((RatingBar) convertView.findViewById(R.id.rbRating));

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(user.name);
        viewHolder.distance.setText(user.distance + "Km away");
        viewHolder.phone.setText(user.phone);
        viewHolder.rating.setRating(Float.parseFloat(user.rating.toString()));
        //viewHolder.image.setImageResource(_res.getDrawable(user.image) + user.image, null, null));
       // if(user.image ==null) {
            viewHolder.image.setImageResource(_res.getIdentifier("com.example.shahzad.ridesafepk:drawable/" + user.image, null, null));
        //}
   //     else{
//            viewHolder.image.setImageResource(_res.getIdentifier("com.example.shahzad.ridesafepk:drawable/" + user.image, null, null));
     //   }
        // Return the completed view to render on screen
        return convertView;
    }
}