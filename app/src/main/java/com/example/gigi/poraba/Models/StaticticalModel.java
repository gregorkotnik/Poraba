package com.example.gigi.poraba.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Models.ConsumptionStatistical;
import com.example.gigi.poraba.Models.ConsumptionValue;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;

import android.content.SharedPreferences;
import android.widget.Spinner;

public class StaticticalModel
{
	private BarChart barChart;
	private LineChart lineChart;
	private ArrayList<BarEntry> barEntries;
	private ArrayList<LineData> lineData;
	private DatabaseHelper consumptionDB;
	private List<String> fuelConsumptionListDates;
	private List<Double> fuelConsumptionList;
	private List<Date> ListOfDates;
	private List<ConsumptionValue> consumptionValues;
	private SharedPreferences sharedPref;
	private String name = "";
	private String leftString;
	private Spinner BarChartSelection;
	private String ChartType;
	private String[] dates;
	private String dataSetLabel = "";
	private boolean monthFlag = false;
	private boolean yearFlag = false;
	private List<ConsumptionStatistical> listConsumption;
	private List<ConsumptionStatistical> consumptionStatisticalsDay;
	private List<ConsumptionStatistical> consumptionStatisticalsMonth;
	private List<ConsumptionStatistical> consumptionStatisticalsYear;

	public BarChart getBarChart()
	{
		return barChart;
	}

	public void setBarChart(BarChart barChart)
	{
		this.barChart = barChart;
	}

	public LineChart getLineChart()
	{
		return lineChart;
	}

	public void setLineChart(LineChart lineChart)
	{
		this.lineChart = lineChart;
	}

	public ArrayList<BarEntry> getBarEntries()
	{
		return barEntries;
	}

	public void setBarEntries(ArrayList<BarEntry> barEntries)
	{
		this.barEntries = barEntries;
	}

	public ArrayList<LineData> getLineData()
	{
		return lineData;
	}

	public void setLineData(ArrayList<LineData> lineData)
	{
		this.lineData = lineData;
	}

	public DatabaseHelper getConsumptionDB()
	{
		return consumptionDB;
	}

	public void setConsumptionDB(DatabaseHelper consumptionDB)
	{
		this.consumptionDB = consumptionDB;
	}

	public List<String> getFuelConsumptionListDates()
	{
		return fuelConsumptionListDates;
	}

	public void setFuelConsumptionListDates(List<String> fuelConsumptionListDates)
	{
		this.fuelConsumptionListDates = fuelConsumptionListDates;
	}

	public List<Double> getFuelConsumptionList()
	{
		return fuelConsumptionList;
	}

	public void setFuelConsumptionList(List<Double> fuelConsumptionList)
	{
		this.fuelConsumptionList = fuelConsumptionList;
	}

	public List<Date> getListOfDates()
	{
		return ListOfDates;
	}

	public void setListOfDates(List<Date> listOfDates)
	{
		ListOfDates = listOfDates;
	}

	public List<ConsumptionValue> getConsumptionValues()
	{
		return consumptionValues;
	}

	public void setConsumptionValues(List<ConsumptionValue> consumptionValues)
	{
		this.consumptionValues = consumptionValues;
	}

	public SharedPreferences getSharedPref()
	{
		return sharedPref;
	}

	public void setSharedPref(SharedPreferences sharedPref)
	{
		this.sharedPref = sharedPref;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLeftString()
	{
		return leftString;
	}

	public void setLeftString(String leftString)
	{
		this.leftString = leftString;
	}

	public Spinner getBarChartSelection()
	{
		return BarChartSelection;
	}

	public void setBarChartSelection(Spinner barChartSelection)
	{
		BarChartSelection = barChartSelection;
	}

	public String getChartType()
	{
		return ChartType;
	}

	public void setChartType(String chartType)
	{
		ChartType = chartType;
	}

	public String[] getDates()
	{
		return dates;
	}

	public void setDates(String[] dates)
	{
		this.dates = dates;
	}

	public boolean isMonthFlag()
	{
		return monthFlag;
	}

	public void setMonthFlag(boolean monthFlag)
	{
		this.monthFlag = monthFlag;
	}

	public boolean isYearFlag()
	{
		return yearFlag;
	}

	public void setYearFlag(boolean yearFlag)
	{
		this.yearFlag = yearFlag;
	}

	public List<ConsumptionStatistical> getListConsumption()
	{
		return listConsumption;
	}

	public void setListConsumption(List<ConsumptionStatistical> listConsumption)
	{
		this.listConsumption = listConsumption;
	}

	public List<ConsumptionStatistical> getConsumptionStatisticalsDay()
	{
		return consumptionStatisticalsDay;
	}

	public void setConsumptionStatisticalsDay(List<ConsumptionStatistical> consumptionStatisticalsDay)
	{
		this.consumptionStatisticalsDay = consumptionStatisticalsDay;
	}

	public List<ConsumptionStatistical> getConsumptionStatisticalsMonth()
	{
		return consumptionStatisticalsMonth;
	}

	public void setConsumptionStatisticalsMonth(List<ConsumptionStatistical> consumptionStatisticalsMonth)
	{
		this.consumptionStatisticalsMonth = consumptionStatisticalsMonth;
	}

	public List<ConsumptionStatistical> getConsumptionStatisticalsYear()
	{
		return consumptionStatisticalsYear;
	}

	public void setConsumptionStatisticalsYear(List<ConsumptionStatistical> consumptionStatisticalsYear)
	{
		this.consumptionStatisticalsYear = consumptionStatisticalsYear;
	}

	public String getDataSetLabel()
	{
		return dataSetLabel;
	}

	public void setDataSetLabel(String dataSetLabel)
	{
		this.dataSetLabel = dataSetLabel;
	}
}
