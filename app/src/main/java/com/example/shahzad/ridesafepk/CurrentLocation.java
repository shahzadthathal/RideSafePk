package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;
    Location location;

    Double lat, lng;

    Marker now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provide
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, false);

        try {
            // the last known location of this provider
             location = locationManager.getLastKnownLocation(provider);
        }catch (SecurityException e )
        {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);

        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        try {
            // location updates: at least 1 meter and 200millsecs change
            locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        }catch (SecurityException e){
            Log.e("PERMISSION_EXCEPTION","PERMISSION_NOT_GRANTED");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng current = new LatLng(this.lat, this.lng);
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            mMap.clear();

            if(now != null)
            {
                now.remove();
            }

            // Initialize the location field
            lat = location.getLatitude();
            lng = location.getLongitude();

           /* try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10); //<10>
                for (Address address : addresses) {
                    this.locationText.append("\n" + address.getAddressLine(0));
                }
            }*/


                // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(lat, lng);
            now = mMap.addMarker(new MarkerOptions().position(latLng));
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            Toast.makeText(getApplicationContext(),  "Location changed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), provider + "'s status changed to "+status +"!",  Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider " + provider + " enabled!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider " + provider + " disabled!", Toast.LENGTH_SHORT).show();

        }
    }

}
