package com.example.shahzad.ridesafepk;

/**
 * Created by shahzad on 1/1/2016.
 */
public class DriverModel {

    Integer id;
    String name,  phone;
    Double distance;


    public DriverModel(Integer id, String name, String phone, Double distance)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.distance = distance;
    }

}
