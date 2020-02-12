package com.example.gigi.poraba.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gigi.poraba.Adapters.FuelConsumptionAdapter;
import com.example.gigi.poraba.Models.fuelConsumption;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.R;

import static com.example.gigi.poraba.Activities.InsertConsumption.DEFAULT;

public class ShowConsumation extends AppCompatActivity
{

	DatabaseHelper consimptionDb;
	Button btnShow;
	FuelConsumptionAdapter fuelConsumptionAdapter;
	ListView listView;
	TextView tvPrice, tvConsumption, tvDate;

	SharedPreferences sharedPref;
	String name;// ="Martina";//sharedPref.getString("Name",DEFAULT);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_show_consumation);
		consimptionDb = new DatabaseHelper(this);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnShow.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showConsumption();
			}
		});
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvConsumption = (TextView) findViewById(R.id.tvConsumptionLiters);
		tvDate = (TextView) findViewById(R.id.tvDate);
		sharedPref = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
		name = sharedPref.getString("Name", DEFAULT);
		// System.out.print(name);

	}

	public void showConsumption()
	{

		double fuel, price, consumption, totalPrice;
		String date, userName;
		int id, FK_id;

		Cursor result = consimptionDb.getFuelConsumptionData(name);

		fuelConsumptionAdapter = new FuelConsumptionAdapter(this, R.layout.display_fuel_row);
		listView = (ListView) this.findViewById(R.id.lvShowResult);
		if (result.getCount() == 0)
		{
			tvPrice.setText("Ni podatkov!");
			tvPrice.setTextSize(30);
			tvConsumption.setText("");
			tvDate.setText("");
			// error
			// showMessage("error","ni najdenih podatkov");
		}
		else
		{
			StringBuffer buffer = new StringBuffer();
			while (result.moveToNext())
			{
				id = result.getInt(result.getColumnIndex("ID"));
				fuel = result.getDouble(result.getColumnIndex("PETROL"));
				price = result.getDouble(result.getColumnIndex("PRICE"));
				date = result.getString(result.getColumnIndex("DATE"));
				consumption = result.getDouble(result.getColumnIndex("CONSUMPTION"));
				FK_id = result.getInt(result.getColumnIndex("USER_ID_FK"));
				userName = result.getString(result.getColumnIndex("USER_NAME_FK"));
				totalPrice = result.getDouble(result.getColumnIndex("TOTAL_PRICE"));

				fuelConsumption fuel_consumption = new fuelConsumption(id, 0.0, fuel, price, date, 0.0, totalPrice, consumption, FK_id, userName);
				fuelConsumptionAdapter.add(fuel_consumption);

			}

			listView.setAdapter(fuelConsumptionAdapter);
		}

	}

	public void showMessage(String title, String Message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(Message);
		builder.show();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
