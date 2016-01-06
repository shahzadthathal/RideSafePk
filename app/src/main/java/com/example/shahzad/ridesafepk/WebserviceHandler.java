package com.example.shahzad.ridesafepk;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by shahzad on 12/12/2015.
 */
public class WebserviceHandler {

    String _error;

    public  WebserviceHandler(Context context)
    {

    }

    private String GetJsonFromUrl(String url) {

        // added by Sir Shahid, to avoid some http request stoped by java
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        // TODO Auto-generated method stub
        InputStream is =null;
        String result="";
        JSONObject jArray=null;

////////// http get

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
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e)
        {
            _error=e.getMessage();
            Log.e("log_tag", "Error Converting Result"+ e.toString());
        }




        result=result.replace("\\u000d\\u000a", "\n");
        result=result.replace("\"", "");
        result = result.replace("/", "");
        result = result.replaceAll("/", "");
        return result;
        //return jArray;
    }


    public ArrayList<DriverModel> FindDriversRequest(Context context, double fromlat, double fromlng) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "GetDrivers/" +  URLEncoder.encode(String.valueOf(fromlat)) + "/" + URLEncoder.encode(String.valueOf(fromlng));
        Log.d("find driver url",url);
        String jsonResponse = GetJsonFromUrl(url);
        Log.d("Drivers Response",jsonResponse);
        if(jsonResponse!="")
        {
            ArrayList<DriverModel> drivers = new ArrayList<DriverModel>();
            try {
                JSONArray driverArr = new JSONArray(jsonResponse);
                for(int i=0; i < driverArr.length(); i++) {
                    JSONObject driver = driverArr.getJSONObject(i);
                    drivers.add(new DriverModel(driver.getInt("id"),driver.getString("name"),driver.getString("phone"),Double.parseDouble(driver.getString("distance"))));
                }
                return drivers;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public User Login(Context context, User loginUser) throws UnsupportedEncodingException {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "authenticate/" + URLEncoder.encode(loginUser.email) + "/" + loginUser.password;
        Log.d("Login url",url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Login Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User(user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"),Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getString("is_login"),user.getString("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User Register(Context context,User rUser) throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "Register/" + Uri.encode(rUser.name) + "/" + rUser.email + "/" + rUser.password + "/" + rUser.phone +"/" + rUser.nic + "/" + rUser.userType;
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Register Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User( user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")),user.getString("is_login"),user.getString("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User saveAddress(Context context,User u) throws UnsupportedEncodingException
    {
      String url = context.getResources().getString(R.string.SERVICE_URL)+ "Saveaddress/" +URLEncoder.encode(String.valueOf(u.id)) + "/" + URLEncoder.encode(String.valueOf(u.street)) + "/" + Uri.encode(u.city) + "/" + Uri.encode(u.country) +"/" + URLEncoder.encode(String.valueOf(u.lat)) + "/" + URLEncoder.encode(String.valueOf(u.lng));
        //String url = context.getResources().getString(R.string.SERVICE_URL)+ "Saveaddress/" +URLEncoder.encode(String.valueOf(u.id)) + "/street102/" + Uri.encode(u.city) + "/" + Uri.encode(u.country) +"/3978522/799555";
        Log.d("save address url",url);
        String jsonResult = GetJsonFromUrl(url);
        Log.d("Save  address Response",jsonResult);
        if(jsonResult!="")
        {
            try {
                JSONObject user = new JSONObject(jsonResult);
                User returnUser = new User(user.getInt("id"),user.getString("name"),user.getString("email"),user.getString("password"),user.getString("phone"),user.getString("nic"),user.getString("userType"), user.getString("street"), user.getString("city"), user.getString("country"), Double.parseDouble(user.getString("lat")), Double.parseDouble(user.getString("lng")), user.getString("is_login"),user.getString("is_vehicle_added"), user.getString("reg_id"), user.getInt("isError"), user.getString("errorMessage"));
                return returnUser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public VehicleModel AddVehicle(Context context, VehicleModel vm)  throws UnsupportedEncodingException
    {
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "AddVehicle/" +URLEncoder.encode(String.valueOf(vm.name)) + "/" + URLEncoder.encode(String.valueOf(vm.model_name)) + "/" + URLEncoder.encode(String.valueOf(vm.manufacturer_name)) + "/" + URLEncoder.encode(String.valueOf(vm.ownerId));
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
        String url = context.getResources().getString(R.string.SERVICE_URL)+ "UpdateVehicle/"+ URLEncoder.encode(String.valueOf(vm.id))+"/" +URLEncoder.encode(String.valueOf(vm.name)) + "/" + URLEncoder.encode(String.valueOf(vm.model_name)) + "/" + URLEncoder.encode(String.valueOf(vm.manufacturer_name)) + "/" + URLEncoder.encode(String.valueOf(vm.ownerId));
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
        String url = context.getResources().getString(R.string.SERVICE_URL)+"GetVehicleDetail/"+URLEncoder.encode(String.valueOf(ownerID));
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
