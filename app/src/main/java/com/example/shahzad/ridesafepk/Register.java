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
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText etName, etEmail, etPassword, etPhone, etNic, etUserType;
    Button btnRegister, btnLogin;
    ProgressDialog pDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etNic = (EditText) findViewById(R.id.etNic);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {


            case R.id.btnRegister:
                String name     = etName.getText().toString();
                String email    = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phone    = etPhone.getText().toString();
                String nic      = etNic.getText().toString();
                String userType = "";
                RadioButton passenger = (RadioButton) findViewById(R.id.radio_passenger);
                RadioButton driver = (RadioButton) findViewById(R.id.radio_driver);

                if(passenger.isChecked()){
                    userType = "Passenger";
                }
                else if (driver.isChecked()){
                    userType = "Driver";
                }

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
                else if(nic.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter NIC Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (userType.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select user type", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    user = new User(name, email, password, phone, nic, userType);
                    new RegisterUser(user).execute();
                }
                break;

            case R.id.btnLogin:
                startActivity(new Intent(this, Login.class));
                break;

            }

    }


    public  class RegisterUser extends AsyncTask<Void, Void, User> {
        User user;

        public  RegisterUser(User u)
        {
            this.user = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating Account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  User doInBackground(Void... params){

            User returnUser = null;

            try {
                Log.d("regiestring user",user.userType);
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                User result = service.Register(getApplicationContext(), user);
                if(result!=null) {
                    returnUser = new User(result.id, result.name, result.email, result.password, result.phone, result.nic, result.userType, result.street, result.city, result.country,result.lat, result.lng, result.is_login, result.is_vehicle_added, result.reg_id,result.isError, result.errorMessage);
                  //  User.IsLoggedIn=true;
                    //User.loggedInUserId = result.id;
                }
            }catch (IOException e) {
                e.printStackTrace();

            }

            return  returnUser;
        }

        @Override
        protected void onPostExecute(User user) {
            pDialog.dismiss();
            goToNextActivityRegister(user);
            super.onPostExecute(user);

        }
    }

    public void goToNextActivityRegister(User user){
        Log.d("return user", user + "");

        if(user == null){
            Toast.makeText(getApplicationContext(),"Error creating user",Toast.LENGTH_SHORT).show();
        }
        else if(user.isError == 1){
            Toast.makeText(getApplicationContext(),user.errorMessage,Toast.LENGTH_SHORT).show();
        }
        else if(user.isError == 0) {
            User.IsLoggedIn = true;
            User.loggedInUserId = user.id;
            startActivity(new Intent(this,AddAddress.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_LONG).show();
        }
    }
}
