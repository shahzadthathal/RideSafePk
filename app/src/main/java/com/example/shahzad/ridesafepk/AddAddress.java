package com.example.shahzad.ridesafepk;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class AddAddress extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap myMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    ProgressDialog pDialog;
    User user;

    EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user !=null) {
                    new SaveUserAddress(user).execute();
                }

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        etLocation = (EditText) findViewById(R.id.et_location);


        // Getting a reference to the map
        //myMap = supportMapFragment.getMap();


        // Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location


                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);

    }

    @Override
    public void onMapReady(GoogleMap retMap) {

        myMap = retMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        myMap.setMyLocationEnabled(true);

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange (Location location) {
                LatLng loc = new LatLng (location.getLatitude(), location.getLongitude());

                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(loc)
                                .title("Lat" + location.getLatitude() + ", Lng" + location.getLongitude())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.from_destination))
                                .draggable(true)
                );
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

                new GetAddressTask().execute(location);
            }

        };
        myMap.setOnMyLocationChangeListener(myLocationChangeListener);

        /*
        double dLatitude = 33.640388;
        double dLongitude = 73.088026;
        myMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("Default Location").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 5));


        myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("draged end function", "latitude : " + marker.getPosition().latitude);
                Log.d("draged end function", "langitude : " + marker.getPosition().longitude);
               // marker.setSnippet(" " + marker.getPosition().latitude);
                myMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
        */

    }

    public  class GetAddressTask extends AsyncTask<Location, Void, String> {


        /*
         * Get a Geocoder instance, get the latitude and longitude look up the
         * address, and return it
         *
              * @params params One or more Location objects
         *
         * @return A string containing the address of the current location, or an
         * empty string if no address can be found, or an error message
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
			/*
			 * Return 1 address.
			 */
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + Double.toString(loc.getLatitude()) + " , "
                        + Double.toString(loc.getLongitude())
                        + " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
			/*
			 * Format the first line of address (if available), city, and
			 * country name.
			 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ? address
                                .getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());


                String street = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "";
                String city = address.getLocality();
                String country = address.getCountryName();
                Double lat = address.getLatitude();
                Double lng = address.getLongitude();
                int id = User.loggedInUserId;
                user = new User(id, street, city, country, lat, lng);

                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        @Override
        protected  void onPostExecute(String address)
        {

            etLocation.setText(address.toString());
        }

    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                //addresses = geocoder.getFromLocationName(locationName[0], 1);
                addresses = geocoder.getFromLocationName(locationName[0], 1);
                while (addresses.size()==0) {
                    addresses = geocoder.getFromLocationName(locationName[0], 1);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            myMap.clear();

            if(addresses !=null) {
                // Adding Markers on Google Map for each matching address
                for (int i = 0; i < addresses.size(); i++) {

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getLocality(),
                            address.getCountryName());

                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);
                    markerOptions.snippet("This is your spot!");
                    markerOptions.draggable(true);

                    myMap.addMarker(markerOptions);

                    // Locate the first location
                    if (i == 0) {
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        //saveAddress(address);

                        String street = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "";
                        String city = address.getLocality();
                        String country = address.getCountryName();
                        Double lat = address.getLatitude();
                        Double lng = address.getLongitude();
                        int id = User.loggedInUserId;
                        user = new User(id, street, city, country, lat, lng);
                    }
                }
            }
        }
    }

  public void saveAddress(Address address){

      if(User.IsLoggedIn)
      {
          int id = User.loggedInUserId;
          String street =  address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "";
          String city = address.getLocality();
          String country = address.getCountryName();
          Double  lat =  address.getLatitude();
          Double  lng = address.getLongitude();
          user = new User(id, street, city, country, lat, lng);
         // new SaveUserAddress(user).execute();
      }

  }

    private  class SaveUserAddress extends AsyncTask<Void, Void, User>{

       User saveAdress;

        public SaveUserAddress(User u)
        {
            this.saveAdress = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddAddress.this);
            pDialog.setMessage("Saving Address...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  User doInBackground(Void... params){

            User returnUser = null;
            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                User result = service.saveAddress(getApplicationContext(), saveAdress);
                if(result!=null){
                    Log.d("Result","Address  added");
                    returnUser = new User(result.id, result.name, result.email, result.password, result.phone, result.nic, result.userType, result.street, result.city, result.country,result.lat, result.lng, result.is_login, result.is_vehicle_added,  result.reg_id, result.isError, result.errorMessage);
                    User.IsLoggedIn=true;
                    User.loggedInUserId = result.id;

                    if(result.userType.equals("Driver")){
                        startActivity(new Intent(getApplicationContext(), AddVehicle.class));
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(), BookRide.class));
                    }
                }
                else{
                    Log.d("Result","Address not aded");
                    finish();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(User user) {
            pDialog.dismiss();
            super.onPostExecute(user);
        }
    }
}