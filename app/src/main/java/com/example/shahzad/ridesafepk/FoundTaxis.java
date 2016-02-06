package com.example.shahzad.ridesafepk;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FoundTaxis extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_taxis);
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

        if(mMap != null)
        {
            mMap.clear();
        }

        mMap = googleMap;


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("Clicked Driver",marker.getTitle().toString());
                try {
                    int userid = Integer.parseInt(marker.getTitle().toString());
                    for (DriverModel d : GlobalSection.driversList) {
                        if (d.id == userid) {
                            if(d.isAvailable ==1) {
                                GlobalSection.SelectedDriverID = d.id;
                                GlobalSection.driverDetail = d;
                                startActivity(new Intent(getApplicationContext(), DriverDetail.class));
                            }
                        }
                    }
                }catch (Exception e)
                {

                }

                return false;
            }
        });


        // Add a marker in Sydney and move the camera
        LatLng passenger = new LatLng(GlobalSection.userProfile.lat, GlobalSection.userProfile.lng);
        mMap.addMarker(new MarkerOptions().position(passenger).title("Me here")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.from_destination));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(passenger));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(passenger, 12));

        if(GlobalSection.driversList != null && GlobalSection.driversList.size() > 0)
        {
            addMarkers();
        }

    }
    private void addMarkers() {

        if(mMap != null)
        {
            LatLng driver;
            for (DriverModel d : GlobalSection.driversList)
            {
                driver = new LatLng(d.lat, d.lng);

                if (d.isAvailable == 1)
                {
                    mMap.addMarker(new MarkerOptions().position(driver)
                            //.title(d.name + ""))
                            .title(d.id + ""))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxiy));
                }
                else
                {
                    mMap.addMarker(new MarkerOptions().position(driver).title(d.name + "")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.taxin)); //.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.to_destination)); //.showInfoWindow()
                }
            }
        }
    }


    @Override
    protected void onStart()
    {
        if(GlobalSection.userProfile == null) {
            startActivity(new Intent(this, Login.class));
        }
        super.onStart();
    }

}
