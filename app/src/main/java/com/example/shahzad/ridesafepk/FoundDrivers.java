package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FoundDrivers extends AppCompatActivity {

    ListView listView;
    DriverAdapter driverAdapter;
    ArrayList<DriverModel> driverData = new ArrayList<DriverModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_drivers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        setListData();
        Resources res =getResources();
        listView = (ListView) findViewById(R.id.lvItems);
        driverAdapter =  new DriverAdapter(getApplicationContext(), driverData, res);
        listView.setAdapter(driverAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(), "item clicked", Toast.LENGTH_SHORT).show();
                ;

                DriverModel driverModel = driverData.get(position);
                GlobalSection.SelectedDriverID = driverModel.id;
                GlobalSection.driverDetail = driverModel;

                Log.d("Driver id", driverModel.id + "");

                startActivity(new Intent(getApplicationContext(), DriverDetail.class));

            }
        });

        */


        if(GlobalSection.driversList != null && GlobalSection.driversList.size() > 0)
        {

            for (DriverModel d : GlobalSection.driversList)
            {
                driverData.add(new DriverModel(d.id, d.name, d.phone, d.distance, d.rating, d.image));
            }

            Resources res =getResources();
            listView = (ListView) findViewById(R.id.lvItems);
            driverAdapter =  new DriverAdapter(getApplicationContext(), driverData, res);
           /* driverAdapter =  new DriverAdapter(getApplicationContext(),
                    driverData, res) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    //TextView text = (TextView) view.findViewById(android.R.id.text1);
                    //text.setTextColor(Color.WHITE);
                    return view;
                }
            };*/


            listView.setAdapter(driverAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub

                        Toast.makeText(getApplicationContext(),"item clicked",Toast.LENGTH_SHORT).show();;

                        DriverModel driverModel = driverData.get(position);
                        GlobalSection.SelectedDriverID = driverModel.id;
                        GlobalSection.driverDetail = driverModel;

                        Log.d("Driver id",driverModel.id+"");

                        startActivity(new Intent(getApplicationContext(), DriverDetail.class));

                    }
                });
        }
        else{
            Toast.makeText(getApplicationContext(), "No driver found at this time",Toast.LENGTH_LONG).show();
        }


        /*

        if(GlobalSection.driversList != null && GlobalSection.driversList.size() > 0)
        {

            for (DriverModel d : GlobalSection.driversList)
            {
                driverList.add(new DriverModel(d.id, d.name, d.phone, d.distance));
            }


            listView = (ListView)findViewById(R.id.listView);


            // old code
            //myAdapter = new ArrayAdapter<DriverModel>(this, android.R.layout.simple_expandable_list_item_1, driverList);

            myAdapter =  new ArrayAdapter<DriverModel>(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1, driverList) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.WHITE);
                    return view;
                }
            };

            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    GlobalSection.SelectedDriverID = myAdapter.getItem(position).id;
                    Integer did = myAdapter.getItem(position).id;
                    String name = myAdapter.getItem(position).name;
                    String phone = myAdapter.getItem(position).phone;
                    Double distance = myAdapter.getItem(position).distance;

                    DriverModel driverModel = new DriverModel(did,name,phone,distance);
                    GlobalSection.driverDetail = driverModel;

                    startActivity(new Intent(getApplicationContext(), DriverDetail.class));
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "No driver found at this time",Toast.LENGTH_LONG).show();
        }

        */

    }

    public void setListData()
    {
        for(Integer i=1; i<=14; i++)
        {
            String image = "";
            if(i>8) {
                 image = "pic" + (i-8);
            }
            else {
                 image = "pic" + i;
            }
            driverData.add(new DriverModel(i, "Driver "+i, "333333", 4.2,3.3, image));

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

   @Override
    protected void onStart() {

        if(!User.IsLoggedIn )
            startActivity(new Intent(this,Login.class));
        super.onStart();
    }

}
