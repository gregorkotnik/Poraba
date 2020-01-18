package com.example.gigi.poraba.Fragments;
// Tukaj naredi še asynctaske za shranjevanje pridobivanje podatkov iz baze, pridobivanje lokacije, prvo porabo popravi tako da lahko spreminjaš samo podatke glede količinein datum

import static com.example.gigi.poraba.Activities.insertConsumption.DEFAULT;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Activities.insertConsumption;
import com.example.gigi.poraba.Adapters.FuelConsumptionAdapterNew;
import com.example.gigi.poraba.Constants.Constants;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Interfaces.CustomListenerConsumptionItem;
import com.example.gigi.poraba.Models.ConsumptionViewModel;
import com.example.gigi.poraba.Models.FuelConsumption;
import com.example.gigi.poraba.Utils.SavePreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumptionFragment extends Fragment implements CustomListenerConsumptionItem
{
	// TODO 27.11. Iz ConumptionViewModela dokončas fuelCOnsumptionList
	private ConsumptionViewModel consumptionViewModel;

	public ConsumptionFragment()
	{
		consumptionViewModel = new ConsumptionViewModel();
	}

	//String name = "";// "martina33";//sharedPref.getString("Name",DEFAULT);

	ImageButton btnConsumptionAdd;
	TextView tvAverageConsumption;

	double odometer = 0.0;
	double editDistance = 0.0;

	//DecimalFormat df;
	boolean checkIfInserted = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		consumptionViewModel.setConsumptionDB(new DatabaseHelper(getActivity().getApplicationContext()));
		consumptionViewModel.setFuelConsumptionList(new ArrayList<>());
		consumptionViewModel.setSharedPref(getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE));

		//name = consumptionViewModel.getSharedPref().getString("Name", DEFAULT);
		consumptionViewModel.setName(consumptionViewModel.getSharedPref().getString("Name", DEFAULT));

		// Decimal format
		//df = new DecimalFormat("#.##");
		consumptionViewModel.setDecimalFormat(new DecimalFormat("#.##"));
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');

		// df.setDecimalFormatSymbols(dfs);
		consumptionViewModel.getDecimalFormat().setDecimalFormatSymbols(dfs);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_consumption, container, false);
		consumptionViewModel.setListViewConsumptions((ListView) view.findViewById(R.id.lvConsumptionFragment));
		btnConsumptionAdd = view.findViewById(R.id.btnConsumptionAdd);
		tvAverageConsumption = (TextView) view.findViewById(R.id.tvAverageConsumption2);

		showConsumationFromDB_Async(consumptionViewModel.getName());

		btnConsumptionAdd.setOnClickListener(view1 -> {
			Intent i = new Intent(getActivity(), insertConsumption.class);
			startActivity(i);
			getActivity().finish();
		});

		return view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		checkIfInserted = true;
		showConsumationFromDB_Async(consumptionViewModel.getName());

	}

	@Override
	public void onStop()
	{
		// TODO why onStop()
		// fuelConsumptionAdapterNew.setCustomListenerEdit(null);
		// fuelConsumptionAdapterNew.setCustomListenerDelete(null);
		checkIfInserted = false;
		super.onStop();
	}

	@Override
	public void onButtonClickListenerEdit(int position, FuelConsumption value)
	{
		updateConsumption(value, position);
	}

	@Override
	public void onButtonClickListenerDelete(int position, FuelConsumption value)
	{
		Toast.makeText(getActivity(), "Click: " + String.valueOf(position), Toast.LENGTH_SHORT).show();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Ste prepričani,da želite izbrisati zapis");
		builder.setPositiveButton("Da", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				consumptionViewModel.getConsumptionDB().DeleteConsumption(value.getId());
				UpdateConsumptionsAfterDelete(consumptionViewModel.getFuelConsumptionList(), /* fuelConsumptionList */ position);
			}
		});
		builder.setNegativeButton("Preklici", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{

			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onItemClickListener(int position, FuelConsumption value)
	{
		Toast.makeText(getContext(), "Hello from: " + position, Toast.LENGTH_LONG).show();
	}

	private class AddConsumationToList extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... name)
		{
			Cursor result = consumptionViewModel.getConsumptionDB().getFuelConsumptionData(name[0]);
			// TODO
			// fuelConsumptionList.clear();
			consumptionViewModel.getFuelConsumptionList().clear();
			while (result.moveToNext())
			{
				consumptionViewModel.getFuelConsumptionList().add(new FuelConsumption(result.getInt(0), result.getDouble(1), result.getDouble(2), result.getDouble(3), result.getString(4),
						result.getDouble(5), result.getDouble(6), result.getDouble(7), result.getInt(8), result.getString(9)));
			}

			result.close();
			return null;
			// dodaj ostale metode tukaj, ker šteka!!!!!
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			super.onPostExecute(aVoid);

			if (checkIfInserted == true)
			{
				UpdateConsumptionsAfterInsert(consumptionViewModel.getFuelConsumptionList() /* fuelConsumptionList */);
				showConsumationFromDB();
			}
			else
			{
				showConsumationFromDB();
			}

		}
	}

	// Asynctask, cursor loader cursor adapter
	private void showConsumationFromDB_Async(String name)
	{
		AddConsumationToList addConsumationToList = new AddConsumationToList();
		addConsumationToList.execute(name);

	}

	private void showConsumationFromDB()
	{
		consumptionViewModel.setFuelConsumptionAdapterNew(
				new FuelConsumptionAdapterNew(getActivity(), R.layout.display_fuel_row_consumption, consumptionViewModel.getFuelConsumptionList(), null, tvAverageConsumption));
		consumptionViewModel.getFuelConsumptionAdapterNew().setCustomListenerEdit(this);
		consumptionViewModel.getFuelConsumptionAdapterNew().setCustomListenerDelete(this);
		consumptionViewModel.getFuelConsumptionAdapterNew().setCustomListenerView(this);
		consumptionViewModel.getListViewConsumptions().setAdapter(consumptionViewModel.getFuelConsumptionAdapterNew());

		if (consumptionViewModel.getFuelConsumptionList().size() >= 2)
		{
			CalculateAvarageConsumption(consumptionViewModel.getFuelConsumptionList());
		}
		else
		{
			tvAverageConsumption.setText(Constants.NOT_APPLICABLE);
		}

	}

	private void CalculateAvarageConsumption(final List<FuelConsumption> fuelConsumptionList)
	{

		// DecimalFormat df = new DecimalFormat("#.##");

		FuelConsumption fuelConsumption;
		double firstOdometer = 0.0;
		double sumConsumption = 0.0;
		double lastOdometer = 0.0;
		double FinalConsumption;
		for (int i = 0; i < fuelConsumptionList.size() - 1; i++)
		{
			firstOdometer = fuelConsumptionList.get(0).getDistance(); // prestavi gor ko optimiziras
			sumConsumption += fuelConsumptionList.get(i).getPetrol();
			lastOdometer = fuelConsumptionList.get(fuelConsumptionList.size() - 1).getDistance() - firstOdometer;
		}

		FinalConsumption = sumConsumption / lastOdometer * 100;
		// String consumptionAverage = df.format(FinalConsumption);
		String consumptionAverage = consumptionViewModel.getDecimalFormat().format(FinalConsumption);
		tvAverageConsumption.setText(consumptionAverage + " l/100 km");
		SavePreferences.putAverage(getContext(), "average", (float) FinalConsumption);
	}

	// ---------------------------------------------------------------Button Edit------------------------------------------------------------------------------------
	private void updateConsumption(final FuelConsumption fuelConsumption, final int position)
	{
		// 8/4/2018
		// PriUpdate moraš dobiti vse podatke in jih preračunati in shraniti v bazo kot pri insertu!
		// popravi view za vse podatke
		// final DecimalFormat df = new DecimalFormat("#.##");
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.update_consumption_dialog, null);
		builder.setView(view);

		final String dateTank = String.valueOf(fuelConsumption.getDate());
		odometer = fuelConsumption.getDistance();
		final double distance_tmp = fuelConsumption.getDistanceTmp();
		final double price = fuelConsumption.getPrice();
		Log.i("FuelConsumptionEdit", dateTank + " || " + String.valueOf(odometer) + " || " + String.valueOf(price));
		final EditText editTextConsumption = view.findViewById(R.id.etPrice);
		final EditText editTextMilage = view.findViewById(R.id.etMilageUpdate);
		final EditText editTextPetrol = view.findViewById(R.id.etPetrolL);

		final TextView tvPriceUnit = view.findViewById(R.id.tvPriceUpdate);
		final TextView tvMilageUnit = view.findViewById(R.id.tvMilageUpdate);
		final TextView tvFuelUpdate = view.findViewById(R.id.tvFuelUpdate);

		tvPriceUnit.setText(String.valueOf("Cena v €/l")); // tukaj moraš dobiti enote od uporabnika

		editTextConsumption.setText(String.valueOf(fuelConsumption.getPrice())/* + " l/km" */);
		editTextMilage.setText(String.valueOf(fuelConsumption.getDistanceTmp()) /* + " km" */);
		editTextPetrol.setText(String.valueOf(fuelConsumption.getPetrol())/* + " l" */);

		final AlertDialog dialog = builder.create();
		dialog.show();

		view.findViewById(R.id.btnUpdateConsumption).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String ConsumptionL = editTextConsumption.getText().toString().trim();
				String Milage = editTextMilage.getText().toString().trim();
				String PetrolL = editTextPetrol.getText().toString().trim();
				String NewOdometer;

				double MilageDouble = Double.parseDouble(Milage);

				if (ConsumptionL.isEmpty())
				{
					editTextConsumption.setError("Polje ne sme biti prazno");
					editTextConsumption.requestFocus();
					return;
				}
				if (Milage.isEmpty())
				{
					editTextMilage.setError("Polje ne sme biti prazno");
					editTextMilage.requestFocus();
					return;
				}
				if (PetrolL.isEmpty())
				{
					editTextPetrol.setError("Polje ne sme biti prazno");
					editTextPetrol.requestFocus();
					return;
				}

				double PetrolLDouble = Double.valueOf(editTextPetrol.getText().toString().trim());
				double newTotalPrice = fuelConsumption.getPrice() * PetrolLDouble;
				// String formatedTotalPrice = df.format(newTotalPrice).trim();
				String formatedTotalPrice = consumptionViewModel.getDecimalFormat().format(newTotalPrice).trim();
				double PriceFinal = Double.parseDouble(formatedTotalPrice);
				String PriceFinalUpdate = String.valueOf(PriceFinal);

				editDistance = MilageDouble - distance_tmp;
				Log.d("editDistance", String.valueOf(editDistance));
				if (CheckDistanceTemp(position, MilageDouble) == false)
				{

					Log.d("Distance", String.valueOf(position));
					editTextMilage.setError("Napaka!");
					editTextMilage.requestFocus();
					return;
				}
				odometer = odometer + (editDistance);
				Log.d("odometer", String.valueOf(odometer));

				NewOdometer = String.valueOf(odometer);
				consumptionViewModel.getFuelConsumptionList().get(position).setConsumption(Double.valueOf(ConsumptionL));
				consumptionViewModel.getFuelConsumptionList().get(position).setDistanceTmp(Double.valueOf(Milage));
				consumptionViewModel.getFuelConsumptionList().get(position).setPetrol(Double.valueOf(PetrolL));
				consumptionViewModel.getFuelConsumptionList().get(position).setDistance(Double.valueOf(NewOdometer));
				consumptionViewModel.getFuelConsumptionList().get(position).setPrice(Double.valueOf(PriceFinalUpdate));

				updateOdometer(consumptionViewModel.getFuelConsumptionList());

				dialog.dismiss();

			}
		});

	}

	private void reloadConsumption(String con)
	{
		// TODO
		Cursor cursor = consumptionViewModel.getConsumptionDB().getFuelConsumptionData(con);
		consumptionViewModel.getFuelConsumptionList().clear();

		if (cursor.getCount() != 0)
		{
			while (cursor.moveToNext())
			{
				consumptionViewModel.getFuelConsumptionList().add(new FuelConsumption(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4),
						cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getInt(8), cursor.getString(9)));
			}
		}

		if (consumptionViewModel.getFuelConsumptionList().size() >= 2)
		{
			CalculateAvarageConsumption(consumptionViewModel.getFuelConsumptionList());
		}
		else
		{
			tvAverageConsumption.setText("N/A");
		}

		cursor.close();
		consumptionViewModel.getFuelConsumptionAdapterNew().notifyDataSetChanged();

	}

	private boolean CheckDistanceTemp(int position, double Milage)
	{

		if (position == consumptionViewModel.getFuelConsumptionList().size() - 1)
		{
			return true;
		}
		else if (consumptionViewModel.getFuelConsumptionList().size() >= 3 && position != 0)
		{
			double tmpDistance = Milage + consumptionViewModel.getFuelConsumptionList().get(position - 1).getDistance();
			if (tmpDistance < consumptionViewModel.getFuelConsumptionList().get(position + 1).getDistance())
			{
				return true;
			}
			else
				return false;

		}
		return false;
	}

	private void UpdateConsumptionsAfterDelete(List<FuelConsumption> fuelConsumptions, int position)
	{
		fuelConsumptions.remove(position);
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{

			double temp = fuelConsumptions.get(i).getDistance();
			double tempNext = fuelConsumptions.get(i + 1).getDistance();
			double tmpKM = tempNext - temp;

			fuelConsumptions.get(i + 1).setDistanceTmp(tmpKM);
		}
		// tukaj z novimi km izračunamo novo porabo
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{

			double tempPetrol = fuelConsumptions.get(i).getPetrol();
			double newConsumption = tempPetrol / fuelConsumptions.get(i + 1).getDistanceTmp() * 100;

			// String formatedConsumtion = df.format(newConsumption).trim();
			String formatedConsumtion = consumptionViewModel.getDecimalFormat().format(newConsumption).trim();
			double consumptionFinal = Double.parseDouble(formatedConsumtion);

			fuelConsumptions.get(i + 1).setConsumption(consumptionFinal);
		}
		// Vstavimo v bazo nove zapise!
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{
			consumptionViewModel.getConsumptionDB().UpdateConsumptionAfterDelete(String.valueOf(fuelConsumptions.get(i + 1).getDistanceTmp()),
					String.valueOf(fuelConsumptions.get(i + 1).getConsumption()), String.valueOf(fuelConsumptions.get(i + 1).getId()));
		}

		consumptionViewModel.getFuelConsumptionAdapterNew().notifyDataSetChanged();

		if (consumptionViewModel.getFuelConsumptionList().size() >= 2)
		{
			CalculateAvarageConsumption(consumptionViewModel.getFuelConsumptionList());
		}
		else
		{
			tvAverageConsumption.setText("N/A");
		}

	}

	private void updateOdometer(List<FuelConsumption> fuelConsumptionList)
	{
		double newOdometer = 0.0;
		String formatedConsumtion = "";

		// z to for zanko se sprehodim skozi nastavljen seznam za distance tmp in ponovno izracunam in dodam nazaj v seznam!
		for (int i = 1; i < fuelConsumptionList.size(); i++)
		{
			double v1 = fuelConsumptionList.get(i - 1).getDistance();
			double v2 = fuelConsumptionList.get(i).getDistanceTmp();
			newOdometer = v1 + v2;
			// String formatedConsumtion = df.format(newOdometer).trim();
			formatedConsumtion = consumptionViewModel.getDecimalFormat().format(newOdometer).trim();
			double FinalOdometer = Double.parseDouble(formatedConsumtion);
			fuelConsumptionList.get(i).setDistance(FinalOdometer);

		}
		// z to for zanko se sprehoidm cez seznam in dodam nove porabe goriva na koncu pa vse skupaj zapisem v bazo
		for (int i = 0; i < fuelConsumptionList.size() - 1; i++)
		{

			double tempPetrol = fuelConsumptionList.get(i).getPetrol();
			double newConsumption = tempPetrol / fuelConsumptionList.get(i + 1).getDistanceTmp() * 100;
			//String formatedConsumtion = df.format(newConsumption).trim();
			formatedConsumtion = consumptionViewModel.getDecimalFormat().format(newOdometer).trim();
			double FinalOdometer = Double.parseDouble(formatedConsumtion);
			fuelConsumptionList.get(i + 1).setConsumption(FinalOdometer);
		}
		// zapis podatkov v bazo
		for (int i = 0; i < fuelConsumptionList.size() - 1; i++)
		{
			Log.d("TEST_Seznam", String.valueOf(fuelConsumptionList.get(i).getDistance()) + i + " distance");
			consumptionViewModel.getConsumptionDB().UpdateOdometer(String.valueOf(fuelConsumptionList.get(i + 1).getDistance()), String.valueOf(fuelConsumptionList.get(i + 1).getDistanceTmp()),
					String.valueOf(fuelConsumptionList.get(i + 1).getConsumption()), String.valueOf(fuelConsumptionList.get(i + 1).getId()));
		}

		consumptionViewModel.getFuelConsumptionAdapterNew().notifyDataSetChanged();
		if (fuelConsumptionList.size() >= 2)
		{
			CalculateAvarageConsumption(fuelConsumptionList);
		}
		else
		{
			tvAverageConsumption.setText("N/A");
		}

	}

	// metode daj v async task, ker šteka
	private void UpdateConsumptionsAfterInsert(List<FuelConsumption> fuelConsumptions)
	{
		String formatedConsumtion="";
		// Najprej gremo čez seznamin set.amo nove zapise glede prevozenih km
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{

			double temp = fuelConsumptions.get(i).getDistance(); // prvi km 100000
			double tempNext = fuelConsumptions.get(i + 1).getDistance();// drugi km 101100
			double tmpKM = tempNext - temp;

			fuelConsumptions.get(i + 1).setDistanceTmp(tmpKM);
		}
		// tukaj z novimi km izračunamo novo porabo
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{

			double tempPetrol = fuelConsumptions.get(i).getPetrol();
			double newConsumption = tempPetrol / fuelConsumptions.get(i + 1).getDistanceTmp() * 100;

			//String formatedConsumtion = df.format(newConsumption).trim();
			formatedConsumtion = consumptionViewModel.getDecimalFormat().format(newConsumption).trim();
			double consumptionFinal = Double.parseDouble(formatedConsumtion);

			fuelConsumptions.get(i + 1).setConsumption(consumptionFinal);
		}
		// Vstavimo v bazo nove zapise!
		for (int i = 0; i < fuelConsumptions.size() - 1; i++)
		{
			consumptionViewModel.getConsumptionDB().UpdateConsumptionAfterDelete(String.valueOf(fuelConsumptions.get(i + 1).getDistanceTmp()),
					String.valueOf(fuelConsumptions.get(i + 1).getConsumption()), String.valueOf(fuelConsumptions.get(i + 1).getId()));
		}
		// fuelConsumptionAdapterNew.notifyDataSetChanged();
		if (consumptionViewModel.getFuelConsumptionList().size() >= 2)
		{
			CalculateAvarageConsumption(consumptionViewModel.getFuelConsumptionList());
		}
		else
		{
			tvAverageConsumption.setText("N/A");
		}

	}
}
