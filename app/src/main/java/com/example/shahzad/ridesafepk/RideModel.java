package com.example.shahzad.ridesafepk;

import java.util.Date;

/**
 * Created by shahzad on 1/7/2016.
 */
public class RideModel {

    Integer id, passengerID, driverID, total_miles;

    String from_destination, to_destination;

    double from_lat,from_lng, to_lat, to_lng;

   // Date reserve_date_start, reserve_date_end, created_date;

    int status;

    float amount, rating;

    String review;



    public  RideModel(Integer id, float amount, float rating, String review)
    {
        this.id = id;
        this.amount = amount;
        this.rating = rating;
        this.review = review;
    }

    //save first time job without acceptene of any driver

    public  RideModel(Integer passengerID, Integer driverID, String from_destination, String to_destination, double from_lat, double from_lng, double to_lat, double to_lng)
    {
        this.passengerID = passengerID;
        this.driverID = driverID;
        this.from_destination = from_destination;
        this.to_destination = to_destination;
        this.from_lat = from_lat;
        this.from_lng = from_lng;
        this.to_lat = to_lat;
        this.to_lng = to_lng;
    }

    //save ride detail after recored added into db

    public  RideModel(Integer id, Integer passengerID, Integer driverID, String from_destination, String to_destination, double from_lat, double from_lng, double to_lat, double to_lng, int status, float amount, String review, float rating)
    {
        this.id = id;
        this.passengerID = passengerID;
        this.driverID = driverID;
        this.from_destination = from_destination;
        this.to_destination = to_destination;
        this.from_lat = from_lat;
        this.from_lng = from_lng;
        this.to_lat = to_lat;
        this.to_lng = to_lng;
        this.status = status;
        this.amount = amount;
        this.review = review;
        this.rating = rating;
    }


    @Override
    public String toString() {
        return  "From:"+ this.from_destination + ", To: " + this.to_destination + ", Driver:"+this.driverID +", Passenger:"+this.passengerID;
    }

}
