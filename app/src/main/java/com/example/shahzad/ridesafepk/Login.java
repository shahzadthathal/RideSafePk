package com.example.shahzad.ridesafepk;

import android.app.ProgressDialog;
import android.content.Context;
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

public class Login extends AppCompatActivity implements View.OnClickListener{

        Button btnLogin, btnRegister;
        EditText txtEmail, txtPassword;
        TextView tvForgetPassword;
        ProgressDialog pDialog;
        User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        //tvRegister = (TextView) findViewById(R.id.tvRegister);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        tvForgetPassword.setOnClickListener(this);


    }

    // added by shahzad checkc user is loged in or not
    @Override
    protected void onStart() {
        super.onStart();
        if(User.IsLoggedIn ==true)
            startActivity(new Intent(this, MainActivity.class));
    }


    @Override
public void onClick(View v) {
        switch (v.getId())
        {
        case R.id.btnLogin:

            String email    = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            if(!user.isValidEmail(email)){
                Toast.makeText(getApplicationContext(),"Please enter valid email address",Toast.LENGTH_SHORT).show();
                return;
            }
            if (AppStatus.getInstance(this).isOnline()) {

                user = new User(email,password);
                new LoginUser(user).execute();

            } else {
                Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_SHORT).show();
                Log.v("Login.java", "############################You are not online!!!!");
            }
        break;

            case R.id.btnRegister:
            startActivity(new Intent(this, Register.class));
            break;

            case R.id.tvForgetPassword:
            startActivity(new Intent(this,ForgetPassword.class));
            break;

        }

        }

    public  class LoginUser extends  AsyncTask<Void, Void, User>{
        User user;

        public  LoginUser(User u){
            this.user = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Authenticating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  User doInBackground(Void... params){

            User returnUser = null;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                User result = service.Login(getApplicationContext(), user);
                if(result!=null) {
                        returnUser = new User(result.id, result.name, result.email, result.password, result.phone, result.nic, result.userType, result.street, result.city, result.country, result.lat, result.lng, result.is_login, result.is_vehicle_added, result.reg_id, result.isError, result.errorMessage);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            return  returnUser;
        }

        @Override
        protected void onPostExecute(User user) {
            pDialog.dismiss();
            goToNextActivity(user);
            super.onPostExecute(user);
        }
    }

    public void goToNextActivity(User user){

       if(user == null){
           Toast.makeText(getApplicationContext(),"No User found",Toast.LENGTH_SHORT).show();
       }
       else if(user.isError == 1){
           Toast.makeText(getApplicationContext(),user.errorMessage,Toast.LENGTH_SHORT).show();
       }
       else if(user.isError == 0) {

           User.IsLoggedIn = true;
           User.loggedInUserId = user.id;
           User.loggedInUserType = user.userType;

           if (user.userType.equals("Driver")) {
               //startActivity(new Intent(this, MainActivity.class));

              if(user.is_vehicle_added == 1){
                  Intent i = new Intent(this, MyService.class);
                  startService(i);
                  startActivity(new Intent(getApplicationContext(), ViewVehicle.class));
               }
               else{
                  startActivity(new Intent(this, AddVehicle.class));
               }

           } else {
               Intent i = new Intent(this, MyService.class);
               startService(i);
               startActivity(new Intent(this, BookRide.class));
           }
       }
       else{
           Toast.makeText(getApplicationContext(), "Network Error",Toast.LENGTH_LONG).show();
       }
    }
}

