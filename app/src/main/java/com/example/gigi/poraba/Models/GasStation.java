package com.example.gigi.poraba.Models;

public class GasStation
{
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private String adress;
	private double distance;

	public GasStation(String adress, double distance)
	{
		this.adress = adress;
		this.distance = distance;
	}


}
