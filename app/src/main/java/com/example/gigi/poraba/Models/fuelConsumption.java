package com.example.gigi.poraba.Models;

/**
 * Created by GIGI on 22/02/2018.
 */

public class fuelConsumption
{

	private int Id, fkId;
	double petrol, Distance, price, DistanceTmp, Consumption, TotalPrice;
	String date, userName;

	public fuelConsumption()
	{};

	public fuelConsumption(int id, double Petrol, double Distance, double price, String date, double DistanceTmp, double Consumption, double totalPrice, int fk_id, String userName)
	{

		this.Id = id;
		this.petrol = Petrol;
		this.Distance = Distance;
		this.price = price;
		this.date = date;
		this.DistanceTmp = DistanceTmp;
		this.Consumption = Consumption;
		this.TotalPrice = totalPrice;

		this.fkId = fk_id;
		this.userName = userName;

	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public int getId()
	{
		return Id;
	}

	public void setId(int id)
	{
		Id = id;
	}

	public int getFkId()
	{
		return fkId;
	}

	public void setFkId(int fkId)
	{
		this.fkId = fkId;
	}

	public double getPetrol()
	{
		return petrol;
	}

	public void setPetrol(double petrol)
	{
		this.petrol = petrol;
	}

	public double getDistance()
	{
		return Distance;
	}

	public void setDistance(double distance)
	{
		Distance = distance;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getDistanceTmp()
	{
		return DistanceTmp;
	}

	public void setDistanceTmp(double distanceTmp)
	{
		DistanceTmp = distanceTmp;
	}

	public double getConsumption()
	{
		return Consumption;
	}

	public void setConsumption(double consumption)
	{
		Consumption = consumption;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public double getTotalPrice()
	{
		return TotalPrice;
	}

	public void setTotalPrice(double totalPrice)
	{
		TotalPrice = totalPrice;
	}

}
