package com.example.gigi.poraba.Models;

public class TaskParam
{

	String name;
	String barChartType;
	StaticticalModel viewModel;

	public TaskParam(String name, String barChartType, StaticticalModel viewModel)
	{
		this.name = name;
		this.barChartType = barChartType;
		this.viewModel = viewModel;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBarChartType()
	{
		return barChartType;
	}

	public void setBarChartType(String barChartType)
	{
		this.barChartType = barChartType;
	}

	public StaticticalModel getViewModel()
	{
		return viewModel;
	}

	public void setViewModel(StaticticalModel viewModel)
	{
		this.viewModel = viewModel;
	}
}
