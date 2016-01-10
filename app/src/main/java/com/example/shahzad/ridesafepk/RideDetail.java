package com.example.shahzad.ridesafepk;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideDetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

   // private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543, -73.998585);
   // private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
   // private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

    private static final LatLng FROM_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.from_lat, GlobalSection.selectedRideDetail.from_lng);
    private static final LatLng TO_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.to_lat,GlobalSection.selectedRideDetail.to_lng);


    final String TAG = "RideDetailActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(GlobalSection.selectedRideDetail !=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please wait, fetching rides", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
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


        // Add a marker in Sydney and move the camera
       /* if(GlobalSection.selectedRideDetail !=null){

            Log.d("Ride Detail Page", GlobalSection.selectedRideDetail.from_destination + "");
            Log.d("Ride Detail flat", GlobalSection.selectedRideDetail.from_lat + "");
            Log.d("Ride Detail flng", GlobalSection.selectedRideDetail.from_lng + "");

            rideLatLng = new LatLng(GlobalSection.selectedRideDetail.from_lat, GlobalSection.selectedRideDetail.from_lng);
            mMap.addMarker(new MarkerOptions().position(rideLatLng).title(GlobalSection.selectedRideDetail.from_destination + ""));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rideLatLng, 13));
        }
        else {
            rideLatLng = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(rideLatLng).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(rideLatLng));
        }
        */


    }


    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + FROM_DESTINATION.latitude + "," + FROM_DESTINATION.longitude
                + "|" + "|" + TO_DESTINATION.latitude + ","
                + TO_DESTINATION.longitude ;


        //String sensor = "sensor=false";
       /// String params = URLEncoder.encode(waypoints) + "&" + URLEncoder.encode(sensor);

        //String origin = "origin=" + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude;
        //String destination = "destination=" + WALL_STREET.latitude + "," + WALL_STREET.longitude;
        //String params = origin + "&" + destination + "&" + waypoints + "&" + sensor;

        //String output = "json";
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;

        String origin = "origin="+GlobalSection.selectedRideDetail.from_destination;
        String destination = "destination="+GlobalSection.selectedRideDetail.to_destination;
        String mode= "mode=driving";
        String key = "key=AIzaSyCuZLeFjkp3x-cCwkoa_zh1-Qotgg04no8";

        String output = "json";
        String params = origin +"&"+ destination +"&"+ mode +"&"+ key;
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output +"?"+ URLEncoder.encode(params);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=6th+Road,+Rawalpindi,+Pakistan&destination=Street+102,+Islamabad,+Pakistan,&mode=driving&key=AIzaSyCuZLeFjkp3x-cCwkoa_zh1-Qotgg04no8";

       // https://maps.googleapis.com/maps/api/directions/json?origin=75+9th+Ave+New+York,+NY&destination=MetLife+Stadium+1+MetLife+Stadium+Dr+East+Rutherford,+NJ+07073&key=YOUR_API_KEY

        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(FROM_DESTINATION).title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(TO_DESTINATION).title("Second Point"));
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
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
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
                    polyLineOptions.width(2);
                    polyLineOptions.color(Color.BLUE);
                }

                if (googleMap != null) {
                    googleMap.addPolyline(polyLineOptions);
                }
                else{
                    Toast.makeText(getApplicationContext(),"googlr map id null", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"No routes found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


}
