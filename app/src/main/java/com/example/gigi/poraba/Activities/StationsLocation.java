package com.example.gigi.poraba.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gigi.poraba.Models.GasStation;
import com.example.gigi.poraba.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StationsLocation extends AppCompatActivity {

    String[] values;
    Button btnGetData;
    public static TextView tvData;
    TextView tvLat, tvLongi;
    public static ListView listView;
    public static ProgressBar spinner;

    private final String API_KEY = "AIzaSyCCvNVzUotAZ2X-NQwVTQpRPfykSSwd1z0";
    private FusedLocationProviderClient fusedLocationClient;
    private boolean flag = false;
    static public final int REQUEST_LOCATION = 1;
    List<GasStation> listOfGasStations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_location);

        requestPermission();
        btnGetData = (Button) findViewById(R.id.btnGetData);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        tvLat = (TextView) findViewById(R.id.tvLATITUDE);
        tvLongi = (TextView) findViewById(R.id.tvLONGITUDE);
        listOfGasStations=new ArrayList<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        listView = (ListView) findViewById(R.id.lvJSON);
//        btnGetData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (ActivityCompat.checkSelfPermission(StationsLocation.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//                    Toast.makeText(StationsLocation.this,"ZAVRNJENO",Toast.LENGTH_LONG).show();
//                    requestPermission();
//                    return;
//                }
//
//                fusedLocationClient.getLastLocation().addOnSuccessListener(StationsLocation.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//
//                        if(location!=null)
//                        {
//                            FetchData fetchData = new FetchData();
//
//                            double Latitude=location.getLatitude();
//                            double Longitude=location.getLongitude();
//
//                            fetchData.execute(StationsLocation.this,Latitude,Longitude);
//                            tvLongi.setText(String.valueOf(location.getLongitude()));
//                            tvLat.setText(String.valueOf(location.getLatitude()));
//                        }
//                    }
//                });
//
//            }
//        });

        if (ActivityCompat.checkSelfPermission(StationsLocation.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    Toast.makeText(StationsLocation.this,"ZAVRNJENO",Toast.LENGTH_LONG).show();
                    requestPermission();
                    return;
                }

                fusedLocationClient.getLastLocation().addOnSuccessListener(StationsLocation.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if(location!=null)
                        {
                            FetchData fetchData = new FetchData();

                            double Latitude=location.getLatitude();
                            double Longitude=location.getLongitude();

                            fetchData.execute(StationsLocation.this,Latitude,Longitude);
                            tvLongi.setText(String.valueOf(location.getLongitude()));
                            tvLat.setText(String.valueOf(location.getLatitude()));
                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text=values[i].toString();
                GasStation gasStation=new GasStation(listOfGasStations.get(i).getAdress(),listOfGasStations.get(i).getDistance());

                //podatki poslani nazaj v insertConsumption activity
                Intent intent=new Intent(StationsLocation.this, insertConsumption.class);
                intent.putExtra("gasStationAdress",gasStation.getAdress().toString());
                intent.putExtra("gasStationDistance",gasStation.getDistance());
                startActivity(intent);
                finish();
                //Toast.makeText(StationsLocation.this,text,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class FetchData extends AsyncTask<Object,Void,Void> {

        String data;
        String getSingleData2="";
        String json;
        //List<GasStation> listOfGasStations;
        ArrayAdapter<String> adapter;
        ArrayAdapter<GasStation> adapterGasStationLocation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //MainActivity.spinner.setVisibility(View.GONE);
            StationsLocation.spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Object... voids) {
            try {

                Context ctx=(Context) voids[0];
                double  d1=(Double)voids[1];
                double  d2=(Double)voids[2];

                String lat=String.valueOf(d1);
                String longi=String.valueOf(d2);

                Location startPoint=new Location("locationA");
                startPoint.setLatitude(d1);
                startPoint.setLongitude(d2);

                URL url=new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+longi+"&radius=1500&type=gas_station&rankBy=1500&key=AIzaSyCCvNVzUotAZ2X-NQwVTQpRPfykSSwd1z0");
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));//,"utf-8"),8);
                StringBuilder sb=new StringBuilder();
                String line=null;

                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                inputStream.close();
                json=sb.toString();

                JSONObject jsonObject=new JSONObject(json);
                JSONArray JA1= (JSONArray) jsonObject.get("results");
                //JSONObject JO1= (JSONObject) JA1.get(0);
                //JSONObject JO2= (JSONObject) JO1.get("geometry");
                //doloci velikost arrayem
                values=new String[JA1.length()];


                //parseData=JO2.toString();
                for(int i=0;i<JA1.length();i++)
                {
                    JSONObject JO=(JSONObject)JA1.get(i);
                    double d3,d4;
                    JSONObject JO2=(JSONObject)JO.get("geometry");
                    JSONObject JO3=(JSONObject)JO2.get("location");
                    Location endPoint=new Location("locationA");
                    d3=Double.valueOf(JO3.getDouble("lat"));
                    d4=Double.valueOf(JO3.getDouble("lng"));
                    endPoint.setLatitude(d3);
                    endPoint.setLongitude(d4);

                    double distance= (double) startPoint.distanceTo(endPoint);
                    getSingleData2=JO.get("name")+"\n";

                    listOfGasStations.add(new GasStation(getSingleData2,distance));

                }


                for (int i=0;i<listOfGasStations.size();i++)
                {
                    //Log.d("test: "+i,values[i]);
                    Log.d("GasLIST",String.valueOf(listOfGasStations.get(i).getDistance()));
                }
                for(int i=0;i<listOfGasStations.size();i++)
                {
                    Log.d("Unsorted",String.valueOf(listOfGasStations.get(i).getDistance()));
                }
                Collections.sort(listOfGasStations, new Comparator<GasStation>() {
                    @Override
                    public int compare(GasStation gasStation, GasStation t1) {
                        return Double.compare(gasStation.getDistance(),t1.getDistance());
                    }
                });
                for(int i=0;i<listOfGasStations.size();i++)
                {
                    double distance=listOfGasStations.get(i).getDistance();
                    int distanceFinal= (int) distance;
                    Log.d("Sorted"+i,String.valueOf(listOfGasStations.get(i).getDistance()));
                    values[i]=String.valueOf(listOfGasStations.get(i).getAdress()
                            +String.valueOf(distanceFinal)+" m");
                }
                adapter=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,values);
                adapterGasStationLocation=new ArrayAdapter<GasStation>(ctx,android.R.layout.simple_list_item_1,listOfGasStations);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //MainActivity.tvData.setText(this.parseData);
            StationsLocation.listView.setAdapter(adapter);
            StationsLocation.spinner.setVisibility(View.GONE);
            //mainActivity.spinner.setVisibility(View.GONE);

        }


    }

}
