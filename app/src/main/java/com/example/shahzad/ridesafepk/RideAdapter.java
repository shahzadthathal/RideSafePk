package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
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
public class RideAdapter extends ArrayAdapter<RideModel> {

    public Resources _res;

    // View lookup cache
    private static class ViewHolder {
        TextView user_name;
        TextView from_destination;
        TextView to_destination;
        TextView amount;
        RatingBar rating;
        TextView status;
    }

    public RideAdapter(Context context, ArrayList<RideModel> rides, Resources res) {
        super(context, R.layout.item_ride, rides);
        _res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RideModel ride = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_ride, parent, false);


            viewHolder.user_name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.status = (TextView) convertView.findViewById(R.id.tvJobStatus);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.tvAmount);
            viewHolder.rating = ((RatingBar) convertView.findViewById(R.id.rbRating));

            viewHolder.from_destination = (TextView) convertView.findViewById(R.id.tvFromDestination);
            viewHolder.to_destination = (TextView) convertView.findViewById(R.id.tvToDestination);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        //

        if(User.loggedInUserType.equals("Driver")){
            viewHolder.user_name.setText(ride.passenger_name); //ride.passenger_name.toString()
        }else{
            viewHolder.user_name.setText(ride.driver_name); //ride.driver_name.toString()
        }

        if(ride.status == 0) {
            viewHolder.status.setText("Pending");
        }
        else if(ride.status == 1){
            viewHolder.status.setText("Accept");
        }
        else if(ride.status == 2){
            viewHolder.status.setText("Finish");
        }
        else if(ride.status == 3){
            viewHolder.status.setText("Reject");
        }

        viewHolder.amount.setText(ride.amount+"");//ride.amount+""
        viewHolder.rating.setRating(Float.parseFloat(ride.rating+""));//Float.parseFloat(ride.rating+"")
        viewHolder.from_destination.setText(ride.from_destination);
        viewHolder.to_destination.setText(ride.to_destination);

        // Return the completed view to render on screen
        return convertView;
    }
}