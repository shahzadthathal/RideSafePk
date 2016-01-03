package com.example.shahzad.ridesafepk;

/**
 * Created by shahzad on 1/1/2016.
 */
public class DriverModel {

    Integer id;
    String name,  phone;
    Double distance;


    public DriverModel(){
        super();
    }

    public DriverModel(Integer id, String name, String phone, Double distance)
    {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return  this.name + ", " + this.distance + "km away";
    }

}
