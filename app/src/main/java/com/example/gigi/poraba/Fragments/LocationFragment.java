package com.example.gigi.poraba.Fragments;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.gigi.poraba.BuildConfig;
import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Models.GasStation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LocationFragment extends android.support.v4.app.Fragment
{

	Context context = getActivity();
	String[] values;
	Button btnGetData;
	public static ListView listView;
	public static ProgressBar spinner;
	TextView tvLat, tvLongi;

	private final String API_KEY = BuildConfig.API_KEY;
	private FusedLocationProviderClient fusedLocationClient;
	private boolean flag = false;
	static public final int REQUEST_LOCATION = 1;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.gasstationslocationfragment, container, false);
		btnGetData = (Button) view.findViewById(R.id.btnGetData);
		spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
		spinner.setVisibility(View.GONE);
		tvLat = (TextView) view.findViewById(R.id.tvLATITUDE);
		tvLongi = (TextView) view.findViewById(R.id.tvLONGITUDE);
		listView = (ListView) view.findViewById(R.id.lvJSON);
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

		btnGetData.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
				{

					Toast.makeText(context, "ZAVRNJENO", Toast.LENGTH_LONG).show();
					requestPermission();
					return;
				}

				fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>()
				{
					@Override
					public void onSuccess(Location location)
					{

						if (location != null)
						{
							FetchData fetchData = new FetchData();

							double Latitude = location.getLatitude();
							double Longitude = location.getLongitude();

							fetchData.execute(getActivity(), Latitude, Longitude);
							tvLongi.setText(String.valueOf(location.getLongitude()));
							tvLat.setText(String.valueOf(location.getLatitude()));
						}
					}
				});
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				String text = values[i].toString();

				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}
		});

		return view;
	}

	private void requestPermission()
	{
		ActivityCompat.requestPermissions(getActivity(), new String[] { ACCESS_FINE_LOCATION }, 1);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class FetchData extends AsyncTask<Object, Void, Void>
	{

		String data;
		String getSingleData2 = "";
		String json;
		List<GasStation> listOfGasStations;
		ArrayAdapter<String> adapter;
		ArrayAdapter<GasStation> adapterGasStationLocation;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// MainActivity.spinner.setVisibility(View.GONE);
			spinner.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(Object... voids)
		{
			try
			{

				Context ctx = (Context) voids[0];
				double d1 = (Double) voids[1];
				double d2 = (Double) voids[2];

				String lat = String.valueOf(d1);
				String longi = String.valueOf(d2);

				Location startPoint = new Location("locationA");
				startPoint.setLatitude(d1);
				startPoint.setLongitude(d2);

				URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + longi
						+ "&radius=1500&type=gas_station&rankBy=1500&key="+API_KEY);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				InputStream inputStream = httpURLConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));// ,"utf-8"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = bufferedReader.readLine()) != null)
				{
					sb.append(line);
				}
				inputStream.close();
				json = sb.toString();

				JSONObject jsonObject = new JSONObject(json);
				JSONArray JA1 = (JSONArray) jsonObject.get("results");
				// JSONObject JO1= (JSONObject) JA1.get(0);
				// JSONObject JO2= (JSONObject) JO1.get("geometry");
				// doloci velikost arrayem
				values = new String[JA1.length()];
				listOfGasStations = new ArrayList<>();

				// parseData=JO2.toString();
				for (int i = 0; i < JA1.length(); i++)
				{
					JSONObject JO = (JSONObject) JA1.get(i);
					double d3, d4;
					JSONObject JO2 = (JSONObject) JO.get("geometry");
					JSONObject JO3 = (JSONObject) JO2.get("location");
					Location endPoint = new Location("locationA");
					d3 = Double.valueOf(JO3.getDouble("lat"));
					d4 = Double.valueOf(JO3.getDouble("lng"));
					endPoint.setLatitude(d3);
					endPoint.setLongitude(d4);

					double distance = (double) startPoint.distanceTo(endPoint);
					getSingleData2 = JO.get("name") + "\n";

					listOfGasStations.add(new GasStation(getSingleData2, distance));

				}

				for (int i = 0; i < listOfGasStations.size(); i++)
				{
					// Log.d("test: "+i,values[i]);
					Log.d("GasLIST", String.valueOf(listOfGasStations.get(i).getDistance()));
				}
				for (int i = 0; i < listOfGasStations.size(); i++)
				{
					Log.d("Unsorted", String.valueOf(listOfGasStations.get(i).getDistance()));
				}
				Collections.sort(listOfGasStations, new Comparator<GasStation>()
				{
					@Override
					public int compare(GasStation gasStation, GasStation t1)
					{
						return Double.compare(gasStation.getDistance(), t1.getDistance());
					}
				});
				for (int i = 0; i < listOfGasStations.size(); i++)
				{
					double distance = listOfGasStations.get(i).getDistance();
					int distanceFinal = (int) distance;
					Log.d("Sorted" + i, String.valueOf(listOfGasStations.get(i).getDistance()));
					values[i] = String.valueOf(listOfGasStations.get(i).getAdress() + String.valueOf(distanceFinal) + " m");
				}
				adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, values);
				adapterGasStationLocation = new ArrayAdapter<GasStation>(ctx, android.R.layout.simple_list_item_1, listOfGasStations);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			super.onPostExecute(aVoid);

			// MainActivity.tvData.setText(this.parseData);
			listView.setAdapter(adapter);
			spinner.setVisibility(View.GONE);
			// mainActivity.spinner.setVisibility(View.GONE);

		}

	}
}
