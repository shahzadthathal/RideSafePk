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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        txtPickup = (EditText) findViewById(R.id.txtPickup);
        txtDropoff = (EditText) findViewById(R.id.txtDropoff);

        txtPickup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                SelectAddress.isFrom = true;
                //startActivity(new Intent(getApplicationContext(), SelectAddress.class));
            }
        });

        txtDropoff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SelectAddress.isFrom = false;
               // startActivity(new Intent(getApplicationContext(), SelectAddress.class));
            }
        });

        txtPickup.setText(GlobalSection.FromText);
        txtDropoff.setText(GlobalSection.ToText);


        btnFindNow = (Button) findViewById(R.id.btnFindNow);
      //  btnFindNow.setOnClickListener(this);
        btnFindNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(GlobalSection.FromLat > 0 && GlobalSection.ToLat >0)
                {
                    LatLng   fromlatLng = new LatLng(GlobalSection.FromLat, GlobalSection.FromLong);
                    LatLng   tolatLng = new LatLng(GlobalSection.ToLat, GlobalSection.ToLong);
                    if(myMap !=null) {
                        Polyline line = myMap.addPolyline(new PolylineOptions()
                                .add(fromlatLng, tolatLng)
                                .width(5)
                                .color(Color.RED));
                    }
                }*/

                new FindDrivers().execute();

            }
        });
    }

    /*public void onMapReady(GoogleMap retMap) {

        myMap = retMap;
    }*/

    public  class FindDrivers extends AsyncTask<Void, Void, ArrayList<DriverModel>>
    {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookRide.this);
            pDialog.setMessage("Searching driver nearby you...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  ArrayList<DriverModel> doInBackground(Void... params)
        {
            ArrayList<DriverModel> result = null;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                result = service.FindDriversRequest(getApplicationContext(),33.6630613,73.0766153);
                if(result!=null) {
                   Log.e("Total Drivers", result.size() + "");

                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(ArrayList<DriverModel> drivers) {
            pDialog.dismiss();
            goToNextActivityBookRide(drivers);
        }
    }

    public void goToNextActivityBookRide(ArrayList<DriverModel> drivers)
    {
        Log.d("gottonextactive","go to next activer");
        Intent intent = new Intent(this, FoundDrivers.class);
        intent.putExtra("DriversList", drivers);
        startActivity(intent);

    }
}
