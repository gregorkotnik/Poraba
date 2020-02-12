package com.example.gigi.poraba.Fragments;

import static com.example.gigi.poraba.Activities.InsertConsumption.DEFAULT;
import static com.example.gigi.poraba.Constants.Constants.DAY;
import static com.example.gigi.poraba.Constants.Constants.MONTH;
import static com.example.gigi.poraba.Constants.Constants.YEAR;
import static com.example.gigi.poraba.Utils.StatisticalMethods.avgConsumptionDay;
import static com.example.gigi.poraba.Utils.StatisticalMethods.avgConsumptionMonth;
import static com.example.gigi.poraba.Utils.StatisticalMethods.avgConsumptionYear;

import java.util.ArrayList;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Models.ConsumptionStatistical;
import com.example.gigi.poraba.Models.StaticticalModel;
import com.example.gigi.poraba.Models.TaskParam;
import com.example.gigi.poraba.Utils.SavePreferences;
import com.example.gigi.poraba.Utils.StatisticalMethods;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class StatisticalFragmentController extends Fragment implements AdapterView.OnItemSelectedListener
{
	private Spinner BarChartSelection;
	private String ChartType;
	public String[] dates;
	private static boolean monthFlag = false;
	private static boolean yearFlag = false;

	private static StaticticalModel staticticalModel;

	public StatisticalFragmentController()
	{
		staticticalModel = new StaticticalModel();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		staticticalModel.setBarEntries(new ArrayList<BarEntry>());
		staticticalModel.setLineData(new ArrayList<LineData>());
		staticticalModel.setConsumptionDB(new DatabaseHelper(getActivity().getApplicationContext()));
		staticticalModel.setFuelConsumptionListDates(new ArrayList<String>());
		staticticalModel.setFuelConsumptionList(new ArrayList<Double>());
		staticticalModel.setConsumptionValues(new ArrayList<com.example.gigi.poraba.Models.ConsumptionValue>());
		staticticalModel.setListConsumption(new ArrayList<ConsumptionStatistical>());
		staticticalModel.setSharedPref(getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE));
		staticticalModel.setName(staticticalModel.getSharedPref().getString("Name", DEFAULT));
		staticticalModel.setLeftString("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.fragment_statistical, container, false);
		staticticalModel.setBarChart((BarChart) view.findViewById(R.id.barchart1));
		BarChartSelection = view.findViewById(R.id.SpinnerCharts);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.chartType, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		BarChartSelection.setAdapter(adapter);
		BarChartSelection.setOnItemSelectedListener(this);
		staticticalModel.getBarChart().setVisibility(View.GONE);

		return view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	private void showConsumptionDates(String name, String type)
	{
		AddConsumationDatesToList addConsumationDatesToList = new AddConsumationDatesToList();
		TaskParam taskParam = new TaskParam(name, type, staticticalModel);
		addConsumationDatesToList.execute(taskParam);
	}

	private class AddConsumationDatesToList extends AsyncTask<TaskParam, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(TaskParam... params)
		{
			setConsumption(params[0]);
			setBarChart();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			staticticalModel.getBarChart().setVisibility(View.VISIBLE);
		}
	}

	private void setBarChart()
	{
		staticticalModel.getBarEntries().clear();

		if (staticticalModel.getConsumptionValues().size() > 0)
		{
			if (ChartType.contains(DAY))
			{
				for (int i = 0; i < staticticalModel.getConsumptionValues().size(); i++)
				{
					if (staticticalModel.getConsumptionValues().get(i).getConsumption() != 0.0)
					{
						staticticalModel.getBarEntries().add(new BarEntry(i, (float) staticticalModel.getConsumptionValues().get(i).getConsumption()));
					}
				}
				staticticalModel.setDataSetLabel("Average consumption - Day");
			}
			else if (ChartType.contains(MONTH))
			{
				for (int i = 0; i < staticticalModel.getConsumptionValues().size(); i++)
				{
					staticticalModel.getBarEntries().add(new BarEntry(i, (float) staticticalModel.getConsumptionValues().get(i).getConsumption()));
				}
				staticticalModel.setDataSetLabel("Average consumption - Month");
			}
			else if (ChartType.contains(YEAR))
			{

				for (int i = 0; i < staticticalModel.getConsumptionValues().size(); i++)
				{
					staticticalModel.getBarEntries().add(new BarEntry(i, (float) staticticalModel.getConsumptionValues().get(i).getConsumption()));
				}
				staticticalModel.setDataSetLabel("Average consumption - Year");
			}

			StatisticalMethods.setBarData(staticticalModel);
			setBarChartStyle();
			// yearFlag = false;
			// monthFlag = false;
		}
		else
		{
			staticticalModel.getBarChart().setNoDataText("No data");
		}

		yearFlag = false;
		monthFlag = false;
	}

	private void setBarChartStyle()
	{
		float limitValue = SavePreferences.getAverage(getContext(), "average");
		StatisticalMethods.setBarChartStyle(staticticalModel, monthFlag, yearFlag, limitValue);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
	{

		if (adapterView.getId() == R.id.SpinnerCharts)
		{
			ChartType = adapterView.getItemAtPosition(i).toString();
			if (!staticticalModel.getName().isEmpty())
			{
				showConsumptionDates(staticticalModel.getName(), ChartType);
			}
			else
			{
				Toast.makeText(getActivity(), "Napaka!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView)
	{

	}

	public void setConsumption(TaskParam param)
	{
		Cursor result, result_month, result_year;
		result = staticticalModel.getConsumptionDB().getFuelConsumptionAvgData(param.getName());

		switch (param.getBarChartType())
		{
			case DAY:
				break;

			case MONTH:
				monthFlag = true;
				break;

			case YEAR:
				yearFlag = true;
				break;

			default:
				monthFlag = false;
				yearFlag = true;
		}
		clearLists();

		while (result.moveToNext())
		{
			staticticalModel.getListConsumption().add(new ConsumptionStatistical(result.getInt(0), result.getInt(1), result.getInt(2), result.getDouble(3), result.getDouble(4), result.getDouble(5),
					result.getDouble(6), result.getString(7)));
		}

		result.close();

		if (monthFlag == true)
		{
			avgConsumptionMonth(staticticalModel);
		}
		else if (yearFlag == true)
		{
			avgConsumptionYear(staticticalModel);
		}
		else
		{
			avgConsumptionDay(staticticalModel);
		}
	}

	private void clearLists()
	{
		staticticalModel.getFuelConsumptionListDates().clear();
		staticticalModel.getFuelConsumptionList().clear();
		staticticalModel.getConsumptionValues().clear();
		staticticalModel.getBarEntries().clear();
		staticticalModel.getListConsumption().clear();
	}

	/*
	 * //https://stackoverflow.com/questions/29888850/mpandroidchart-set-different-color-to-bar-in-a-bar-chart-based-on-y-axis-values
	 */
}
