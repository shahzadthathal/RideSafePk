package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;

public class Logout extends AppCompatActivity {

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    /*
    public  class LogoutUser extends AsyncTask<boolean, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Logout.this);
            pDialog.setMessage("You will be signout in a moment,please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  boolean doInBackground(Void... params){

                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                boolean result = service.Logout(getApplicationContext(), User.loggedInUserId);
                if(result!=false) {
                    return  true;
                }
                else{
                    return  false;
                }
        }

        @Override
        protected void onPostExecute(boolean res) {
            super.onPostExecute(res);
            pDialog.dismiss();

        }
    }*/

}
