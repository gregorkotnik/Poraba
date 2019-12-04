package com.example.gigi.poraba.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gigi.poraba.Models.FuelConsumption;
import com.example.gigi.poraba.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GIGI on 25/02/2018.
 */

public class FuelConsumptionAdapter extends ArrayAdapter {

    List list=new ArrayList();

    public FuelConsumptionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(FuelConsumption object) {
        //dodamo v list
        list.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        FuelConsumprionHolder fuelConsumprionHolder;

        if(row==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.display_fuel_row,parent,false);
            //row.findViewById(R.id.tvDatumShow);
            fuelConsumprionHolder=new FuelConsumprionHolder();
            fuelConsumprionHolder.tvConsumption=row.findViewById(R.id.tvConsumption);
            fuelConsumprionHolder.tvPrice=row.findViewById(R.id.tvPrice);
            fuelConsumprionHolder.tvDatumShow=row.findViewById(R.id.tvDatumShow);
            row.setTag(fuelConsumprionHolder);
        }
        else
        {
            fuelConsumprionHolder=(FuelConsumprionHolder) row.getTag();
        }

        FuelConsumption fuelConsumption=(FuelConsumption) getItem(position);
        //Potrebno imeti trenutne kilometre in prevožene, da lahko izracunam poabo
        //Double poraba=fuelConsumption.getFuel()/fuelConsumption.getCarMileage()
        // double finalConsumation=Fuel/Distance*100;

        fuelConsumprionHolder.tvPrice.setText(Double.toString(fuelConsumption.getPrice()));
        fuelConsumprionHolder.tvConsumption.setText(Double.toString(fuelConsumption.getConsumption())+" l/100km");
        fuelConsumprionHolder.tvDatumShow.setText(fuelConsumption.getDate());


        return row;
    }
    static class FuelConsumprionHolder
    {
        TextView tvPrice,tvConsumption,tvDatumShow;
    }
}
