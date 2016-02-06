package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by shahzad on 12/12/2015.
 */
public class WebserviceHandler {

    String _error;

    private String Content;
    private String Error = null;


    public  WebserviceHandler(Context context)
    {

    }

    /*private String GetJsonFromUrl(String url) {

        // added by Sir Shahid, to avoid some http request stoped by java
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        // TODO Auto-generated method stub

        InputStream is =null;
        String result="";

        try
        {
            StringBuilder builder = new StringBuilder();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type","application/json");
            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        }catch(Exception e)
        {
            _error=e.getLocalizedMessage();
            Log.e("log_tag", "Error in Http Connection" + e.toString());
        }

        ////////  convert response to String

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb =new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            is.close();
            result = sb.toString();

        } catch(Exception e)
        {
            _error=e.getMessage();
            Log.e("log_tag", "Error Converting Result"+ e.toString());
        }

        return result;

    }*/


    private String GetJsonFromUrl(String requestedUrl)
    {
        // added by Sir Shahid, to avoid some http request stoped by java
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestedUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Excep while read url", e.toString());
        } finally {

            try
            {
                iStream.close();
                urlConnection.disconnect();
            }
            catch(Exception ex) {}
        }
        return data;
    }

    public String postData(String urlpath, String json)
    {
        Log.d("posting data",json.toString());
        Log.d("postingContentLength", Integer.toString(json.toString().getBytes().length)+"");


        String data = "";
        HttpURLConnection connection = null;

        try {
            URL url=new URL(urlpath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
           // connection.setRequestProperty("Content-Length", "" + Integer.toString(json.toString().getBytes().length));
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());

            streamWriter.write(json.toString());
            streamWriter.flush();

            StringBuilder stringBuilder = new StringBuilder();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                while ((data = bufferedReader.readLine()) != null) {
                    stringBuilder.append(data);
                }
                bufferedReader.close();

                Log.d("HTTP_OK response", stringBuilder.toString());
                return stringBuilder.toString();
            } else {
                Log.e("HTTP_ERROR response", connection.getResponseMessage());
                return null;
            }
        } catch (Exception exception){
            Log.e("test", exception.toString());
            return null;
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    public User RegisterPost(Context context, User rUser) throws UnsupportedEncodingException{

        String url = context.getResources().getString(R.string.SERVICE_URL)+ "Signup";
        try
        {
            JSONObject newUser =  new JSONObject();
            newUser.put("name", rUser.name);
            newUser.put("email", rUser.email);
            newUser.put("password", rUser.password);
            newUser.put("phone",  rUser.phone);
            newUser.put("nic", rUser.nic);
            newUser.put("userType", rUser.userType);
            newUser.put("image", rUser.image);

            String response = postData(url, newUser.toString());
            Log.d("response", response);

            if(response!="")
            {
                try {
                    JSONObject user = new JSONObject(response);
                    User returnUser = new User( user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getInt("is_login"),user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"), user.getString("image"));
                    return returnUser;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return  null;
    }



    /*public User Register(Context context,User rUser) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "Register/" + URLEncoder.encode(String.valueOf(rUser.name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.email), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.password), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.phone), "UTF-8") +"/" + URLEncoder.encode(String.valueOf(rUser.nic), "UTF-8") + "/" + rUser.userType;//+ "/" + URLEncoder.encode(String.valueOf(rUser.image), "UTF-8")
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Register Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User( user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getInt("is_login"),user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"), user.getString("image"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/


    public User UpdateProfile(Context context,User rUser) throws UnsupportedEncodingException
    {
       // String url = context.getResources().getString(R.string.SERVICE_URL)+ "UpdateProfile/"+ rUser.id +"/"+ URLEncoder.encode(String.valueOf(rUser.name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.email), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.password), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.phone), "UTF-8") +"/" + URLEncoder.encode(String.valueOf(rUser.nic), "UTF-8");
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "UpdateProfile";
        try {
            JSONObject newUser =  new JSONObject();
            newUser.put("id",rUser.id);
            newUser.put("name", rUser.name);
            newUser.put("email", rUser.email);
            newUser.put("password", rUser.password);
            newUser.put("phone",  rUser.phone);
            newUser.put("nic", rUser.nic);
            newUser.put("userType", rUser.userType);
            newUser.put("image", rUser.image);

            String response = postData(url, newUser.toString());

            if (response != "")
            {
                try {
                    JSONObject user = new JSONObject(response);
                    User returnUser = new User(user.getInt("id"), user.getString("name"), user.getString("email"), user.getString("password"), user.getString("phone"), user.getString("nic"), user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")), user.getInt("is_login"), user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"), user.getString("image"));
                    return returnUser;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /*public User UpdateProfile(Context context,User rUser) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "UpdateProfile/"+ rUser.id +"/"+ URLEncoder.encode(String.valueOf(rUser.name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.email), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.password), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(rUser.phone), "UTF-8") +"/" + URLEncoder.encode(String.valueOf(rUser.nic), "UTF-8");
        Log.d("Update Profile Url", url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Update Profile Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User( user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getInt("is_login"),user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"), user.getString("image"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/


    public User saveAddress(Context context,User u) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "Saveaddress/" +u.id + "/" + URLEncoder.encode(String.valueOf(u.street), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(u.city), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(u.country), "UTF-8") +"/" + u.lat + "/" + u.lng;
        Log.d("save address url", url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Save  address Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User(user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")), user.getInt("is_login"),user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"), user.getString("image"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public RideModel FinishRide(Context context, RideModel rideModel ) throws UnsupportedEncodingException {
       // String url = context.getResources().getString(R.string.SERVICE_URL)+ "FinishRide/" + rideModel.id  +"/"+  rideModel.amount +"/"+  URLEncoder.encode(String.valueOf(rideModel.review), "UTF-8") +"/"+ rideModel.rating;
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "FinishRide";

        Log.d("ChangeRideStatus",url);
        //String jsonResult = GetJsonFromUrl(url);

        JSONObject postRide = new JSONObject();
        try {
            postRide.put("id", rideModel.id);
            postRide.put("amount", rideModel.amount);
            postRide.put("review", rideModel.review);
            postRide.put("rating", rideModel.rating);

            String response = postData(url, postRide.toString());

            //String response = GetJsonFromUrl(url);

            if (response != null) {
                JSONObject ride;
                try {
                    ride = new JSONObject(response);
                    Integer id = ride.getInt("id");
                    Integer passengerID = ride.getInt("passengerID");
                    Integer driverID = ride.getInt("driverID");
                    String from_address = ride.getString("from_destination");
                    String to_address = ride.getString("to_destination");
                    double from_lat = ride.getDouble("from_lat");
                    double from_lng = ride.getDouble("from_lng");
                    double to_lat = ride.getDouble("to_lat");
                    double to_lng = ride.getDouble("to_lng");
                    Integer status = ride.getInt("status");
                    String review = ride.getString("review");
                    //float amount = ride.isNull("amount") ? 0 : Float.valueOf(ride.getString("amount"));
                    //float rating = ride.isNull("rating") ? 0 : Float.valueOf(ride.getString("rating"));

                    float amount, rating;

                    if (ride.getString("amount").equals("") || ride.isNull("amount")) {
                        amount = 0;
                    } else {
                        amount = Float.valueOf(ride.getString("amount"));
                    }

                    if (ride.getString("rating").equals("") || ride.isNull("rating")) {
                        rating = 0;
                    } else {
                        rating = Float.valueOf(ride.getString("rating"));
                    }

                    String driver_name = ride.getString("driver_name");
                    String passenger_name = ride.getString("passenger_name");

                    // String date_created = jsonResponse.optString("date_created");
                    RideModel returnRide = new RideModel(id, passengerID, driverID, from_address, to_address, from_lat, from_lng, to_lat, to_lng, status, amount, review, rating, driver_name, passenger_name);
                    return returnRide;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public RideModel ChangeRideStatus(Context context, Integer rideId, Integer userId, Integer rstatus ) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "ChangeRideStatus/" + rideId  +"/"+  userId +"/"+ rstatus;
        Log.d("ChangeRideStatus",url);
        String jsonResult = GetJsonFromUrl(url);
        if (jsonResult != null) {
            JSONObject ride;
            try {
                ride = new JSONObject(jsonResult);
                Integer id          = ride.getInt("id");
                Integer passengerID = ride.getInt("passengerID");
                Integer driverID = ride.getInt("driverID");
                String from_address = ride.getString("from_destination");
                String to_address   = ride.getString("to_destination");
                double from_lat     = ride.getDouble("from_lat");
                double from_lng     = ride.getDouble("from_lng");
                double to_lat       = ride.getDouble("to_lat");
                double to_lng = ride.getDouble("to_lng");
                Integer status      = ride.getInt("status");
                String review = ride.getString("review");
                //float amount = ride.isNull("amount") ? 0 : Float.valueOf(ride.getString("amount"));
                //float rating = ride.isNull("rating") ? 0 : Float.valueOf(ride.getString("rating"));

                float amount, rating;

                if(ride.getString("amount").equals("") || ride.isNull("amount")) {
                    amount =  0;
                }
                else{
                    amount = Float.valueOf(ride.getString("amount"));
                }

                if(ride.getString("rating").equals("") || ride.isNull("rating")) {
                    rating = 0;
                }
                else{
                    rating = Float.valueOf(ride.getString("rating"));
                }

                String driver_name = ride.getString("driver_name");
                String passenger_name = ride.getString("passenger_name");

                // String date_created = jsonResponse.optString("date_created");
                RideModel returnRide = new RideModel(id, passengerID, driverID, from_address, to_address, from_lat, from_lng, to_lat,to_lng, status, amount , review, rating, driver_name, passenger_name);
                return returnRide;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public RideModel CheckNewRide(Context context,Integer userId, String userType ) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "CheckNewRide/" +   userId + "/" +  userType;
        Log.d("Ride detail url",url);
        String jsonResult = GetJsonFromUrl(url);

        Log.d("Ride Detail service", "Result: "+jsonResult.toString());
        if (jsonResult != null) {
            JSONObject ride;
            try {

                ride = new JSONObject(jsonResult);
               // if(!ride.isNull("id"))
                if(ride != null)
                {

                    Integer id = ride.getInt("id");
                    Integer passengerID = ride.getInt("passengerID");
                    Integer driverID = ride.getInt("driverID");
                    String from_address = ride.getString("from_destination");
                    String to_address = ride.getString("to_destination");
                    double from_lat = ride.getDouble("from_lat");
                    double from_lng = ride.getDouble("from_lng");
                    double to_lat = ride.getDouble("to_lat");
                    double to_lng = ride.getDouble("to_lng");
                    Integer status = ride.getInt("status");
                    String review = ride.getString("review");
                    //float amount = ride.isNull("amount") ? 0 : Float.valueOf(ride.getString("amount"));
                    //float rating = ride.isNull("rating") ? 0 : Float.valueOf(ride.getString("rating"));

                    float amount, rating;

                    if (ride.getString("amount").equals("") || ride.isNull("amount")) {
                        amount = 0;
                    } else {
                        amount = Float.valueOf(ride.getString("amount"));
                    }

                    if (ride.getString("rating").equals("") || ride.isNull("rating")) {
                        rating = 0;
                    } else {
                        rating = Float.valueOf(ride.getString("rating"));
                    }

                    String driver_name = ride.getString("driver_name");
                    String passenger_name = ride.getString("passenger_name");

                    // String date_created = jsonResponse.optString("date_created");
                    RideModel returnRide = new RideModel(id, passengerID, driverID, from_address, to_address, from_lat, from_lng, to_lat, to_lng, status, amount, review, rating, driver_name, passenger_name);
                    return returnRide;
                }
                else{
                    return  null;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<RideModel> GetAllRides(Context context,Integer userId, String userType ) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "GetAllRides/" +   userId + "/" +  userType;
        Log.d("GetAllRide url",url);
        String jsonResponse = GetJsonFromUrl(url);
        Log.d("GetAllRide Response",jsonResponse);
        if(jsonResponse!="" && jsonResponse !="[]")
        {
            ArrayList<RideModel> rides = new ArrayList<RideModel>();
            try {
                JSONArray ridesArr = new JSONArray(jsonResponse);

                for(int i=0; i < ridesArr.length(); i++) {
                    JSONObject ride = ridesArr.getJSONObject(i);

                    float amount, rating;

                    if(ride.getString("amount").equals("") || ride.isNull("amount")) {
                         amount =  0;
                    }
                    else{
                        amount = Float.valueOf(ride.getString("amount"));
                    }

                    if(ride.getString("rating").equals("") || ride.isNull("rating")) {
                         rating = 0;
                    }
                    else{
                        rating = Float.valueOf(ride.getString("rating"));
                    }

                    String driver_name = ride.getString("driver_name");
                    String passenger_name = ride.getString("passenger_name");

                    rides.add(new RideModel(ride.getInt("id"), ride.getInt("passengerID"), ride.getInt("driverID") ,  ride.getString("from_destination"), ride.getString("to_destination"), ride.getDouble("from_lat"), ride.getDouble("from_lng"),  ride.getDouble("to_lat"), ride.getDouble("to_lng"), ride.getInt("status"), amount, ride.getString("review"), rating, driver_name, passenger_name));
                }
                return rides;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public RideModel AddRide(Context context,RideModel obj) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "AddRide/" + obj.passengerID  + "/" + obj.driverID + "/" +  URLEncoder.encode(String.valueOf(obj.from_destination), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(obj.to_destination), "UTF-8") +"/" + obj.from_lat + "/" + obj.from_lng +"/"+ obj.to_lat +"/"+ obj.to_lng;
        URLEncoder.encode(url, "UTF-8");
        Log.d("Add Job Url", url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Add job Response", jsonResult + "");
        if (jsonResult != null) {
            JSONObject ride;
            try {
                ride = new JSONObject(jsonResult);
                Integer id          = ride.getInt("id");
                Integer passengerID = ride.getInt("passengerID");
                Integer driverID = ride.getInt("driverID");
                String from_address = ride.getString("from_destination");
                String to_address   = ride.getString("to_destination");
                double from_lat     = ride.getDouble("from_lat");
                double from_lng     = ride.getDouble("from_lng");
                double to_lat       = ride.getDouble("to_lat");
                double to_lng = ride.getDouble("to_lng");
                Integer status      = ride.getInt("status");
                String review = ride.getString("review");
                //float amount = ride.isNull("amount") ? 0 : Float.valueOf(ride.getString("amount"));
                //float rating = ride.isNull("rating") ? 0 : Float.valueOf(ride.getString("rating"));

                float amount, rating;

                if(ride.getString("amount").equals("") || ride.isNull("amount")) {
                    amount =  0;
                }
                else{
                    amount = Float.valueOf(ride.getString("amount"));
                }

                if(ride.getString("rating").equals("") || ride.isNull("rating")) {
                    rating = 0;
                }
                else{
                    rating = Float.valueOf(ride.getString("rating"));
                }

                String driver_name = ride.getString("driver_name");
                String passenger_name = ride.getString("passenger_name");

               // String date_created = jsonResponse.optString("date_created");
                RideModel returnRide = new RideModel(id, passengerID, driverID, from_address, to_address, from_lat, from_lng, to_lat,to_lng,status, amount , review, rating, driver_name, passenger_name);
                return returnRide;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<DriverModel> FindDriversRequest(Context context, double fromlat, double fromlng) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "GetDrivers/" +   fromlat + "/" +  fromlng;
        URLEncoder.encode(url, "UTF-8");
        Log.d("Find driver url",url);
        String jsonResponse = GetJsonFromUrl(url);
        Log.d("Drivers Response",jsonResponse);
        if(jsonResponse!="")
        {
            ArrayList<DriverModel> drivers = new ArrayList<DriverModel>();
            try {
                JSONArray driverArr = new JSONArray(jsonResponse);
                double  avg_rating;
                for(int i=0; i < driverArr.length(); i++) {
                    JSONObject driver = driverArr.getJSONObject(i);

                    if(driver.getString("rating").equals("") || driver.isNull("rating")) {
                        avg_rating = 0;
                    }
                    else{
                        avg_rating = Double.valueOf(driver.getString("rating"));
                    }

                    drivers.add(new DriverModel(driver.getInt("id"), driver.getString("name"),
                            driver.getString("phone"),
                            Double.parseDouble(driver.getString("distance")),
                            avg_rating,
                            driver.getString("image")
                            ,Double.parseDouble(driver.getString("lat")), Double.parseDouble(driver.getString("lng")),
                            driver.getInt("isAvailable")
                    ));
                }
                return drivers;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public Boolean  Logout(Context context, int userid) {
        String url = context.getResources().getString(R.string.SERVICE_URL) + "logout/" + userid;
        Log.d("Logout url", url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("logout res", jsonResult + "");
        if (jsonResult != null && jsonResult.contains("true")) {
            return true;
        }
        else
            return false;
    }
    public User Login(Context context, User loginUser) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "authenticate/" +  URLEncoder.encode(String.valueOf(loginUser.email), "UTF-8") + "/" +  URLEncoder.encode(String.valueOf(loginUser.password), "UTF-8");
        Log.d("Login url",url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Login Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User(user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"),Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getInt("is_login"), user.getInt("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage") , user.getString("image"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public VehicleModel AddVehicle(Context context, VehicleModel vm)  throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "AddVehicle/" + URLEncoder.encode(String.valueOf(vm.name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(vm.model_name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(vm.manufacturer_name), "UTF-8") + "/" + vm.ownerId;
        Log.d("save vehicle url",url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Save  vehicle Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject vehicleObj = new JSONObject(jsonResult);
                VehicleModel returnVehicle = new VehicleModel(vehicleObj.getInt("id"),vehicleObj.getString("name"),vehicleObj.getString("model_name"),vehicleObj.getString("manufacturer_name"),vehicleObj.getInt("ownerId"));
                return returnVehicle;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public VehicleModel UpdateVehicle(Context context, VehicleModel vm)  throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "UpdateVehicle/"+ vm.id +"/" + URLEncoder.encode(String.valueOf(vm.name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(vm.model_name), "UTF-8") + "/" + URLEncoder.encode(String.valueOf(vm.manufacturer_name), "UTF-8") + "/" + vm.ownerId;
        Log.d("update vehicle url",url);
        String jsonResult = GetJsonFromUrl(url);
        if(jsonResult!="")
        {
            try {
                JSONObject vehicleObj = new JSONObject(jsonResult);
                VehicleModel returnVehicle = new VehicleModel(vehicleObj.getInt("id"),vehicleObj.getString("name"),vehicleObj.getString("model_name"),vehicleObj.getString("manufacturer_name"),vehicleObj.getInt("ownerId"));
                return returnVehicle;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public VehicleModel GetVehicleDetail(Context context, int ownerID) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+"GetVehicleDetail/"+ ownerID;
        Log.d("get vehicle detail",url);
        String jsonResult = GetJsonFromUrl(url);
        if(jsonResult !="")
        {
            try{
                JSONObject vehicleObj = new JSONObject(jsonResult);
                VehicleModel returnVehicle = new VehicleModel(vehicleObj.getInt("id"),vehicleObj.getString("name"),vehicleObj.getString("model_name"),vehicleObj.getString("manufacturer_name"),vehicleObj.getInt("ownerId"));
                return returnVehicle;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  null;
    }

}
