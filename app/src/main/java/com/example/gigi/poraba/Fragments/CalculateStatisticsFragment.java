package com.example.gigi.poraba.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.gigi.poraba.Activities.InsertConsumption.DEFAULT;


public class CalculateStatisticsFragment extends Fragment {


    DatabaseHelper consumptionDB;
    SharedPreferences sharedPref;
    String name="";
    TextView tvStatistical;
    TextView tvStanjeStevca,tvSkupnaRazdalja,tvSkupnoGorivo,tvSkupniStroski,tvSkupnoSteviloPolnjenj,
            tvLetoDoDatuma,tvPrejsnjeLeto,tvTaMesec,tvPrejsnjiMesec,tvMaxPolnjenje,tvMinPolnjenje,tvLetoDoDatumaGorivo,
            tvPrejsnjeLetoGorivo,tvTaMesecGorivo,tvPrejsnjiMesecGorivo,tvLetoDoDatumaStroski,tvPrejsnjeLetoStroski,tvTaMesecStroski,
            tvPrejsnjiMesecStroski,tvNajvisjiRacun,tvNajnizjiRacun,tvNajboljsaCena,tvNajslabsaCena,tvNajboljsaCenaNaKilometer,tvNajslabsaCenaNaKilometer,
            tvPovprecnoPolnjenje,tvPovprecenRacunPolnjenja,tvPovprecnaCenaNaKilometer,tvPovprecniStrosekNaDan,tvPovprecniStrosekNaMesec,tvPovprecniKilometriNaDan,
            tvPovprecniKilometriNaMesec,tvPovrpecnaPorabaGoriva,tvNajmanjsaPorabaGoriva,tvNajvecjaPorabaGoriva;
    List<String> statisticalData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        consumptionDB=new DatabaseHelper(getActivity().getApplicationContext());
        sharedPref = getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        name = sharedPref.getString("Name", DEFAULT);
        statisticalData= new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calculate_statistics, container, false);
        //tvStatistical = (TextView) view.findViewById(R.id.tvStatisticalData);
        tvStanjeStevca=(TextView)view.findViewById(R.id.tvStanjeStevca);
        tvSkupnaRazdalja=(TextView)view.findViewById(R.id.tvSkupnaRazdalja);
        tvSkupnoGorivo=(TextView)view.findViewById(R.id.tvSkupnoGorivo);
        tvSkupniStroski=(TextView)view.findViewById(R.id.tvSkupniStroski);
        tvSkupnoSteviloPolnjenj=(TextView)view.findViewById(R.id.tvSkupnoSteviloPolnjenj);
        tvLetoDoDatuma=(TextView)view.findViewById(R.id.tvLetoDoDatuma);
        tvPrejsnjeLeto=(TextView)view.findViewById(R.id.tvPrejsnjeLeto);
        tvTaMesec=(TextView)view.findViewById(R.id.tvTaMesec);
        tvPrejsnjiMesec=(TextView)view.findViewById(R.id.tvPrejsniMesec);
        tvMaxPolnjenje=(TextView)view.findViewById(R.id.tvMaxPolnjenje);
        tvMinPolnjenje=(TextView)view.findViewById(R.id.tvMinPolnjenje);
        tvLetoDoDatumaGorivo=(TextView)view.findViewById(R.id.tvLetoDoDatumaGorivo);
        tvPrejsnjeLetoGorivo=(TextView)view.findViewById(R.id.tvPrejsnjeLetoGorivo);
        tvTaMesecGorivo=(TextView)view.findViewById(R.id.tvTaMesecGorivo);
        tvPrejsnjiMesecGorivo=(TextView)view.findViewById(R.id.tvPrejsnjiMesecGorivo);
        tvLetoDoDatumaStroski=(TextView)view.findViewById(R.id.tvLetoDoDatumaStroski);
        tvPrejsnjeLetoStroski=(TextView)view.findViewById(R.id.tvPrejsnjeLetoStroski);
        tvTaMesecStroski=(TextView) view.findViewById(R.id.tvTaMesecStroski);
        tvPrejsnjiMesecStroski=(TextView)view.findViewById(R.id.tvPrejsnjiMesecStroski);
        tvNajvisjiRacun=(TextView)view.findViewById(R.id.tvNajvisjiRacun);
        tvNajnizjiRacun=(TextView)view.findViewById(R.id.tvNajnizjiRacun);
        tvNajboljsaCena=(TextView)view.findViewById(R.id.tvNajboljsaCena);
        tvNajslabsaCena=(TextView)view.findViewById(R.id.tvNajslabsaCena);
        tvNajboljsaCenaNaKilometer=(TextView)view.findViewById(R.id.tvNajboljsaCenaNaKilometer);
        tvNajslabsaCenaNaKilometer=(TextView)view.findViewById(R.id.tvNajslabsaCenaNaKilometer);
        tvPovprecnoPolnjenje=(TextView)view.findViewById(R.id.tvPovprecnoPolnjenje);
        tvPovprecenRacunPolnjenja=(TextView)view.findViewById(R.id.tvPovprecenRacunPolnjenja);
        tvPovprecnaCenaNaKilometer=(TextView)view.findViewById(R.id.tvPovprecnaCenaNaKilometer);
        tvPovprecniStrosekNaDan=(TextView)view.findViewById(R.id.tvPovprecniStrosekNaDan);
        tvPovprecniStrosekNaMesec=(TextView)view.findViewById(R.id.tvPovprecniStrosekNaMesec);
        tvPovprecniKilometriNaDan=(TextView)view.findViewById(R.id.tvPovprecniKilometriNaDan);
        tvPovprecniKilometriNaMesec=(TextView)view.findViewById(R.id.tvPovprecniKilometriNaMesec);
        tvPovrpecnaPorabaGoriva=(TextView)view.findViewById(R.id.tvPovrpecnaPorabaGoriva);
        tvNajmanjsaPorabaGoriva=(TextView)view.findViewById(R.id.tvNajmanjsaPorabaGoriva);
        tvNajvecjaPorabaGoriva=(TextView)view.findViewById(R.id.tvNajvecjaPorabaGoriva);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getCalculateStatisticalData calculateData=new getCalculateStatisticalData();
        calculateData.execute(name);

    }

    private class getCalculateStatisticalData extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Cursor result=consumptionDB.getStatisticalData(strings[0]);
            while (result.moveToNext()) {
                statisticalData.add(result.getString(1));
                statisticalData.add(result.getString(2));
                statisticalData.add(result.getString(3));
                statisticalData.add(result.getString(4));
                statisticalData.add(result.getString(5));
                statisticalData.add(result.getString(6));
                statisticalData.add(result.getString(7));
                statisticalData.add(result.getString(8));
                statisticalData.add(result.getString(9));
                statisticalData.add(result.getString(10));
                statisticalData.add(result.getString(11));
                statisticalData.add(result.getString(12));
                statisticalData.add(result.getString(13));
                statisticalData.add(result.getString(14));
                statisticalData.add(result.getString(15));
                statisticalData.add(result.getString(16));
                statisticalData.add(result.getString(17));
                statisticalData.add(result.getString(18));
                statisticalData.add(result.getString(19));
                statisticalData.add(result.getString(20));
                statisticalData.add(result.getString(21));
                statisticalData.add(result.getString(22));
                statisticalData.add(result.getString(23));
                statisticalData.add(result.getString(24));
                statisticalData.add(result.getString(25));
                statisticalData.add(result.getString(26));
                statisticalData.add(result.getString(27));
                statisticalData.add(result.getString(28));
                statisticalData.add(result.getString(29));
                statisticalData.add(result.getString(30));
                statisticalData.add(result.getString(31));
                statisticalData.add(result.getString(32));
                statisticalData.add(result.getString(33));
                statisticalData.add(result.getString(34));
                statisticalData.add(result.getString(35));
            }
            result.close();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!statisticalData.isEmpty()) {
                tvStanjeStevca.setText("Stanje stevca: "+statisticalData.get(0));
                tvSkupnaRazdalja.setText("Skupna razdalja: "+statisticalData.get(1));
                tvSkupnoGorivo.setText("Skupna poraba goriva: "+statisticalData.get(2));
                tvSkupniStroski.setText("Skupni stroski: "+statisticalData.get(3));
                tvSkupnoSteviloPolnjenj.setText("Skupno stevilo polnjenj: "+statisticalData.get(4));
                tvLetoDoDatuma.setText("Leto do datuma (st. polnjenj): "+statisticalData.get(5));
                tvPrejsnjeLeto.setText("Prejsnje leto (st. polnjenj): "+statisticalData.get(6));
                tvTaMesec.setText("Ta mesec (st. polnjenj): "+statisticalData.get(7));
                tvPrejsnjiMesec.setText(tvPrejsnjiMesec.getText()+" "+statisticalData.get(8));
                tvMaxPolnjenje.setText(tvMaxPolnjenje.getText()+" "+statisticalData.get(9));
                tvMinPolnjenje.setText(tvMinPolnjenje.getText()+" "+statisticalData.get(10));
                tvLetoDoDatumaGorivo.setText(tvLetoDoDatumaGorivo.getText()+ " "+ statisticalData.get(11));
                tvPrejsnjeLetoGorivo.setText(tvPrejsnjeLetoGorivo.getText()+" "+ statisticalData.get(12));
                tvTaMesecGorivo.setText(tvTaMesecGorivo.getText()+" "+statisticalData.get(13));
                tvPrejsnjiMesecGorivo.setText(tvPrejsnjiMesecGorivo.getText()+" "+statisticalData.get(14));
                tvLetoDoDatumaStroski.setText(tvLetoDoDatumaStroski.getText()+" "+statisticalData.get(15));
                tvPrejsnjeLetoStroski.setText(tvPrejsnjeLetoStroski.getText()+" "+statisticalData.get(16));
                tvTaMesecStroski.setText(tvTaMesecStroski.getText()+" "+statisticalData.get(17));
                tvPrejsnjiMesecStroski.setText(tvPrejsnjiMesecStroski.getText()+" "+statisticalData.get(18));
                tvNajvisjiRacun.setText(tvNajvisjiRacun.getText()+ " "+statisticalData.get(19));
                tvNajnizjiRacun.setText(tvNajnizjiRacun.getText()+" "+statisticalData.get(20));
                tvNajboljsaCena.setText(tvNajboljsaCena.getText()+ " "+statisticalData.get(21));
                tvNajslabsaCena.setText(tvNajslabsaCena.getText()+" "+statisticalData.get(22));
                tvNajboljsaCenaNaKilometer.setText(tvNajboljsaCenaNaKilometer.getText()+" "+statisticalData.get(23));
                tvNajslabsaCenaNaKilometer.setText(tvNajslabsaCenaNaKilometer.getText()+" "+statisticalData.get(24));
                tvPovprecnoPolnjenje.setText(tvPovprecnoPolnjenje.getText()+""+statisticalData.get(25));
                tvPovprecenRacunPolnjenja.setText(tvPovprecenRacunPolnjenja.getText()+" "+statisticalData.get(26));
                tvPovprecnaCenaNaKilometer.setText(tvPovprecnaCenaNaKilometer.getText()+" "+statisticalData.get(27));
                tvPovprecniStrosekNaDan.setText(tvPovprecniStrosekNaDan.getText()+" "+statisticalData.get(28));
                tvPovprecniStrosekNaMesec.setText(tvPovprecniStrosekNaMesec.getText()+" "+statisticalData.get(29));
                tvPovprecniKilometriNaDan.setText(tvPovprecniKilometriNaDan.getText()+" "+statisticalData.get(30));
                tvPovprecniKilometriNaMesec.setText(tvPovprecniKilometriNaMesec.getText()+" "+statisticalData.get(31));
                tvPovrpecnaPorabaGoriva.setText(tvPovrpecnaPorabaGoriva.getText()+" "+statisticalData.get(32));
                tvNajmanjsaPorabaGoriva.setText(tvNajmanjsaPorabaGoriva.getText()+" "+statisticalData.get(33));
                tvNajvecjaPorabaGoriva.setText(tvNajvecjaPorabaGoriva.getText()+" "+statisticalData.get(34));
            }

        }
    }


}
