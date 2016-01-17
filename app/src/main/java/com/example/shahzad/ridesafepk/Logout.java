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
import android.widget.Toast;

import java.io.IOException;

public class Logout extends AppCompatActivity {

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new LogoutUser().execute();

    }

    public  class LogoutUser extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Logout.this);
            pDialog.setMessage("You will be signout in a moment,please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  Boolean  doInBackground(Void... params){

            Boolean res = false;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                Boolean  result  = service.Logout(getApplicationContext(), User.loggedInUserId);
                if (result) {
                    res = result;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            pDialog.dismiss();

            if(res)
            {
                Log.d("Logout.java",res+"");

                User.IsLoggedIn = false;
                User.loggedInUserType = "";

                GlobalSection.selectedRideDetail = null;
                GlobalSection.rideDetailBeforeSave = null;
                GlobalSection.driverDetail = null;
                GlobalSection.driversList = null;
                GlobalSection.rideHistoryList= null;

                GlobalSection.FromText = "";
                GlobalSection.ToText = "";
                GlobalSection.SelectedDriverID = 0;
                GlobalSection.rideDetailAfterSave = null;
                GlobalSection.FromLat = 0;
                GlobalSection.FromLong = 0;
                GlobalSection.ToLat = 0;
                GlobalSection.ToLong = 0;

                Toast.makeText(getApplicationContext(),"Logged out from app...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"Unable to loged out, please try latter...", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
