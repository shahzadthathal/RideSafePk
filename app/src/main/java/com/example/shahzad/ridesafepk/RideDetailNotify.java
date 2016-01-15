package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideDetailNotify extends AppCompatActivity  implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private static final LatLng FROM_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.from_lat, GlobalSection.selectedRideDetail.from_lng);
    private static final LatLng TO_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.to_lat,GlobalSection.selectedRideDetail.to_lng);
    ProgressDialog pDialog;

    Button btnAccept, btnFinish, btnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail_notify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnReject = (Button) findViewById(R.id.btnReject);


        if(User.IsLoggedIn && User.loggedInUserType =="Passenger")
        {
            btnAccept.setEnabled(false);
            btnReject.setEnabled(false);
        }

        if(GlobalSection.selectedRideDetail !=null) {

            if(GlobalSection.selectedRideDetail.status == 1 || GlobalSection.selectedRideDetail.status == 2)
            {
                btnAccept.setBackgroundColor(Color.GREEN);
                btnAccept.setEnabled(false);
            }

            if(GlobalSection.selectedRideDetail.status == 2)
            {
                btnFinish.setBackgroundColor(Color.GREEN);
                btnFinish.setEnabled(false);
            }

            if(GlobalSection.selectedRideDetail.status == 3 )
            {
                btnReject.setBackgroundColor(Color.RED);
                btnReject.setEnabled(false);
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        else{
            Toast.makeText(getApplicationContext(), "There is and error, please select ride again", Toast.LENGTH_SHORT).show();

        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Accept Button Clicked", Toast.LENGTH_LONG).show();
                //1
                new ChangeRideStatus().execute();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Finish Button Clicked", Toast.LENGTH_LONG).show();
            }
            //1
            //new ChangeRideStatus().execute();
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reject Button Clicked", Toast.LENGTH_LONG).show();
                //3
                //new ChangeRideStatus().execute();
            }
        });


    }

    public class ChangeRideStatus extends AsyncTask<Void, Void, RideModel>{

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetailNotify.this);
            pDialog.setMessage("Updating ride status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected  RideModel doInBackground(Void... params){

            RideModel rideModel = null;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                rideModel = service.ChangeRideStatus(getApplicationContext(), GlobalSection.selectedRideDetail.id, GlobalSection.selectedRideDetail.driverID, 1);
            }catch (IOException e) {
                e.printStackTrace();
            }

            return  rideModel;
        }

        @Override
        protected void onPostExecute(RideModel rideModel) {
            pDialog.dismiss();
            super.onPostExecute(rideModel);
            if(rideModel != null) {
                Log.i("ride status 1", GlobalSection.selectedRideDetail.status + "");
                Log.i("ride status 1", rideModel.status + "");
                GlobalSection.selectedRideDetail = rideModel;

                if(rideModel.status ==1) {
                    btnAccept.setBackgroundColor(Color.GREEN);
                    btnAccept.setEnabled(false);
                }
                Toast.makeText(getApplicationContext(), "Job status has been changed", Toast.LENGTH_LONG).show();
            }
        }

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
                Toast.makeText(getApplicationContext(),"Action Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_ride:
                startActivity(new Intent(getApplicationContext(), BookRide.class));
                break;
            case R.id.action_view_rides:
                startActivity(new Intent(getApplicationContext(), RideHistory.class));
                break;
            case R.id.action_logout:
                User.IsLoggedIn = false;
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        LatLng rideLatLng;

        MarkerOptions options = new MarkerOptions();
        options.position(FROM_DESTINATION);
        options.position(TO_DESTINATION);
        googleMap.addMarker(options);

        String url = getMapsApiDirectionsUrl();
        Log.d("direction url", url.toString());

        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FROM_DESTINATION, 13));
        addMarkers();
    }


    private String getMapsApiDirectionsUrl() {

        String origin = "origin=" + GlobalSection.selectedRideDetail.from_lat + "," + GlobalSection.selectedRideDetail.from_lng;
        String destination = "destination=" + GlobalSection.selectedRideDetail.to_lat + "," + GlobalSection.selectedRideDetail.to_lng;
        String mode= "mode=driving";
        String key = "key=AIzaSyCuZLeFjkp3x-cCwkoa_zh1-Qotgg04no8";
        String params = origin + "&" + destination +"&"+ mode +"&"+ key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(FROM_DESTINATION).title(GlobalSection.selectedRideDetail.from_destination+""));// .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.from_destination)); //.showInfoWindow()
            googleMap.addMarker(new MarkerOptions().position(TO_DESTINATION).title(GlobalSection.selectedRideDetail.to_destination+"")); //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.to_destination)); //.showInfoWindow()
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetailNotify.this);
            pDialog.setMessage("Fetching route detail...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetailNotify.this);
            pDialog.setMessage("Drawing a path on map...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground( String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {

            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if(routes !=null) {
                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(8);
                    polyLineOptions.color(Color.RED);
                }

                if (googleMap != null) {
                    googleMap.addPolyline(polyLineOptions);
                }
                else{
                    Toast.makeText(getApplicationContext(),"google map id null", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"Fetching routes detail", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }
    }

}
