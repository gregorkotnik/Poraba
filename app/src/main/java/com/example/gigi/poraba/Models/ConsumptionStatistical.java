package com.example.gigi.poraba.Models;

public class ConsumptionStatistical
{
	int month;
	int year;
	int day;
	double consumption;
	double petrol;
	double temporaryDistance;
	double distance;
	String date;

	public ConsumptionStatistical(int month, int year, int day, double consumption, double petrol, double temporaryDistance, double distance, String date)
	{
		this.month = month;
		this.year = year;
		this.day = day;
		this.consumption = consumption;
		this.petrol = petrol;
		this.temporaryDistance = temporaryDistance;
		this.distance = distance;
		this.date = date;

	}

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getDay()
	{
		return day;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public double getConsumption()
	{
		return consumption;
	}

	public void setConsumption(double consumption)
	{
		this.consumption = consumption;
	}

	public double getPetrol()
	{
		return petrol;
	}

	public void setPetrol(double petrol)
	{
		this.petrol = petrol;
	}

	public double getTemporaryDistance()
	{
		return temporaryDistance;
	}

	public void setTemporaryDistance(double temporaryConsumption)
	{
		this.temporaryDistance = temporaryConsumption;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}
}
