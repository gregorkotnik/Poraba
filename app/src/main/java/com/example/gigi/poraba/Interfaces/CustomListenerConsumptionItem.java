package com.example.gigi.poraba.Interfaces;

import com.example.gigi.poraba.Models.fuelConsumption;

public interface CustomListenerConsumptionItem
{
    void onButtonClickListenerEdit(int position, fuelConsumption value);
    void onButtonClickListenerDelete(int position, fuelConsumption value);
    void onItemClickListener(int position, fuelConsumption value);
}