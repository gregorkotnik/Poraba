package com.example.gigi.poraba.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Models.User;

import java.util.ArrayList;

public class NewUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

	TextView tvFuelType;
	EditText etAddNewUser, etTankCapacity;
	Spinner fuelType, spinnerDistanceUnit, spinnerConsumptionUnit, spinnerUnit;
	ImageButton btnAddNewUser;
	ArrayList<String> listOfUsers;
	DatabaseHelper databaseHelper;

	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);

		listOfUsers = getIntent().getStringArrayListExtra("listOfUsers");
		databaseHelper = new DatabaseHelper(this);

		user = new User();
		btnAddNewUser = findViewById(R.id.btnAddNewUser);
		etAddNewUser = findViewById(R.id.etAddNewUser);
		etTankCapacity = findViewById(R.id.etTankCapacity);
		fuelType = findViewById(R.id.spinner);
		spinnerDistanceUnit = findViewById(R.id.spinnerDistanceUnit);
		spinnerUnit = findViewById(R.id.spinnerUnit);
		spinnerConsumptionUnit = findViewById(R.id.spinnerConsumptionUnit);

		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.fuelType, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.distanceUnit, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.unit, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.consumptionUnit, android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		fuelType.setAdapter(adapter1);
		fuelType.setOnItemSelectedListener(this);

		spinnerDistanceUnit.setAdapter(adapter2);
		spinnerDistanceUnit.setOnItemSelectedListener(this);

		spinnerUnit.setAdapter(adapter3);
		spinnerUnit.setOnItemSelectedListener(this);

		spinnerConsumptionUnit.setAdapter(adapter4);
		spinnerConsumptionUnit.setOnItemSelectedListener(this);

		btnAddNewUser.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				String newUser = etAddNewUser.getText().toString().trim();
				String tankCapacity = etTankCapacity.getText().toString();// Double.parseDouble(etTankCapacity.getText().toString()); //15.3 - preveri

				if (newUser.isEmpty())
				{
					etAddNewUser.setError("Polje ne sme biti prazno");
					etAddNewUser.requestFocus();
					return;
				}

				else if (tankCapacity.isEmpty())
				{
					etTankCapacity.setError("Polje ne sme biti prazno");
					etTankCapacity.requestFocus();
					return;
				}

				else if (listOfUsers.contains(newUser.toString()))
				{
					Toast.makeText(getApplicationContext(), "To oporabniško ime ime že obstaja!", Toast.LENGTH_SHORT).show();
					return;
				}

				user.setName(etAddNewUser.getText().toString().trim());
				user.setFuelCapacity(Double.valueOf(tankCapacity));

				String test = "TEST: " + user.getName().toString() + " " + user.getConsumptionUnit().toString() + " " + user.getDistanceUnit().toString() + " " + user.getTypeOfFuel().toString() + " "
						+ user.getFuelUnit().toString();

				// Toast.makeText(NewUser.this,test,Toast.LENGTH_SHORT).show();
				user.printUser(user, getBaseContext());

				// insert user

				boolean isInserted = databaseHelper.insertUserDataNew(user);
				if (isInserted == true)
				{
					Toast.makeText(getBaseContext(), "Uporabnik uspešno vnesen v bazo!", Toast.LENGTH_LONG).show();
					finish();
				}

			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{

		if (adapterView.getId() == R.id.spinner)
		{
			String fuelType = adapterView.getItemAtPosition(i).toString();
			user.setTypeOfFuel(fuelType.toString().trim());
			// Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
		}
		else if (adapterView.getId() == R.id.spinnerDistanceUnit)
		{
			String distanceUnit = adapterView.getItemAtPosition(i).toString();
			user.setDistanceUnit(distanceUnit.toString().trim());

		}
		else if (adapterView.getId() == R.id.spinnerUnit)
		{
			String text = adapterView.getItemAtPosition(i).toString();
			user.setFuelUnit(text.toString().trim());
		}
		else if (adapterView.getId() == R.id.spinnerConsumptionUnit)
		{
			String text = adapterView.getItemAtPosition(i).toString();
			user.setConsumptionUnit(text.toString().trim());
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{}

}
