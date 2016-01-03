package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FoundDrivers extends AppCompatActivity {

    /*ListView list;
    String[] itemname ={
            "Driver 1",
            "Driver 2",
            "Driver 2",
            "Driver 3",
            "Driver 4",
            "Driver 5",
            "Driver 6",
            "Driver 7"
    };

    Integer[] imgid={
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
    };
    */

    ArrayList<DriverModel> driverList = new ArrayList<DriverModel>();
    ListView listView;
    ArrayAdapter<DriverModel> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_drivers);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // ArrayList<DriverModel> result = GlobalSection.driversList;

        if(GlobalSection.driversList.size() > 0){

            for (DriverModel d : GlobalSection.driversList) {
                driverList.add(new DriverModel(d.id, d.name, d.phone, d.distance));
            }

            listView = (ListView)findViewById(R.id.listView);

            myAdapter = new ArrayAdapter<DriverModel>(this, android.R.layout.simple_expandable_list_item_1, driverList);
            listView.setAdapter(myAdapter);

           /* ArrayAdapter<DriverModel> result = new ArrayAdapter<DriverModel>(this, android.R.layout.simple_expandable_list_item_1){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = convertView;
                    if (v == null) {
                        LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.content_found_drivers, null);
                    }
                    //DriverModel currentEmployee = result.get(position);

                    v.setTag(getMyIdForPosition(position));
                    return convertView;
                }
            };
            */

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    GlobalSection.SelectedDriverID = myAdapter.getItem(position).id;
                     Log.d("item", GlobalSection.SelectedDriverID+"");
                    //startActivity(new Intent(this, DriverDetail.class));
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "No driver found at this time",Toast.LENGTH_LONG).show();
        }




       /* CustomListAdapter adapter=new CustomListAdapter(this, drivername, imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

}
