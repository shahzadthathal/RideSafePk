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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener{


    EditText etName, etEmail, etPassword, etPhone, etNic, etUserType;
    Button btnUpdateProfile;
    Button btnUpdateAddress;
    ProgressDialog pDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!User.IsLoggedIn)
        {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etNic = (EditText) findViewById(R.id.etNic);

        if(GlobalSection.userProfile !=null)
        {

            etName.setText(GlobalSection.userProfile.name.toString());
            etEmail.setText(GlobalSection.userProfile.email.toString());
            etPassword.setText(GlobalSection.userProfile.password.toString());
            etPhone.setText(GlobalSection.userProfile.phone.toString());
            etNic.setText(GlobalSection.userProfile.nic.toString());

        }
        else{
            Toast.makeText(getApplicationContext(),"Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplication(), Login.class));
        }


        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(this);

        btnUpdateAddress = (Button) findViewById(R.id.btnUpdateAddress);
        btnUpdateAddress.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId())
        {
               case R.id.btnUpdateProfile:
                String name     = etName.getText().toString();
                String email    = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phone    = etPhone.getText().toString();
                String nic      = etNic.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!User.isValidEmail(email)){
                    Toast.makeText(getApplicationContext(),"Please enter valid email address",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!User.isValidPhoneNumber(phone))
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid phone number, > 6 and less than 13", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!User.isValidNicNumber(nic))
                {
                    Toast.makeText(getApplicationContext(), "Valid NIC number required, xxxxx-xxxxxxx-x", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(nic.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter NIC Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    user = new User(User.loggedInUserId, name, email, password, phone, nic);
                    new UserUpdateProfile(user).execute();
                }
                break;

            /*case R.id.btnLogin:
                startActivity(new Intent(this, Login.class));
                break;*/

            case R.id.btnUpdateAddress:
                startActivity(new Intent(this, UpdateAddress.class));
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


    public  class UserUpdateProfile extends AsyncTask<Void, Void, User> {
        User _user;

        public  UserUpdateProfile(User u)
        {
            _user = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateProfile.this);
            pDialog.setMessage("Updating your profile information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  User doInBackground(Void... params){

            try {

                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                User result = service.UpdateProfile(getApplicationContext(), _user);
                if(result!=null)
                {
                    _user = new User(result.id, result.name, result.email, result.password, result.phone, result.nic, result.userType, result.street, result.city, result.country,result.lat, result.lng, result.is_login, result.is_vehicle_added, result.reg_id,result.isError, result.errorMessage);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            return  _user;
        }

        @Override
        protected void onPostExecute(User user)
        {
            pDialog.dismiss();
            super.onPostExecute(user);

            if(user!=null)
            {
                Toast.makeText(getApplicationContext(), "Profile update", Toast.LENGTH_SHORT).show();
                GlobalSection.userProfile = user;

                etName.setText(user.name.toString());
                etEmail.setText(user.email.toString());
                etPassword.setText(user.password.toString());
                etPhone.setText(user.phone.toString());
                etNic.setText(user.nic.toString());
            }
            else{
                Toast.makeText(getApplicationContext(), "Profile not updated", Toast.LENGTH_SHORT).show();
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
