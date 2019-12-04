package com.example.gigi.poraba.Adapters;

import java.util.List;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Interfaces.CustomListenerConsumptionItem;
import com.example.gigi.poraba.Models.FuelConsumption;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class FuelConsumptionAdapterNew extends ArrayAdapter<FuelConsumption>
{
	Context ctx;
	int listLayoutRes;
	List<FuelConsumption> consumptionList;
	DatabaseHelper databaseHelper;
	TextView textView;

	CustomListenerConsumptionItem customListenerEdit;
	CustomListenerConsumptionItem customListenerDelete;
	CustomListenerConsumptionItem customListenerView;

	public FuelConsumptionAdapterNew(@NonNull Context ctx, int listLayoutRes, List<FuelConsumption> consumptionList, DatabaseHelper databaseHelper, TextView textView)
	{
		super(ctx, listLayoutRes, consumptionList);
		this.databaseHelper = databaseHelper;
		this.ctx = ctx;
		this.listLayoutRes = listLayoutRes;
		this.consumptionList = consumptionList;
		this.textView = textView;

	}

	// TODO 26.11. CREATE INTERFACE WITH ALL THREE METHOD you already have interface method

	public void setCustomListenerEdit(CustomListenerConsumptionItem listener)
	{
		this.customListenerEdit = listener;
	}

	public void setCustomListenerDelete(CustomListenerConsumptionItem listener)
	{
		this.customListenerDelete = listener;
	}

	public void setCustomListenerView(CustomListenerConsumptionItem listener)
	{
		this.customListenerView = listener;
	}

	@NonNull
	@Override
	public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{

		ViewHolder viewHolder;
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(ctx);
			convertView = inflater.inflate(listLayoutRes, null);
			viewHolder = new ViewHolder();
			viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.tvDateFuelRow);
			viewHolder.textViewMilage = (TextView) convertView.findViewById(R.id.tvMilageFuelRow);
			viewHolder.textViewConsumption = (TextView) convertView.findViewById(R.id.tvConsumptionL);
			viewHolder.textViewDistance = (TextView) convertView.findViewById(R.id.tvMilage);
			viewHolder.textViewPetrol = (TextView) convertView.findViewById(R.id.tvFuelLoaded);
			viewHolder.textViewTotalPrice = (TextView) convertView.findViewById(R.id.tvPriceConsumption);
			viewHolder.btnEdit = (Button) convertView.findViewById(R.id.btnEditConsumption);
			viewHolder.btnDelete = (Button) convertView.findViewById(R.id.btnDeleteConsumption);
			viewHolder.view = convertView;
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final FuelConsumption temp = getItem(position);
		viewHolder.textViewDate.setText(String.valueOf(temp.getDate()));
		viewHolder.textViewMilage.setText(String.valueOf(temp.getDistance()));
		viewHolder.textViewTotalPrice.setText(String.valueOf(temp.getTotalPrice()));
		viewHolder.textViewConsumption.setText(String.valueOf(temp.getConsumption()));
		viewHolder.textViewDistance.setText(String.valueOf(temp.getDistanceTmp()));
		viewHolder.textViewPetrol.setText(String.valueOf(temp.getPetrol()));

		viewHolder.btnEdit.setOnClickListener(view -> {
			if (customListenerEdit != null)
			{
				customListenerEdit.onButtonClickListenerEdit(position, temp);
			}
		});

		viewHolder.btnDelete.setOnClickListener(view -> {
			if (customListenerDelete != null)
			{
				customListenerDelete.onButtonClickListenerDelete(position, temp);
			}
		});

		viewHolder.view.setOnClickListener(view -> {
			if (customListenerView != null)
			{
				customListenerView.onItemClickListener(position, temp);
			}
		});

		return convertView;

	}

	public class ViewHolder
	{
		Button btnDelete, btnEdit;
		TextView textViewDate, textViewMilage, textViewConsumption, textViewDistance, textViewPetrol, textViewTotalPrice;
		View view;
	}

}
