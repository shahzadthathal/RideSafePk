package com.example.shahzad.ridesafepk;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DriverDetail extends AppCompatActivity implements  View.OnClickListener{

    Button btnContactDriver;
    TextView tvName, tvPhone, tvDistance;
    ImageView mImageView;

    Chronometer chronometer;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        mImageView = (ImageView) findViewById(R.id.imageView);

        btnContactDriver = (Button) findViewById(R.id.btnContactDriver);
        btnContactDriver.setOnClickListener(this);

        chronometer = (Chronometer) findViewById(R.id.chronometer);


        if(GlobalSection.driverDetail == null)
        {
            finish();
        }
        else{
            tvName.setText("Name: "+GlobalSection.driverDetail.name);
            tvPhone.setText("Phone: "+GlobalSection.driverDetail.phone);
            tvDistance.setText(GlobalSection.driverDetail.distance.toString()+"km away");
            mImageView.setImageResource(R.drawable.pic1);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContactDriver:
                chronometer.start(); //chronometer.stop();
                Toast.makeText(this, "Conecting Driver, pleaase wait", Toast.LENGTH_SHORT).show();
        }
    }

        }
