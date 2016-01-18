package com.example.shahzad.ridesafepk;

/**
 * Created by shahzad on 1/1/2016.
 */
public class DriverModel {

    Integer id;
    String name,  phone;
    Double distance, rating;

    String image = "pic1";

    public DriverModel(){
        super();
    }

    public DriverModel(Integer id, String name, String phone, Double distance, Double rating, String image)
    {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.distance = distance;
        this.rating =rating;
        this.image = image;
    }



    //@Override
    //public String toString() {
      //  return  this.name + ", " + this.distance + "km away";
   // }

}
