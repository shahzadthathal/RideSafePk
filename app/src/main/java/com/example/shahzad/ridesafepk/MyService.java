package com.example.shahzad.ridesafepk;

/**
 * Created by shahzad on 1/8/2016.
 */

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MyService extends Service {

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            //Run ur code here
            timerHandler.postDelayed(this,15000);
            // WebServer Request URL
            //String serverURL = "http://172.16.0.64/webservice/index.php";

                Log.d("My Service","running");

                String serverURL = getApplicationContext().getResources().getString(R.string.SERVICE_URL) +"CheckNewRide/3071/Driver";
                //Log.d("My Service", serverURL+"");
                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);


            //Log.d("My Service","running");
          /* NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MyService.this)
                            // .setSmallIcon(R.drawable.ic_launcher)
                            .setSmallIcon(R.drawable.cast_ic_notification_1)
                            .setContentTitle("New Request Found")
                            .setContentText("you have a new ride request from "+" and PassengerID: 23423");

            Intent resultIntent = new Intent(MyService.this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
            */


        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        timerHandler.postDelayed(timerRunnable, 0);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


// Class with extends AsyncTask class

    private class LongOperation  extends AsyncTask<String, Void, RideModel> {

        protected void onPreExecute() {
            // Set Request parameter
//                data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();
        }

        // Call after onPreExecute method
        protected RideModel doInBackground(String... urls) {

            RideModel rideModel = null;
            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                rideModel = service.CheckNewRide(getApplicationContext(), User.loggedInUserId, User.loggedInUserType);
                GlobalSection.selectedRideDetail = rideModel;
            }catch (IOException e) {
            e.printStackTrace();
            }

            return rideModel;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(RideModel rideModel) {

            if(rideModel != null) {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MyService.this)
                                // .setSmallIcon(R.drawable.ic_launcher)
                                .setSmallIcon(R.drawable.cast_ic_notification_1)
                                .setContentTitle("New Request Found")
                                .setContentText("you have a new ride request from " + rideModel.from_destination +
                                        " and PassengerID: " + rideModel.passengerID);
                Intent resultIntent = new Intent(MyService.this, RideDetailNotify.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
                stackBuilder.addParentStack(RideDetailNotify.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());

                }
            }
        }

    }

