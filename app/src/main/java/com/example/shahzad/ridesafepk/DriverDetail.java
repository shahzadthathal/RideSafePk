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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class DriverDetail extends AppCompatActivity implements  View.OnClickListener{
//implements OnMapReadyCallback, View.OnClickListener{

    Button btnContactDriver;
    TextView name;
    TextView distance;
    TextView phone;
    RatingBar rating;
    ImageView image;

    Chronometer chronometer;
    ProgressDialog pDialog;

    private static final LatLng FROM_DESTINATION = new LatLng(GlobalSection.FromLat, GlobalSection.FromLong);


    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        image=(ImageView) findViewById(R.id.icon);
        name = (TextView) findViewById(R.id.tvName);
        distance = (TextView) findViewById(R.id.tvDistance);
        phone = (TextView) findViewById(R.id.tvPhone);
        rating = ((RatingBar) findViewById(R.id.rbRating));


        btnContactDriver = (Button) findViewById(R.id.btnContactDriver);
        btnContactDriver.setOnClickListener(this);

       // chronometer = (Chronometer) findViewById(R.id.chronometer);


        if(GlobalSection.driverDetail == null)
        {
            finish();
        }
        else{

            name.setText("Name: "+GlobalSection.driverDetail.name);
            distance.setText(GlobalSection.driverDetail.distance.toString()+"km away");
            phone.setText("Phone: "+GlobalSection.driverDetail.phone);
            rating.setRating(Float.parseFloat(GlobalSection.driverDetail.rating.toString()));
            image.setImageResource(R.drawable.pic1);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

    }

    /*
    @Override
    public void onMapReady(GoogleMap mMap) {

        if(googleMap !=null)
            googleMap.clear();

        googleMap = mMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng driverLatLng = new LatLng(33.6629794, 73.0738022);
        LatLng passengerLatLng = FROM_DESTINATION;
        googleMap.addMarker(new MarkerOptions().position(driverLatLng).title("Driver")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxi));
        googleMap.addMarker(new MarkerOptions().position(passengerLatLng).title("Passenger")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.from_destination));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(passengerLatLng, 13));
    }

    */



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContactDriver:
                //chronometer.start(); //chronometer.stop();

               int customerid = User.loggedInUserId;
               int driverid =  GlobalSection.driverDetail.id;
               String from_address = GlobalSection.FromText;
               String to_address = GlobalSection.ToText;
               double from_lat = GlobalSection.FromLat;
               double from_lng = GlobalSection.FromLong;
               double to_lat = GlobalSection.ToLat;
               double to_lng = GlobalSection.ToLong;

               RideModel rideModel = new RideModel(customerid, driverid,from_address, to_address, from_lat, from_lng, to_lat, to_lng);
               Log.d("rideModel", rideModel.from_destination+"");

               GlobalSection.rideDetailBeforeSave = rideModel;
               Log.d("ride detail",GlobalSection.rideDetailBeforeSave.from_destination+"");

               new AddRideDetail().execute();

               Toast.makeText(this, "Conecting Driver, pleaase wait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {

        if(!User.IsLoggedIn )
            startActivity(new Intent(this,Login.class));
        super.onStart();
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
                startActivity(new Intent(getApplicationContext(),BookRide.class));
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
                Log.d("From lat",GlobalSection.rideDetailBeforeSave.from_lat+"");
                Log.d("From lng",GlobalSection.rideDetailBeforeSave.from_lng+"");
                Log.d("To lat",GlobalSection.rideDetailBeforeSave.to_lat+"");
                Log.d("To lng",GlobalSection.rideDetailBeforeSave.to_lng+"");
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                rideModel =  service.AddRide(getApplicationContext(),GlobalSection.rideDetailBeforeSave);
                GlobalSection.rideDetailAfterSave = rideModel;
                GlobalSection.selectedRideDetail = rideModel;

            }catch (IOException e) {
                e.printStackTrace();
            }
            return rideModel;
        }
        protected void onPostExecute(RideModel rideModel){
            pDialog.dismiss();
            super.onPostExecute(rideModel);
            if(rideModel != null) {
                startActivity(new Intent(getApplicationContext(), RideDetail.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"An error occour to create ride, please try again", Toast.LENGTH_LONG).show();
            }
        }

    }

}
