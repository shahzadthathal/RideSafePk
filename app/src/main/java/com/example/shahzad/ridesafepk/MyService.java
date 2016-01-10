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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MyService {

        /*extends Service {

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            //Run ur code here
            timerHandler.postDelayed(this, 15000);
            // WebServer Request URL
            String serverURL = "http://172.16.0.64/webservice/index.php";

            // Use AsyncTask execute Method To Prevent ANR Problem
            new LongOperation().execute(serverURL);
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

    private class LongOperation  extends AsyncTask<String, Void, Void> {
        private String Content;
        private String Error = null;


        protected void onPreExecute() {
            // Set Request parameter
//                data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            // Make Post Call To Web Server
            BufferedReader reader=null;

            // Send data
            try
            {
                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write( data );
//                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line);
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }

                catch(Exception ex) {}
            }


            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(Void unused) {

            if (Error != null) {


            } else {
                String OutputData = "";
                JSONObject jsonResponse;

                try {
                    jsonResponse = new JSONObject(Content);

                    String id       = jsonResponse.optString("id").toString();
                    String from_address     = jsonResponse.optString("from_address").toString();
                    String to_address = jsonResponse.optString("to_address").toString();
                    String passengerID       = jsonResponse.optString("passengerID").toString();
                    String driverID     = jsonResponse.optString("driverID").toString();
                    String date_created = jsonResponse.optString("date_created").toString();
                    String from_lat     = jsonResponse.optString("from_lat").toString();
                    String from_lng = jsonResponse.optString("from_lng").toString();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(MyService.this)
                                   // .setSmallIcon(R.drawable.ic_launcher)
                                    .setSmallIcon(R.drawable.cast_ic_notification_1)
                                    .setContentTitle("New Request Found")
                                    .setContentText("you have a new ride request from "+from_address+
                                            " and PassengerID: "+passengerID);
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

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }
    */
}

