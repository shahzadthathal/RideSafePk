package com.example.shahzad.ridesafepk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //User user;

    Button btnPassenger,btnDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPassenger = (Button) findViewById(R.id.btnPassenger);
        btnDriver = (Button) findViewById(R.id.btnDriver);

        btnPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PassengerActivity.class));
            }
        });

        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), DriverActivity.class));

            }
        });

    }


    protected  void onResume()
    {
        super.onResume();
    }

    // added by shahzad checkc user is loged in or not
    @Override
    protected void onStart()
    {
        //if(authenticate() !=true)
          //startActivity(new Intent(this,Login.class));
        super.onStart();
    }

}
