package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class RideHistory extends AppCompatActivity {


   // ArrayList<RideModel> rideList = new ArrayList<RideModel>();
    //ListView listView;
    //ArrayAdapter<RideModel> myAdapter;

    ListView listView;
    RideAdapter rideAdapter;
    ArrayList<RideModel> rideData = new ArrayList<RideModel>();



    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(GlobalSection.rideHistoryList !=null && GlobalSection.rideHistoryList.size() > 0) {

            for (RideModel r : GlobalSection.rideHistoryList) {
                rideData.add(new RideModel(r.id, r.passengerID, r.driverID, r.from_destination, r.to_destination, r.from_lat, r.from_lng, r.to_lat, r.to_lng, r.status, r.amount, r.review, r.rating, r.driver_name, r.passenger_name));
            }

            Resources res =getResources();
            listView = (ListView) findViewById(R.id.lvItems);
            //rideAdapter =  new RideAdapter(getApplicationContext(), rideData, res);

            rideAdapter =  new RideAdapter(getApplicationContext(), rideData, res) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    RideModel rideModel = rideData.get(position);
                    TextView txt;

                    if(rideModel.status ==0)
                    {
                        txt = (TextView) view.findViewById(R.id.tvJobStatus);
                        txt.setTextColor(Color.YELLOW);
                    }
                    else if(rideModel.status ==1)
                    {
                        txt = (TextView) view.findViewById(R.id.tvJobStatus);
                        txt.setTextColor(Color.BLUE);
                    }
                    else if(rideModel.status ==2)
                    {
                       txt = (TextView) view.findViewById(R.id.tvJobStatus);
                       txt.setTextColor(Color.DKGRAY);
                    }
                    else if(rideModel.status ==3)
                    {
                        txt = (TextView) view.findViewById(R.id.tvJobStatus);
                        txt.setTextColor(Color.RED);
                    }

                    return view;
                }
            };


            listView.setAdapter(rideAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    RideModel rideModel = rideData.get(position);
                    GlobalSection.selectedRideDetail = rideModel;
                    startActivity(new Intent(getApplicationContext(), RideDetail.class));

                }
            });
        }
        else{
            //Toast.makeText(getApplicationContext(), "No rides found",Toast.LENGTH_LONG).show();
            new GetAllRide().execute();
        }

        /*
        if(GlobalSection.rideHistoryList !=null && GlobalSection.rideHistoryList.size() > 0){

            for (RideModel r : GlobalSection.rideHistoryList) {
                rideData.add(new RideModel(r.id, r.passengerID, r.driverID, r.from_destination, r.to_destination, r.from_lat, r.from_lng, r.to_lat, r.to_lng, r.status, r.amount, r.review, r.rating, r.driver_name, r.passenger_name));
            }

            listView = (ListView)findViewById(R.id.listView);

            //myAdapter = new ArrayAdapter<RideModel>(this, android.R.layout.simple_expandable_list_item_1, rideList);
            myAdapter =  new ArrayAdapter<RideModel>(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1, rideList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.WHITE);
                    return view;
                }
            };
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    Integer rid = myAdapter.getItem(position).id;
                    Integer pid = myAdapter.getItem(position).passengerID;
                    Integer did = myAdapter.getItem(position).driverID;
                    String from_destination = myAdapter.getItem(position).from_destination;
                    String to_destination = myAdapter.getItem(position).to_destination;
                    double from_lat = myAdapter.getItem(position).from_lat;
                    double from_lng = myAdapter.getItem(position).from_lng;
                    double to_lat = myAdapter.getItem(position).to_lat;
                    double to_lng = myAdapter.getItem(position).to_lng;
                    Integer status = myAdapter.getItem(position).status;

                    float amount = myAdapter.getItem(position).amount;
                    String review = myAdapter.getItem(position).review;
                    float rating = myAdapter.getItem(position).rating;

                    String driver_name = myAdapter.getItem(position).driver_name;
                    String passenger_name = myAdapter.getItem(position).passenger_name;

                    RideModel rideModel = new RideModel(rid, pid, did, from_destination, to_destination, from_lat, from_lng, to_lat, to_lng, status, amount, review, rating, driver_name, passenger_name);
                    GlobalSection.selectedRideDetail = rideModel;
                    startActivity(new Intent(getApplicationContext(), RideDetail.class));
                }
            });
        }
        else{
            new GetAllRide().execute();
        }
        */



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId())
        {
            case R.id.action_profile:
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
                break;
            case R.id.action_add_ride:
                startActivity(new Intent(getApplicationContext(), BookRide.class));
                break;
            case R.id.action_view_rides:
                startActivity(new Intent(getApplicationContext(), RideHistory.class));
                break;
            case R.id.action_logout:
                startActivity(new Intent(getApplicationContext(), Logout.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean  onPrepareOptionsMenu(Menu menu) {
        if (User.IsLoggedIn) {
            if(User.loggedInUserType.equals("Driver")) {
                menu.findItem(R.id.action_add_ride).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }


    public  class GetAllRide extends AsyncTask<Void, Void, RideModel>
    {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideHistory.this);
            pDialog.setMessage("Fetching your rides...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected  RideModel doInBackground(Void... params)
        {
            RideModel rideModel = null;
            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                // GlobalSection.driversList = service.FindDriversRequest(getApplicationContext(),33.6630613,73.0766153);
                GlobalSection.rideHistoryList = service.GetAllRides(getApplicationContext(),User.loggedInUserId, User.loggedInUserType);
                if(GlobalSection.rideHistoryList!=null) {
                    Log.e("Total Rides", GlobalSection.rideHistoryList.size() + "");

                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return rideModel;
        }
        protected void onPostExecute(RideModel rideModel){
            pDialog.dismiss();
            super.onPostExecute(rideModel);
            if(GlobalSection.rideHistoryList !=null) {
                if(GlobalSection.rideHistoryList.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "No rides found...", Toast.LENGTH_LONG).show();

                    if(User.loggedInUserType == "Passenger") {
                        startActivity(new Intent(getApplicationContext(), BookRide.class));
                    }
                    else{
                        finish();
                    }
                }
                else {
                    startActivity(new Intent(getApplicationContext(), RideHistory.class));
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "No rides found...", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {

        if(!User.IsLoggedIn )
            startActivity(new Intent(this,Login.class));
        super.onStart();
    }
}
