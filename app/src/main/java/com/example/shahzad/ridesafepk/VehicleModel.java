package com.example.shahzad.ridesafepk;

/**
 * Created by shahzad on 12/21/2015.
 */
public class VehicleModel {

    Integer id;
    String name, model_name, manufacturer_name;
    Integer ownerId;


    //set detail

    public VehicleModel(Integer id, String name, String model_name, String manufacturer_name, Integer ownerId)
    {
        this.id = id;
        this.name = name;
        this.model_name = model_name;
        this.manufacturer_name = manufacturer_name;
        this.ownerId = ownerId;
    }

    //save vehicel constuctor
    public  VehicleModel(String name, String model_name, String manufacturer_name, Integer ownerId)
    {
        this.name = name;
        this.model_name = model_name;
        this.manufacturer_name  = manufacturer_name;
        this.ownerId = ownerId;
    }

}
