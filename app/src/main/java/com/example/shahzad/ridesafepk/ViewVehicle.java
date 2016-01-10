package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ViewVehicle extends AppCompatActivity implements View.OnClickListener{

    EditText txtName, txtModelName, txtManufacturerName;
    Button btnUpdateVehicle;
    ProgressDialog pDialog;
    User user;
    VehicleModel vehicleModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (EditText) findViewById(R.id.txtName);
        txtModelName = (EditText) findViewById(R.id.txtModelName);
        txtManufacturerName = (EditText) findViewById(R.id.txtManufacturerName);

        btnUpdateVehicle = (Button) findViewById(R.id.btnUpdateVehicle);

        vehicleModel = GlobalSection.vehicleDetail;
        if(vehicleModel!=null){

            txtName.setText(vehicleModel.name.toString());
            txtModelName.setText(vehicleModel.model_name.toString());
            txtManufacturerName.setText(vehicleModel.manufacturer_name.toString());
        }
        else{
             new VehicleDetail().execute();
        }

        btnUpdateVehicle.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnUpdateVehicle:

                Integer vid = GlobalSection.vehicleDetail.id;

                String name    = txtName.getText().toString();
                String modelName = txtModelName.getText().toString();
                String manufacturerName = txtManufacturerName.getText().toString();
                Integer ownerId = GlobalSection.vehicleDetail.ownerId;

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your vehicle number", Toast.LENGTH_SHORT).show();
                }
                else if (modelName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter model name", Toast.LENGTH_SHORT).show();
                }
                else if (manufacturerName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter manufacturer name", Toast.LENGTH_SHORT).show();
                }
                else {
                    VehicleModel vehicleModel = new VehicleModel(vid, name, modelName, manufacturerName, ownerId);
                    new UpdateVehicle(vehicleModel).execute();
                }
                break;
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
                user = null;
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class VehicleDetail extends AsyncTask<Void, Void, VehicleModel> {

        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewVehicle.this);
            pDialog.setMessage("Retrieving vehicle detail...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected VehicleModel doInBackground(Void... paramd)
        {
            VehicleModel returnVehicleModel = null;
            try{
                WebserviceHandler wsh = new WebserviceHandler(getApplicationContext());
                returnVehicleModel  = wsh.GetVehicleDetail(getApplicationContext(),User.loggedInUserId);
                GlobalSection.vehicleDetail = returnVehicleModel;
            }
            catch (IOException e) {
                e.printStackTrace();
            };
            return returnVehicleModel;
        }

        protected void onPostExecute(VehicleModel vm)
        {
            pDialog.dismiss();
            super.onPostExecute(vm);

            if(vm != null) {

                txtName.setText(vm.name.toString());
                txtModelName.setText(vm.model_name.toString());
                txtManufacturerName.setText(vm.manufacturer_name.toString());
            }
        }
    }

    public  class UpdateVehicle extends AsyncTask<Void, Void, VehicleModel> {
        VehicleModel vModel;

        public  UpdateVehicle(VehicleModel v){
            this.vModel = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewVehicle.this);
            pDialog.setMessage("Updating Vehicle...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  VehicleModel doInBackground(Void... params){

            VehicleModel returnVehicleModel = null;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                VehicleModel result = service.UpdateVehicle(getApplicationContext(), vModel);
                if(result!=null){
                    returnVehicleModel = new VehicleModel(result.id, result.name, result.model_name, result.manufacturer_name, result.ownerId);
                    GlobalSection.vehicleDetail = returnVehicleModel;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return  returnVehicleModel;
        }

        @Override
        protected void onPostExecute(VehicleModel vm) {
            pDialog.dismiss();
            if(vm !=null){
                Toast.makeText(getApplicationContext(), "Vehicle updated", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Vehicle not updated", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(vm);
        }
    }

}
