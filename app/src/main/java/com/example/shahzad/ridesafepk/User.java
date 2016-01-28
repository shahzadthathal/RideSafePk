package com.example.shahzad.ridesafepk;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shahzad on 12/12/2015.
 */
public class User {

    Integer id;
    String name, email, password, phone, nic, userType;

    String street, city,country;
    Double  lat, lng;

    Integer is_login, is_vehicle_added;

    String reg_id;
   // Date register_date;
    String register_date;

    int isError;
    String errorMessage;

    public static boolean IsLoggedIn = false;
    public static int loggedInUserId = 0;
    public  static String loggedInUserType;

    String image;

    //constructor for set user data
    public  User(Integer id, String name, String email, String password, String phone, String nic, String userType, String street, String city, String country, Double  lat, Double  lng, Integer is_login, Integer is_vehicle_added, String reg_id, int isError, String errorMessage, String image)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nic = nic;
        this.userType = userType;
        this.street = street;
        this.city = city;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
        this.is_login = is_login;
        this.is_vehicle_added = is_vehicle_added;
       // this.register_date = register_date;
        this.reg_id = reg_id;
        this.image = image;

    }

    //constructor for register user date
    public  User(String name, String email, String password, String phone, String nic, String userType, String image)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nic = nic;
        this.userType = userType;
        this.image = image;
    }
    //constructor for sending loagin detail
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    //constructor for saving user address
    public User(int id, String street, String city, String country, Double  lat, Double  lng)
    {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
        this.lat = lat;
        this.lng = lng;

    }


    //constructor for update  user profile
    public  User(Integer id, String name, String email, String password, String phone, String nic, String image)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nic = nic;
        this.image = image;
    }

    public static int userId(){
        if(User.IsLoggedIn == true)
            return loggedInUserId;
        else
            return 0;
    }
    public  boolean checkUserLoggedIn(){
        if(User.IsLoggedIn == true)
            return true;
        else
             return false;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public final  static boolean isValidNicNumber(CharSequence target)
    {
        //32303-7101329-7
       // String regexStr = "^[\\ddddd-\\ddddddd-\\d]$";
        //String regexStr = "^[\\d{5}[-]?\\d{7}[-]?\\d{1}]$";
        String regexStr = "^(\\d{5}-)(\\d{7}-)(\\d{1})$";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(target);

        if(target == null){
            return false;
        }
        else  if (target.length() < 13 || target.length() > 15) {
            return  false;
        }
       /* else if(!matcher.matches())
        {
            return  false;
        }
        */
        else{
            return  true;
        }
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,70 , byteArrayOutputStream);

        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        return encodedImage;
    }

}

