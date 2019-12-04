package com.example.gigi.poraba.Models;

public class ConsumptionValue
{

	private String date;
	private double consumption;
	private String year;
	private int day;
	private int month;

	public ConsumptionValue(String InputDate, double InputConsumption, String Year, int day)
	{
		this.date = InputDate;
		this.consumption = InputConsumption;
		this.year = Year;
		this.day = day;
	}

	public ConsumptionValue(String InputDate, double InputConsumption, String Year, int day, int month)
	{
		this.date = InputDate;
		this.consumption = InputConsumption;
		this.year = Year;
		this.day = day;
		this.month = month;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public double getConsumption()
	{
		return consumption;
	}

	public void setConsumption(double consumption)
	{
		this.consumption = consumption;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
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

	public int getMonth()
	{
		return month;
	}

	public void setMonth(int month)
	{
		this.month = month;
	}

}
