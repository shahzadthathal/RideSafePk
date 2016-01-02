package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AddVehicle extends AppCompatActivity implements View.OnClickListener{

    EditText txtName, txtModelName, txtManufacturerName;
    Button btnAddVehicle;
    ProgressDialog pDialog;
    User user;
    VehicleModel vehicleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtName = (EditText) findViewById(R.id.txtName);
        txtModelName = (EditText) findViewById(R.id.txtModelName);
        txtManufacturerName = (EditText) findViewById(R.id.txtManufacturerName);

        btnAddVehicle = (Button) findViewById(R.id.btnAddVehicle);
        btnAddVehicle.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnAddVehicle:

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
                    new SaveVehicle(vehicleModel).execute();
                }
                break;
        }
    }


    protected void onStart() {
        super.onStart();
        if(User.IsLoggedIn != true){
            startActivity(new Intent(this, Login.class));
        }
    }


    public  class SaveVehicle extends AsyncTask<Void, Void, VehicleModel> {
        VehicleModel vModel;

        public  SaveVehicle(VehicleModel v){
            this.vModel = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddVehicle.this);
            pDialog.setMessage("Adding Vehicle...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  VehicleModel doInBackground(Void... params){



            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                VehicleModel result = service.AddVehicle(getApplicationContext(), vModel);
                if(result!=null) {
                    //if(result.isError == 0) {
                    vehicleModel = new VehicleModel(result.id, result.name, result.model_name, result.manufacturer_name, result.ownerId);
                    //  User.IsLoggedIn = true;
                    //User.loggedInUserId = result.id;
                    //}
                }
            }catch (IOException e) {
                e.printStackTrace();

            }

            return  vehicleModel;
        }

        @Override
        protected void onPostExecute(VehicleModel vm) {
            pDialog.dismiss();
            goToNextActivityAddVehicle(vm);
            super.onPostExecute(vm);

        }
    }


    public void goToNextActivityAddVehicle(VehicleModel vm){

        if(vm == null){
            Toast.makeText(getApplicationContext(), "Vehicle not Aded found", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(this, ViewVehicle.class));
        }
    }

}
