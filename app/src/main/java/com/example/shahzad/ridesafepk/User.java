package com.example.shahzad.ridesafepk;

import java.util.Date;

/**
 * Created by shahzad on 12/12/2015.
 */
public class User {

    Integer id;
    String name, email, password, phone, nic, userType;

    String street, city,country;
    Double  lat, lng;

    String is_login,is_vehicle_added,reg_id;
   // Date register_date;
    String register_date;

    int isError;
    String errorMessage;

    public static boolean IsLoggedIn = false;
    public static int loggedInUserId = 0;

    //constructor for set user data
    public  User(Integer id, String name, String email, String password, String phone, String nic, String userType, String street, String city, String country, Double  lat, Double  lng, String is_login, String is_vehicle_added, String reg_id, int isError, String errorMessage)
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

    }

    //constructor for register user date
    public  User(String name, String email, String password, String phone, String nic, String userType)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nic = nic;
        this.userType = userType;
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
}

