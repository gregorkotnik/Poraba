package com.example.gigi.poraba.Models;

import android.widget.Button;
import android.widget.EditText;

import com.example.gigi.poraba.BuildConfig;
import com.example.gigi.poraba.DB.DatabaseHelper;

public class InsertConsumptionModel
{

    private DatabaseHelper consumptionDB;
    private EditText etVehicleMileage;
    private EditText etFuelInput;
    private EditText etFuelPrice;
    private Button btnDateOfRefueling;
    private Button btnAddOne;
    private Button btnAddTen;
    private BuildConfig btnAddOneHundred;

	public DatabaseHelper getConsumptionDB()
	{
		return consumptionDB;
	}

	public void setConsumptionDB(DatabaseHelper consumptionDB)
	{
		this.consumptionDB = consumptionDB;
	}


}
