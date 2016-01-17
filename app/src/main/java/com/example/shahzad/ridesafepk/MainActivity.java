package com.example.shahzad.ridesafepk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        if(User.IsLoggedIn ) {
            Log.d("User is ",User.loggedInUserType+"");
            Intent i = new Intent(this, MyService.class);
            startService(i);
        }
        */


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    protected  void onResume()
    {
        super.onResume();
    }

    // added by shahzad checkc user is loged in or not
    @Override
    protected void onStart() {

        if(authenticate() !=true)
          startActivity(new Intent(this,Login.class));
        super.onStart();
    }



    private boolean authenticate()
    {
        return User.IsLoggedIn;
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
                startActivity(new Intent(getApplication(), UpdateProfile.class));
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

}
