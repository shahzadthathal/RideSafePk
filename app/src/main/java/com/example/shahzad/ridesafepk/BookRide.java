package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookRide extends AppCompatActivity  { //implements OnMapReadyCallback

    private ListView lv;

    /*GoogleMap myMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    */
    ProgressDialog pDialog;

    Button btnFindNow;
    EditText txtPickup, txtDropoff;

    //public  static  ArrayList<DriverModel> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);
        */



        txtPickup = (EditText) findViewById(R.id.txtPickup);
        txtDropoff = (EditText) findViewById(R.id.txtDropoff);

        txtPickup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                SelectAddress.isFrom = true;
                startActivity(new Intent(getApplicationContext(), SelectAddress.class));
            }
        });

        txtDropoff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SelectAddress.isFrom = false;
                startActivity(new Intent(getApplicationContext(), SelectAddress.class));
            }
        });

        txtPickup.setText(GlobalSection.FromText);
        txtDropoff.setText(GlobalSection.ToText);


        btnFindNow = (Button) findViewById(R.id.btnFindNow);
      //  btnFindNow.setOnClickListener(this);
        btnFindNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( GlobalSection.FromLat !=0 && GlobalSection.FromLong != 0) {
                    new FindDrivers().execute();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select address properly", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /*public void onMapReady(GoogleMap retMap) {

        myMap = retMap;
    }*/



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
                Toast.makeText(getApplicationContext(),"Action Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_ride:
                //startActivity(new Intent(getApplicationContext(),BookRide.class));
                break;
            case R.id.action_view_rides:
                startActivity(new Intent(getApplicationContext(),RideHistory.class));
                break;
            case R.id.action_logout:
                User.IsLoggedIn = false;
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public  class FindDrivers extends AsyncTask<Void, Void, DriverModel>
    {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookRide.this);
            pDialog.setMessage("Searching driver nearby you...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected  DriverModel doInBackground(Void... params)
        {
            DriverModel driverModel = null;
            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
               // GlobalSection.driversList = service.FindDriversRequest(getApplicationContext(),33.6630613,73.0766153);
               GlobalSection.driversList = service.FindDriversRequest(getApplicationContext(),GlobalSection.FromLat,GlobalSection.FromLong);
                if(GlobalSection.driversList!=null) {
                   Log.e("Total Drivers", GlobalSection.driversList.size() + "");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return driverModel;
        }
        protected void onPostExecute(DriverModel driverModel){
            pDialog.dismiss();
            super.onPostExecute(driverModel);
            startActivity(new Intent(getApplicationContext(), FoundDrivers.class));
        }
    }


}
