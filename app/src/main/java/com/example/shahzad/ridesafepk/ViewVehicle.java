package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        btnUpdateVehicle = (Button) findViewById(R.id.btnAddVehicle);

        vehicleModel = vehicleModel.vehicleDetail();
        if(vehicleModel!=null) {

            txtName.setText(vehicleModel.name.toString());
            txtModelName.setText(vehicleModel.model_name.toString());
            txtManufacturerName.setText(vehicleModel.manufacturer_name.toString());
        }
        btnUpdateVehicle.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnUpdateVehicle:

                String name    = txtName.getText().toString();
                String modelName = txtModelName.getText().toString();
                String manufacturerName = txtManufacturerName.getText().toString();
                Integer ownerId = User.loggedInUserId;

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
                    VehicleModel vehicleModel = new VehicleModel(name, modelName, manufacturerName, ownerId);
                    new UpdateVehicle(vehicleModel).execute();
                }
                break;
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
                if(result!=null) {
                    //if(result.isError == 0) {
                    returnVehicleModel = new VehicleModel(result.id, result.name, result.model_name, result.manufacturer_name, result.ownerId);
                    //  User.IsLoggedIn = true;
                    //User.loggedInUserId = result.id;
                    //}
                }
            }catch (IOException e) {
                e.printStackTrace();

            }

            return  returnVehicleModel;
        }

        @Override
        protected void onPostExecute(VehicleModel vm) {
            pDialog.dismiss();
            super.onPostExecute(vm);

        }
    }


}
