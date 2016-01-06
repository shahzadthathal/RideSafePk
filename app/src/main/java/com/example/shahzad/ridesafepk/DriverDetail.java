package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class DriverDetail extends AppCompatActivity implements  View.OnClickListener{

    Button btnContactDriver;
    TextView tvName, tvPhone, tvDistance;
    ImageView mImageView;

    Chronometer chronometer;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        mImageView = (ImageView) findViewById(R.id.imageView);

        btnContactDriver = (Button) findViewById(R.id.btnContactDriver);
        btnContactDriver.setOnClickListener(this);

        chronometer = (Chronometer) findViewById(R.id.chronometer);


        if(GlobalSection.driverDetail == null)
        {
            finish();
        }
        else{
            tvName.setText("Name: "+GlobalSection.driverDetail.name);
            tvPhone.setText("Phone: "+GlobalSection.driverDetail.phone);
            tvDistance.setText(GlobalSection.driverDetail.distance.toString()+"km away");
            mImageView.setImageResource(R.drawable.pic1);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContactDriver:
                chronometer.start(); //chronometer.stop();

               int customerid = User.loggedInUserId;
               int driverid =  GlobalSection.driverDetail.id;
               String from_address = GlobalSection.FromText;
               String to_address = GlobalSection.ToText;
               double from_lat = GlobalSection.FromLat;
               double from_lng = GlobalSection.FromLong;
               double to_lat = GlobalSection.ToLat;
               double to_lng = GlobalSection.ToLong;

                Log.d("customerid",customerid+"");
                Log.d("driverid",driverid+"");
                Log.d("from_address",from_address+"");
                Log.d("to_address",to_address+"");
                Log.d("from_lat",from_lat+"");
                Log.d("from_lng",from_lng+"");
                Log.d("to_lat",to_lat+"");
                Log.d("to_lng",to_lng+"");

               RideModel rideModel = new RideModel(customerid, driverid,from_address, to_address, from_lat, from_lng, to_lat, to_lng);
               Log.d("rideModel", rideModel.from_destination+"");

               GlobalSection.rideDetailBeforeSave = rideModel;
               Log.d("ride detail",GlobalSection.rideDetailBeforeSave.from_destination+"");

               new AddRideDetail().execute();

               Toast.makeText(this, "Conecting Driver, pleaase wait", Toast.LENGTH_SHORT).show();
        }
    }

    public class AddRideDetail extends AsyncTask<Void, Void, RideModel>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DriverDetail.this);
            pDialog.setMessage("Sending Request to driver...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected  RideModel doInBackground(Void... params)
        {
            RideModel rideModel = null;
            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                rideModel =  service.AddRide(getApplicationContext(),GlobalSection.rideDetailBeforeSave);
                GlobalSection.rideDetailBeforeSave = rideModel;
            }catch (IOException e) {
                e.printStackTrace();
            }
            return rideModel;
        }
        protected void onPostExecute(RideModel rideModel){
            pDialog.dismiss();
            super.onPostExecute(rideModel);
            if(rideModel != null) {
                startActivity(new Intent(getApplicationContext(), FoundDrivers.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"An error occour to create ride, please try again", Toast.LENGTH_LONG).show();
            }
        }

    }

}
