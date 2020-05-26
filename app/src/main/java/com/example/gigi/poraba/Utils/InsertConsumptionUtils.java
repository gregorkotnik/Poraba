package com.example.gigi.poraba.Utils;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Models.InsertConsumptionModel;

import android.support.v7.app.AppCompatActivity;

public class InsertConsumptionUtils extends AppCompatActivity
{

	public void setConsumptionModel(final InsertConsumptionModel model)
	{
		model.setEtVehicleMileage(findViewById(R.id.etCarDistance));
		model.setEtFuelInput(findViewById(R.id.etInsertFuel));
		model.setEtFuelPrice(findViewById(R.id.etPrice));
		model.setTvOdometer(findViewById(R.id.tvOdometer));
		model.setBtnDateOfRefueling(findViewById(R.id.btnDate));
		model.setBtnBack(findViewById(R.id.btnBackImg));
		model.setBtnAddOneKilometer(findViewById(R.id.btnPlus1));
		model.setBtnAddTenKilometers(findViewById(R.id.btnPlus10));
		model.setBtnAddOneHundredKilimeters(findViewById(R.id.btnPlus100));
		model.setBtnInsertConsumption(findViewById(R.id.btnInsertPlus));
		model.setBtnSelectGasStation(findViewById(R.id.imgBtnGas));
		model.setCbCheckLocation(findViewById(R.id.cbLocation));
	}
}
