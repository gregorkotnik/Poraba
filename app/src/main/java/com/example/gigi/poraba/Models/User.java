package com.example.gigi.poraba.Models;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by GIGI on 27/02/2018.
 */

public class User
{
   public String name,brandOfCar,typeOfFuel,fuelUnit,consumptionUnit,distanceUnit;
   public Double odometer,fuelCapacity;

    public  User(){}

    public User(String name, Double odometer) {
        this.name = name;
        this.odometer = odometer;
    }

    public User(String name, Double odometer, /*String brandOfCar*/ String typeOfFuel, String fuelUnit, String consumptionUnit, String distanceUnit, Double fuelCapacity) {
        this.name=name;
        this.odometer=odometer;

        //this.brandOfCar=brandOfCar;
        this.typeOfFuel=typeOfFuel;
        this.fuelUnit=fuelUnit;
        this.consumptionUnit=consumptionUnit;
        this.distanceUnit=distanceUnit;
        this.fuelCapacity=fuelCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public String getBrandOfCar() {
        return brandOfCar;
    }

    public void setBrandOfCar(String brandOfCar) {
        this.brandOfCar = brandOfCar;
    }

    public String getTypeOfFuel() {
        return typeOfFuel;
    }

    public void setTypeOfFuel(String typeOfFuel) {
        this.typeOfFuel = typeOfFuel;
    }

    public String getFuelUnit() {
        return fuelUnit;
    }

    public void setFuelUnit(String fuelUnit) {
        this.fuelUnit = fuelUnit;
    }

    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void printUser(User user, Context ctg)
    {
        String test=user.getName()+" "+user.getConsumptionUnit()+" "+String.valueOf(user.getFuelCapacity());
        Toast.makeText(ctg,test,Toast.LENGTH_LONG).show();
    }

}
