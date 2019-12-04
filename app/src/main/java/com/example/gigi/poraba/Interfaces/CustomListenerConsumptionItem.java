package com.example.gigi.poraba.Interfaces;

import com.example.gigi.poraba.Models.FuelConsumption;

public interface CustomListenerConsumptionItem
{
    void onButtonClickListenerEdit(int position, FuelConsumption value);
    void onButtonClickListenerDelete(int position, FuelConsumption value);
    void onItemClickListener(int position, FuelConsumption value);
}