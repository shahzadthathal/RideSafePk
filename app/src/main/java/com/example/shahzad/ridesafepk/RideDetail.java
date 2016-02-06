package com.example.shahzad.ridesafepk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideDetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private static final LatLng FROM_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.from_lat, GlobalSection.selectedRideDetail.from_lng);
    private static final LatLng TO_DESTINATION = new LatLng(GlobalSection.selectedRideDetail.to_lat,GlobalSection.selectedRideDetail.to_lng);
    ProgressDialog pDialog;
    final String TAG = "RideDetailActivity";

    Button btnAccept, btnFinish, btnReject;

    final Context context = this;

    EditText etAmount, etReviews;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnReject = (Button) findViewById(R.id.btnReject);


        if(User.IsLoggedIn)
        {
            if(User.loggedInUserType.equals("Passenger"))
            {
                btnAccept.setEnabled(false);
            }
        }

        if(GlobalSection.selectedRideDetail !=null)
        {

            Log.d("job status",GlobalSection.selectedRideDetail.status+"");
            // if ride status is pending
            if(GlobalSection.selectedRideDetail.status == 0)
            {
                btnFinish.setEnabled(false);
            }

            // if ride has been accepted
            if(GlobalSection.selectedRideDetail.status == 1)
            {
                btnAccept.setBackgroundColor(Color.GREEN);
                btnAccept.setEnabled(false);
            }

            //if ride is finished
            if(GlobalSection.selectedRideDetail.status == 2)
            {
                btnAccept.setBackgroundColor(Color.GREEN);
                btnFinish.setBackgroundColor(Color.GREEN);

                btnAccept.setEnabled(false);
                btnFinish.setEnabled(false);
                btnReject.setEnabled(false);
            }

            //VehNav

            // if ride has been rejected
            if(GlobalSection.selectedRideDetail.status == 3 )
            {
                btnReject.setBackgroundColor(Color.RED);

                btnAccept.setEnabled(false);
                btnFinish.setEnabled(false);
                btnReject.setEnabled(false);
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        else{
            Toast.makeText(getApplicationContext(), "There is and error, please select ride again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), RideHistory.class));
        }



        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1 ride accepted
                new ChangeRideStatus().execute("1");
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);//context
                View promptsView = li.inflate(R.layout.finish_ride_alert_box, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set finish_ride_alert_box.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                etAmount = (EditText) promptsView.findViewById(R.id.etAmount);
                etReviews = (EditText) promptsView.findViewById(R.id.etReviews);
                ratingBar = ((RatingBar) promptsView.findViewById(R.id.ratingBar));

                // set dialog message
                alertDialogBuilder
                        .setTitle("Payment && Reviews")
                        .setCancelable(false)
                        .setPositiveButton("Submit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //String   amount = etAmount.getText().toString();
                                        //String   rating  = ratingBar.getRating()+"";
                                        //String   reviews = etReviews.getText().toString();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                Button sbumitButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                sbumitButton.setOnClickListener(new CustomListenerValidateDialogValue(alertDialog));

            }
        });




        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3 ride rejected
                new ChangeRideStatus().execute("3");
            }
        });

    }

    @Override
    protected void onStart() {

        if(!User.IsLoggedIn )
            startActivity(new Intent(this,Login.class));
        super.onStart();
    }

    public class CustomListenerValidateDialogValue implements View.OnClickListener {

        private final Dialog dialog;

        public CustomListenerValidateDialogValue(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            // put your code here
            String   amount_str = etAmount.getText().toString();
            String   rating_str  = ratingBar.getRating()+"";
            String   reviews = etReviews.getText().toString();

            if (amount_str.equals("") || amount_str.equals("0") || rating_str.equals("0.0") || reviews.equals("")) {
                Toast.makeText(RideDetail.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }else{
                int rid = GlobalSection.selectedRideDetail.id;
                Float amount = Float.parseFloat(amount_str);
                Float rating = Float.parseFloat(rating_str);
                RideModel rideModel = new RideModel(rid, amount, rating , reviews);
                new FinishRide(rideModel).execute();
                dialog.dismiss();
            }

        }
    }

    public class FinishRide extends AsyncTask<Void, Void, RideModel> {

        RideModel _rideModel;

        public FinishRide(RideModel rideModel)
        {
            _rideModel = rideModel;
        }
            protected void onPreExecute()
            {
                super.onPreExecute();
                pDialog = new ProgressDialog(RideDetail.this);
                pDialog.setMessage("Updating ride status...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            protected RideModel doInBackground(Void... params) {

                try {
                    WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                    _rideModel = service.FinishRide(getApplicationContext(), _rideModel);
                    GlobalSection.selectedRideDetail = _rideModel;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return _rideModel;
            }

            @Override
            protected void onPostExecute(RideModel rideModel)
            {

                pDialog.dismiss();
                super.onPostExecute(rideModel);

                if (rideModel != null)
                {
                    Log.i("ride status before", GlobalSection.selectedRideDetail.status + "");
                    Log.i("ride status after", rideModel.status + "");
                    GlobalSection.selectedRideDetail = rideModel;

                    if (rideModel.status == 2)
                    {
                        btnAccept.setBackgroundColor(Color.GREEN);
                        btnAccept.setEnabled(false);
                        btnFinish.setBackgroundColor(Color.GREEN);
                        btnFinish.setEnabled(false);
                        btnReject.setEnabled(false);
                    }

                    Toast.makeText(getApplicationContext(), "Job status has been changed", Toast.LENGTH_LONG).show();

                    // update GlobalSection.rideHistoryList
                    new GetAllRideUpdated().execute();
                }
            }
    }


    public class ChangeRideStatus extends AsyncTask<String, Void, RideModel> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetail.this);
            pDialog.setMessage("Updating ride status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected RideModel doInBackground(String... params) {
            RideModel rideModel = null;
            try {
                int rideStatus = Integer.parseInt(params[0]);
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                rideModel = service.ChangeRideStatus(getApplicationContext(), GlobalSection.selectedRideDetail.id, GlobalSection.selectedRideDetail.driverID, rideStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rideModel;
        }

        @Override
        protected void onPostExecute(RideModel rideModel) {
            pDialog.dismiss();
            super.onPostExecute(rideModel);
            if (rideModel != null) {
                Log.i("ride status 1", GlobalSection.selectedRideDetail.status + "");
                Log.i("ride status 1", rideModel.status + "");
                GlobalSection.selectedRideDetail = rideModel;

                if (rideModel.status == 1) {
                    btnAccept.setBackgroundColor(Color.GREEN);
                    btnAccept.setEnabled(false);
                }

                if (rideModel.status == 3) {
                    btnReject.setBackgroundColor(Color.RED);
                    btnAccept.setEnabled(false);

                    btnAccept.setEnabled(false);
                    btnFinish.setEnabled(false);
                }
                Toast.makeText(getApplicationContext(), "Job status has been changed", Toast.LENGTH_LONG).show();

                // update GlobalSection.rideHistoryList
                new GetAllRideUpdated().execute();
            }
        }
    }

    public  class GetAllRideUpdated extends AsyncTask<Void, Void, RideModel>
    {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetail.this);
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
                GlobalSection.rideHistoryList = service.GetAllRides(getApplicationContext(), User.loggedInUserId, User.loggedInUserType);
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
                //startActivity(new Intent(getApplicationContext(), RideHistory.class));
            }
            else{
               // Toast.makeText(getApplicationContext(), "No rides found...", Toast.LENGTH_LONG).show();
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

    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        LatLng rideLatLng;

        /*MarkerOptions options = new MarkerOptions();
        options.position(FROM_DESTINATION);
        options.position(TO_DESTINATION);
        googleMap.addMarker(options);
        */

        String url = getMapsApiDirectionsUrl();
        //Log.d("direction url", url.toString());


        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FROM_DESTINATION, 13));

        /*googleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(GlobalSection.selectedRideDetail.from_lat, GlobalSection.selectedRideDetail.from_lng))  // from
                .add(new LatLng(GlobalSection.selectedRideDetail.to_lat, GlobalSection.selectedRideDetail.to_lng))  // to
                //.add(new LatLng(21.291, -157.821))  // Hawaii
                //.add(new LatLng(37.423, -122.091))  // Mountain View
        );
        */

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
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(FROM_DESTINATION).title(GlobalSection.selectedRideDetail.from_destination+""));// .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.from_destination)); //.showInfoWindow()
            googleMap.addMarker(new MarkerOptions().position(TO_DESTINATION).title(GlobalSection.selectedRideDetail.to_destination+"")); //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.to_destination)); //.showInfoWindow()
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RideDetail.this);
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
            pDialog = new ProgressDialog(RideDetail.this);
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
