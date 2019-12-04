package com.example.gigi.poraba.Models;

import java.text.DecimalFormat;
import java.util.List;

import com.example.gigi.poraba.Adapters.FuelConsumptionAdapterNew;
import com.example.gigi.poraba.DB.DatabaseHelper;

import android.content.SharedPreferences;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ConsumptionViewModel
{
	private DatabaseHelper consumptionDB;
	private List<FuelConsumption> fuelConsumptionList;
	private ListView listViewConsumptions;
	private FuelConsumptionAdapterNew fuelConsumptionAdapterNew;
	private SharedPreferences sharedPref;
	private String name = "";
	private ImageButton btnConsumptionAdd;
	private TextView tvAverageConsumption;
	private double odometer = 0.0;
	private double editDistance = 0.0;
	private DecimalFormat df;
	private boolean checkIfInserted = false;

	public DatabaseHelper getConsumptionDB()
	{
		return consumptionDB;
	}

	public void setConsumptionDB(DatabaseHelper consumptionDB)
	{
		this.consumptionDB = consumptionDB;
	}

	public List<FuelConsumption> getFuelConsumptionList()
	{
		return fuelConsumptionList;
	}

	public void setFuelConsumptionList(List<FuelConsumption> fuelConsumptionList)
	{
		this.fuelConsumptionList = fuelConsumptionList;
	}

	public ListView getListViewConsumptions()
	{
		return listViewConsumptions;
	}

	public void setListViewConsumptions(ListView listViewConsumptions)
	{
		this.listViewConsumptions = listViewConsumptions;
	}

	public FuelConsumptionAdapterNew getFuelConsumptionAdapterNew()
	{
		return fuelConsumptionAdapterNew;
	}

	public void setFuelConsumptionAdapterNew(FuelConsumptionAdapterNew fuelConsumptionAdapterNew)
	{
		this.fuelConsumptionAdapterNew = fuelConsumptionAdapterNew;
	}

	public SharedPreferences getSharedPref()
	{
		return sharedPref;
	}

	public void setSharedPref(SharedPreferences sharedPref)
	{
		this.sharedPref = sharedPref;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ImageButton getBtnConsumptionAdd()
	{
		return btnConsumptionAdd;
	}

	public void setBtnConsumptionAdd(ImageButton btnConsumptionAdd)
	{
		this.btnConsumptionAdd = btnConsumptionAdd;
	}

	public TextView getTvAverageConsumption()
	{
		return tvAverageConsumption;
	}

	public void setTvAverageConsumption(TextView tvAverageConsumption)
	{
		this.tvAverageConsumption = tvAverageConsumption;
	}

	public double getOdometer()
	{
		return odometer;
	}

	public void setOdometer(double odometer)
	{
		this.odometer = odometer;
	}

	public double getEditDistance()
	{
		return editDistance;
	}

	public void setEditDistance(double editDistance)
	{
		this.editDistance = editDistance;
	}

	public DecimalFormat getDf()
	{
		return df;
	}

	public void setDf(DecimalFormat df)
	{
		this.df = df;
	}

	public boolean isCheckIfInserted()
	{
		return checkIfInserted;
	}

	public void setCheckIfInserted(boolean checkIfInserted)
	{
		this.checkIfInserted = checkIfInserted;
	}
}
