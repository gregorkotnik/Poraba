package com.example.gigi.poraba.Models;

import java.text.DecimalFormat;
import java.util.List;

import com.example.gigi.poraba.DB.DatabaseHelper;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InsertConsumptionModel
{
	private DatabaseHelper consumptionDataBaseHelper;
	private EditText etVehicleMileage;
	private EditText etFuelInput;
	private EditText etFuelPrice;
	private Button btnDateOfRefueling;
	private Button btnAddOneKilometer;
	private Button btnAddTenKilometers;
	private Button btnAddOneHundredKilimeters;
	private ImageButton btnInsertConsumption;
	private CheckBox cbCheckLocation;
	private TextView tvDate;
	private TextView tvOdometer;
	private TextView tvLocation;
	private DatePickerDialog.OnDateSetListener DateSetListener;
	private double currentDistance;
	private double sumOfMilage;
	private double fuel;
	private DecimalFormat decimalFormat;
	private ImageView btnSelectGasStation;
	private boolean dateCheck;
	private boolean distanceCheck;
	private boolean dataCheck;
	private List<fuelConsumption> fuelConsumptionList;
	private SharedPreferences sharedPref;
	private fuelConsumption consumptionValue;
	private String userName;
	private Cursor cursor;

	public DatabaseHelper getConsumptionDataBaseHelper()
	{
		return consumptionDataBaseHelper;
	}

	public void setConsumptionDataBaseHelper(DatabaseHelper consumptionDataBaseHelper)
	{
		this.consumptionDataBaseHelper = consumptionDataBaseHelper;
	}

	public EditText getEtVehicleMileage()
	{
		return etVehicleMileage;
	}

	public void setEtVehicleMileage(EditText etVehicleMileage)
	{
		this.etVehicleMileage = etVehicleMileage;
	}

	public EditText getEtFuelInput()
	{
		return etFuelInput;
	}

	public void setEtFuelInput(EditText etFuelInput)
	{
		this.etFuelInput = etFuelInput;
	}

	public EditText getEtFuelPrice()
	{
		return etFuelPrice;
	}

	public void setEtFuelPrice(EditText etFuelPrice)
	{
		this.etFuelPrice = etFuelPrice;
	}

	public Button getBtnDateOfRefueling()
	{
		return btnDateOfRefueling;
	}

	public void setBtnDateOfRefueling(Button btnDateOfRefueling)
	{
		this.btnDateOfRefueling = btnDateOfRefueling;
	}

	public Button getBtnAddOneKilometer()
	{
		return btnAddOneKilometer;
	}

	public void setBtnAddOneKilometer(Button btnAddOneKilometer)
	{
		this.btnAddOneKilometer = btnAddOneKilometer;
	}

	public Button getBtnAddTenKilometers()
	{
		return btnAddTenKilometers;
	}

	public void setBtnAddTenKilometers(Button btnAddTenKilometers)
	{
		this.btnAddTenKilometers = btnAddTenKilometers;
	}

	public Button getBtnAddOneHundredKilimeters()
	{
		return btnAddOneHundredKilimeters;
	}

	public void setBtnAddOneHundredKilimeters(Button btnAddOneHundredKilimeters)
	{
		this.btnAddOneHundredKilimeters = btnAddOneHundredKilimeters;
	}

	public ImageButton getBtnInsertConsumption()
	{
		return btnInsertConsumption;
	}

	public void setBtnInsertConsumption(ImageButton btnInsertConsumption)
	{
		this.btnInsertConsumption = btnInsertConsumption;
	}

	public CheckBox getCbCheckLocation()
	{
		return cbCheckLocation;
	}

	public void setCbCheckLocation(CheckBox cbCheckLocation)
	{
		this.cbCheckLocation = cbCheckLocation;
	}

	public TextView getTvDate()
	{
		return tvDate;
	}

	public void setTvDate(TextView tvDate)
	{
		this.tvDate = tvDate;
	}

	public TextView getTvOdometer()
	{
		return tvOdometer;
	}

	public void setTvOdometer(TextView tvOdometer)
	{
		this.tvOdometer = tvOdometer;
	}

	public TextView getTvLocation()
	{
		return tvLocation;
	}

	public void setTvLocation(TextView tvLocation)
	{
		this.tvLocation = tvLocation;
	}

	public DatePickerDialog.OnDateSetListener getDateSetListener()
	{
		return DateSetListener;
	}

	public void setDateSetListener(DatePickerDialog.OnDateSetListener dateSetListener)
	{
		DateSetListener = dateSetListener;
	}

	public double getCurrentDistance()
	{
		return currentDistance;
	}

	public void setCurrentDistance(double currentDistance)
	{
		this.currentDistance = currentDistance;
	}

	public double getSumOfMilage()
	{
		return sumOfMilage;
	}

	public void setSumOfMilage(double sumOfMilage)
	{
		this.sumOfMilage = sumOfMilage;
	}

	public double getFuel()
	{
		return fuel;
	}

	public void setFuel(double fuel)
	{
		this.fuel = fuel;
	}

	public DecimalFormat getDecimalFormat()
	{
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat)
	{
		this.decimalFormat = decimalFormat;
	}

	public ImageView getBtnSelectGasStation()
	{
		return btnSelectGasStation;
	}

	public void setBtnSelectGasStation(ImageView btnSelectGasStation)
	{
		this.btnSelectGasStation = btnSelectGasStation;
	}

	public boolean isDateCheck()
	{
		return dateCheck;
	}

	public void setDateCheck(boolean dateCheck)
	{
		this.dateCheck = dateCheck;
	}

	public boolean isDistanceCheck()
	{
		return distanceCheck;
	}

	public void setDistanceCheck(boolean distanceCheck)
	{
		this.distanceCheck = distanceCheck;
	}

	public boolean isDataCheck()
	{
		return dataCheck;
	}

	public void setDataCheck(boolean dataCheck)
	{
		this.dataCheck = dataCheck;
	}

	public List<fuelConsumption> getFuelConsumptionList()
	{
		return fuelConsumptionList;
	}

	public void setFuelConsumptionList(List<fuelConsumption> fuelConsumptionList)
	{
		this.fuelConsumptionList = fuelConsumptionList;
	}

	public SharedPreferences getSharedPref()
	{
		return sharedPref;
	}

	public void setSharedPref(SharedPreferences sharedPref)
	{
		this.sharedPref = sharedPref;
	}

	public fuelConsumption getConsumptionValue()
	{
		return consumptionValue;
	}

	public void setConsumptionValue(fuelConsumption consumptionValue)
	{
		this.consumptionValue = consumptionValue;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Cursor getCursor()
	{
		return cursor;
	}

	public void setCursor(Cursor cursor)
	{
		this.cursor = cursor;
	}

}
