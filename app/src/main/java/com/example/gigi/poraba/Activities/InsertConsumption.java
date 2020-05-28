package com.example.gigi.poraba.Activities;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Adapters.FuelConsumptionAdapterNew;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Models.GasStation;
import com.example.gigi.poraba.Models.InsertConsumptionModel;
import com.example.gigi.poraba.Models.User;
import com.example.gigi.poraba.Models.fuelConsumption;
import com.example.gigi.poraba.Utils.GsonParserUtils;
import com.example.gigi.poraba.Utils.InsertConsumptionUtils;
import com.google.gson.reflect.TypeToken;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class InsertConsumption extends AppCompatActivity
{
	private InsertConsumptionModel insertConsumptionModel;

	public InsertConsumption()
	{
		insertConsumptionModel = new InsertConsumptionModel();
	}

	// https://medium.com/@CodyEngel/4-ways-to-implement-onclicklistener-on-android-9b956cbd2928
	// http://blog.cubeactive.com/onclicklistener-android-tutorial/

	// TODO: 27.5 tvDate z view model
	private DatePickerDialog.OnDateSetListener DateSetListener;

	public static final String DEFAULT = "N/A";
	public static final Float DEFAULT_FLOAT = 0.0f;

	DecimalFormat df;

	ImageView imgBtnGas;

	private boolean CheckDate, CheckDistance = false, checkData = false;

	// --------------------------Custom ListViewAdapter-----------------------------

	List<fuelConsumption> fuelConsumptionList;
	ListView listViewConsumptions;
	FuelConsumptionAdapterNew fuelConsumptionAdapterNew;
	SharedPreferences sharedPref;
	String name;// ="Martina";//sharedPref.getString("Name",DEFAULT);
	// -----------------------------------------------------------------------------
	private fuelConsumption consumptionValue = null;

	private static final Pattern DOUBLE_PATTERN = Pattern
			.compile("[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" + "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|"
					+ "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" + "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

	String userName;

	@Override
	protected void onStart()
	{
		super.onStart();
		Intent i = getIntent();

		// TODO pošlji cel objekt(fuelConusmption) iz fragmenta COnsumptionFragment tukaj končal 5.2
		final String consumption = i.getExtras().getString("consumptionList");
		if (consumption != null)
		{
			setConsumptionData(consumption);
		}

		// Log.d("POSITION", String.valueOf(consumptionValue.getConsumption()));

		if (i.hasExtra("gasStationAdress") && i.hasExtra("gasStationDistance"))
		{
			String address = getIntent().getExtras().getString("gasStationAdress");
			int distance = (int) getIntent().getExtras().getDouble("gasStationDistance");
			GasStation gasStation = new GasStation(address, distance);
			// tvLocation.setText(address + " " + distance + " m");
			insertConsumptionModel.getTvLocation().setText(address + " " + distance + " m");
			imgBtnGas.setVisibility(View.VISIBLE);
			insertConsumptionModel.getCbCheckLocation().setChecked(true);

		}

	}

	private void setConsumptionData(String consumption)
	{
		if (consumption != null)
		{
			Type listfuelConsumptionObject = new TypeToken<ArrayList<fuelConsumption>>()
			{}.getType();
			List<fuelConsumption> fuelConsumptionUpdateList = GsonParserUtils.getGsonParser().fromJson(consumption, listfuelConsumptionObject);
			for (fuelConsumption fuelConsumption : fuelConsumptionUpdateList)
			{
				// TODO 11.2 koncal tukaj - ko uspeš prenesti seznam najprej naredi refactoring tega classa
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_insert_consumption);

		insertConsumptionModel.setConsumptionDataBaseHelper(new DatabaseHelper(this));
		fuelConsumptionList = new ArrayList<>();

		sharedPref = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
		name = sharedPref.getString("Name", DEFAULT);

		AddConsumationToList addConsumationToList = new AddConsumationToList();

		// klic metode showConsumation from DB
		if (!name.isEmpty())
		{
			addConsumationToList.execute(name);
		}
		// -----------------------------------------------------------------------------

		// 26.5.
		setComponentsInModel();

		df = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);

		// AddData();
		insertConsumptionModel.setSumOfMilage(LastOdometer(checkLoginData().name));

		insertConsumptionModel.getBtnBack().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(InsertConsumption.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// check box za lokacijo bencinske
		insertConsumptionModel.getCbCheckLocation().setOnCheckedChangeListener((compoundButton, isChecked) -> {
			if (isChecked)
			{
				imgBtnGas.setVisibility(View.VISIBLE);
			}
			else
			{
				imgBtnGas.setVisibility(View.GONE);
			}
		});
		// gumb za dodajanje bencinske
		imgBtnGas.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent i = new Intent(InsertConsumption.this, StationsLocation.class);
				startActivity(i);

			}
		});
		// gumbi za prištevanje prevoženih kilometrov
		insertConsumptionModel.getBtnAddOneKilometer().setOnClickListener(view -> {
			insertConsumptionModel.setSumOfMilage(insertConsumptionModel.getSumOfMilage() + 1);
			insertConsumptionModel.getEtVehicleMileage().setText(Double.toString(insertConsumptionModel.getSumOfMilage()));
		});

		insertConsumptionModel.getBtnAddTenKilometers().setOnClickListener(view -> {
			insertConsumptionModel.setSumOfMilage(insertConsumptionModel.getSumOfMilage() + 10);
			insertConsumptionModel.getEtVehicleMileage().setText(Double.toString(insertConsumptionModel.getSumOfMilage()));

		});
		// TODO: rename getBtnAddOneHundredKilimeters atribute
		insertConsumptionModel.getBtnAddOneHundredKilimeters().setOnClickListener(view -> {
			insertConsumptionModel.setSumOfMilage(insertConsumptionModel.getSumOfMilage() + 100);
			insertConsumptionModel.getEtVehicleMileage().setText(Double.toString(insertConsumptionModel.getSumOfMilage()));
		});

		// tvDate = (TextView) findViewById(R.id.tvDate);
		// tvLocation = (TextView) findViewById(R.id.tvLocation);
		setDate();
		insertConsumptionModel.getBtnDateOfRefueling().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// trenurni datum
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(InsertConsumption.this, android.R.style.Theme_Holo_Dialog_MinWidth, insertConsumptionModel.getDateSetListener(), year, month, day);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.show();

			}
		});

		// izberemo datum, ki se potem prikazemo v textView

		DateSetListener = new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, dayOfMonth);

				month = month + 1;

				SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

				String formatted = myFormat.format(calendar.getTime());
				insertConsumptionModel.getTvDate().setText(formatted);
				// String Date=dayOfMonth+"/"+month+"/"+year;
				// String Date=year+"-"+month+"-"+dayOfMonth;
				// tvDate.setText(Date);
			}

		};
		insertConsumptionModel.getTvOdometer().setText("Trenutno stanje števca: " + LastOdometer(checkLoginData().name) + " km");
		// tvOdometer.setText("Trenutno stanje števca: " + LastOdometer(checkLoginData().name) + " km");

		insertConsumptionModel.getBtnInsertConsumption().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (TextUtils.isEmpty(insertConsumptionModel.getEtVehicleMileage().getText()))
				{
					insertConsumptionModel.getEtVehicleMileage().setError("Polje ne sme biti prazno");
					return;
				}
				else if (CheckDouble(insertConsumptionModel.getEtVehicleMileage().getText().toString()) == false)
				{
					insertConsumptionModel.getEtVehicleMileage().setError("Vnesite število! Primer: 10.1");
					return;
				}
				if (TextUtils.isEmpty(insertConsumptionModel.getEtFuelInput().getText()))
				{
					insertConsumptionModel.getEtFuelInput().setError("Polje ne sme biti prazno");

					return;
				}
				else if (CheckDouble(insertConsumptionModel.getEtFuelInput().getText().toString()) == false)
				{
					insertConsumptionModel.getEtFuelInput().setError("Vnesite število primer:50.3");
					return;
				}
				if (TextUtils.isEmpty(insertConsumptionModel.getEtFuelPrice().getText()))
				{
					insertConsumptionModel.getEtFuelPrice().setError("Polje ne sme biti prazno");
					return;
				}
				else if (CheckDouble(insertConsumptionModel.getEtFuelPrice().getText().toString()) == false)
				{
					insertConsumptionModel.getEtFuelPrice().setError("Vnesite število primer 1.25");
					return;
				}

				String date = insertConsumptionModel.getTvDate().getText().toString();// String.valueOf(tvDate.getText().toString());
				DateFormat formatter;
				Date datum;
				// formatter=new SimpleDateFormat("dd/MM/yyyy");
				formatter = new SimpleDateFormat("yyyy-MM-dd");

				try
				{
					datum = formatter.parse(date);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(datum);
					Log.d("DatumTest", calendar.toString());

					Double insertedKM = Double.valueOf(insertConsumptionModel.getEtVehicleMileage().getText().toString());
					// Tukaj preveri datum
					CheckDate = CheckDateInConsumption(calendar, insertedKM);
					// InsertAdditionalRow()(Distance, Fuel, Price, Date, odometerTMP, userName, insertedKM);
					Log.d("CHECKDATE", String.valueOf(CheckDate));
					// etCarDistance.setError("Upostevajte zaporedje km");
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}

				insertConsumptionModel.setFuel(Double.valueOf(insertConsumptionModel.getEtFuelInput().getText().toString()));
				userName = checkLoginData().getName();

				Cursor result = insertConsumptionModel.getConsumptionDataBaseHelper().getUserId(userName);
				// Cursor result = consimprionDb.getUserId(userName);
				if (result.getCount() == 0)
				{

					// String a = result.getString(result.getColumnIndex("USER_ID")); //tukaj je težava 13.11. 2018
					Toast.makeText(InsertConsumption.this, "Error no record", Toast.LENGTH_LONG).show();
				}
				else
				{
					StringBuffer buffer = new StringBuffer();

					while (result.moveToNext())
					{
						buffer.append(result.getString(0));
					}
					String a = buffer.toString();
					int UserId = Integer.parseInt(a);

					// INSERT BETWEEN
					if (CheckDate == true)
					{
						double Fuel = Double.valueOf(insertConsumptionModel.getEtFuelInput().getText().toString());
						double Distance = Double.parseDouble(insertConsumptionModel.getEtVehicleMileage().getText().toString());
						double Price = Double.parseDouble(insertConsumptionModel.getEtFuelPrice().getText().toString());
						String Date = insertConsumptionModel.getTvDate().getText().toString();// tvDate.getText().toString();
						double odometerTMP = Double.valueOf(insertConsumptionModel.getEtVehicleMileage().getText().toString());

						boolean isInserted = InsertAdditionalRow(Distance, Fuel, Price, Date, odometerTMP, userName, UserId);
						if (isInserted == true)
						{
							Toast.makeText(InsertConsumption.this, "Podatki vstavljeni", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(InsertConsumption.this, MainActivity.class);
							intent.putExtra("FlagInsertBetween", isInserted);
							startActivity(intent);
							finish();
						}
						else
						{
							Toast.makeText(InsertConsumption.this, "Podatki niso vstavljeni", Toast.LENGTH_LONG).show();
						}
					}

					else if (CheckDate == false)
					{
						insertConsumptionModel.getEtVehicleMileage().setError("Vnesite pravilne kilometre!");
						return;
					}
				}
			}
		});
	}

	private void setComponentsInModel()
	{
		InsertConsumptionUtils insertConsumptionUtils = new InsertConsumptionUtils();
		insertConsumptionUtils.setConsumptionModel(insertConsumptionModel);
	}

	private void setDate()
	{

		// Nastavi pravilen datum za sqlite in potem preveri metodo check
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		month = month + 1;
		// tvDate.setText(day+"/"+month+"/"+year);
		// String date=year+"-"+month+"-"+day;

		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		String formatted = myFormat.format(cal.getTime());
		insertConsumptionModel.getTvDate().setText(formatted);
		// tvDate.setText(formatted);

		// tvDate.setText(year+"-"+month+"-"+day);
	}

	public static boolean CheckDouble(String number)
	{
		return DOUBLE_PATTERN.matcher(number).matches();
	}

	// Check odometer
	public boolean CheckOdometer(double odometer)
	{

		// IZ BAZE
		final Cursor cursor = insertConsumptionModel.getConsumptionDataBaseHelper().getOdometer(checkLoginData().name);
		// Cursor cursor = consimprionDb.getOdometer(checkLoginData().name);
		double odometer1 = 0.0;
		while (cursor.moveToNext())
		{
			odometer1 = cursor.getDouble(2);
		}

		if (odometer < odometer1)
			return false;
		else
			return true;

	}

	public double LastOdometer(String name)
	{
		Cursor cursor = insertConsumptionModel.getConsumptionDataBaseHelper().getOdometer(name);
		// Cursor cursor = consimprionDb.getOdometer(name);
		double odometer = 0.0;
		while (cursor.moveToNext())
		{
			odometer = cursor.getDouble(2);
		}
		return odometer;
	}

	public User checkLoginData()
	{

		// Tukaj preverimo login podatke stanje števca in ime profila
		SharedPreferences sharedPref = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
		String name = sharedPref.getString("Name", DEFAULT);
		Float odometer = sharedPref.getFloat("Odometer", DEFAULT_FLOAT);
		Double ODOMETER = Double.valueOf(odometer.toString());
		User LoginUser = new User(name, ODOMETER);
		return LoginUser;

	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean CheckDateInConsumption(Calendar date, double insertedDistance)
	{
		Date date1, date1_2, date3, date4;
		Date date2 = date.getTime();
		String dateInList = "", dateInList2 = "";
		// SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = fuelConsumptionList.size() - 1; i > 0; i--)
		{

			dateInList = fuelConsumptionList.get(i).getDate();
			dateInList2 = fuelConsumptionList.get(i - 1).getDate();

			try
			{
				date1 = format.parse(dateInList);
				date1_2 = format.parse(dateInList2);

				if (date2.compareTo(date1) < 0 && insertedDistance > fuelConsumptionList.get(i).getDistance()
						|| date2.compareTo(date1_2) > 0 && insertedDistance < fuelConsumptionList.get(i - 1).getDistance())
				{
					return false;
				}

			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	private boolean InsertAdditionalRow(double distance, double fuel, double price, String date, double odometerTMP, String userName, int UserId)
	{
		double totalPrice = fuel * price;
		insertConsumptionModel.setCurrentDistance(0.0);
		boolean isInserted = insertConsumptionModel.getConsumptionDataBaseHelper().insertData(fuel, distance, price, date, insertConsumptionModel.getCurrentDistance(), 0.0, totalPrice, userName,
				UserId);
		// boolean isInserted = consimprionDb.insertData(fuel, distance, price, date, distance_tmp, 0.0, totalPrice, userName, UserId);

		if (isInserted == true)
		{
			return true;
		}
		else
			return false;
	}

	private class AddConsumationToList extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... name)
		{
			Cursor result = insertConsumptionModel.getConsumptionDataBaseHelper().getFuelConsumptionData(name[0]);
			// Cursor result = consimprionDb.getFuelConsumptionData(name[0]);
			fuelConsumptionList.clear();
			while (result.moveToNext())
			{
				fuelConsumptionList.add(new fuelConsumption(result.getInt(0), result.getDouble(1), result.getDouble(2), result.getDouble(3), result.getString(4), result.getDouble(5),
						result.getDouble(6), result.getDouble(7), result.getInt(8), result.getString(9)));
			}

			result.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			super.onPostExecute(aVoid);
			checkData = true;
			// showConsumationFromDB();
		}
	}

}

// Napolni bazo tako ko dobiš + z asynctask
