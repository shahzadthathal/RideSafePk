package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectAddress extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1 ;

    GoogleMap myMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    ProgressDialog pDialog;

    EditText etLocation;

   // Location locationt;

    public static boolean isFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        /*PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) { // Handle the selected Place
            }

            @Override
            public void onError(Status status) { // Handle the error
            }

        });

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
           e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Handle the exception
        }



        */



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(getApplicationContext(), BookRide.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        etLocation = (EditText) findViewById(R.id.et_location);

        // Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Defining button click event listener for the find button
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location


                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    ///location =  location+"&key=AIzaSyB_k92_bbhLDb-tITqqCMFv2Z4sYvTs0Og";
                    new GeocoderTask().execute(location);
                }
            }
        };
        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);

    }

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("AutoComplete1", "Place: " + place.getName());
                Log.i("AutoComplete2", "Place: " + place.getAddress());
                Log.i("AutoComplete3", "Place: " + place.getLatLng());

                etLocation.setText(place.getAddress());
                new GeocoderTask().execute(place.getAddress()+"");

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("AutoComplete", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
*/


    @Override
    public void onMapReady(GoogleMap retMap) {

        myMap = retMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //myMap.setMyLocationEnabled(true);

        /*
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


                locationt.setLatitude( marker.getPosition().latitude);
                locationt.setLongitude(marker.getPosition().longitude);
                new GetAddressTask().execute(locationt);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });

        */





        double dLatitude = 33.640388;
        double dLongitude = 73.088026;
        myMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
                .title("Default Location").draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
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
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);


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

                if(isFrom){

                    GlobalSection.FromText= addressText;
                    GlobalSection.FromLat= address.getLatitude();
                    GlobalSection.FromLong = address.getLongitude();
                }
                else{
                    GlobalSection.ToText= addressText;
                    GlobalSection.ToLat= address.getLatitude();
                    GlobalSection.ToLong = address.getLongitude();
                }



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
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try
            {
                // Getting a maximum of 1 Address that matches the input text
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
                return;
            }

            // Clears all the existing markers on the map
            myMap.clear();


                Address address = (Address) addresses.get(0);

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

                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                if(isFrom){
                    GlobalSection.FromText= addressText;
                    GlobalSection.FromLat= address.getLatitude();
                    GlobalSection.FromLong = address.getLongitude();
                    //startActivity(new Intent(getApplicationContext(),BookRide.class));
                }
                else{
                    GlobalSection.ToText= addressText;
                    GlobalSection.ToLat= address.getLatitude();
                    GlobalSection.ToLong = address.getLongitude();
                    //startActivity(new Intent(getApplicationContext(),BookRide.class));
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
