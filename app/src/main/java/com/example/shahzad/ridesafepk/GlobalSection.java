package com.example.shahzad.ridesafepk;

import java.util.ArrayList;

/**
 * Created by shahzad on 12/29/2015.
 */
public class GlobalSection {

    public static double FromLong,ToLong;
    public static double FromLat,ToLat;
    public static String FromText;
    public static String ToText;

    public  static ArrayList<DriverModel> driversList;

    public static Integer SelectedDriverID;

    public  static DriverModel driverDetail;

    public static  VehicleModel vehicleDetail;

    public  static RideModel rideDetailBeforeSave;

    public static RideModel rideDetailAfterSave;

    public  static ArrayList<RideModel> rideHistoryList;

    public static  RideModel selectedRideDetail;

}
