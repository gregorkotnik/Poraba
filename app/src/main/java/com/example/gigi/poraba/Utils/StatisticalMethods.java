package com.example.gigi.poraba.Utils;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.example.gigi.poraba.Models.ConsumptionStatistical;
import com.example.gigi.poraba.Models.ConsumptionValue;
import com.example.gigi.poraba.Models.StaticticalModel;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.graphics.Color;

public class StatisticalMethods
{

	private static void clearLists(StaticticalModel staticticalModel)
	{
		if (staticticalModel.getConsumptionStatisticalsDay() != null)
		{
			staticticalModel.getConsumptionStatisticalsDay().clear();
		}
		if (staticticalModel.getConsumptionStatisticalsMonth() != null)
		{
			staticticalModel.getConsumptionStatisticalsMonth().clear();
		}
		if (staticticalModel.getConsumptionStatisticalsYear() != null)
		{
			staticticalModel.getConsumptionStatisticalsYear().clear();
		}
		// staticticalModel.getListMonthYear().clear();

	}

	private static List<ConsumptionStatistical> sortList(List<ConsumptionStatistical> list, Function<ConsumptionStatistical, String> function)
	{
		final Map<String, List<ConsumptionStatistical>> dayList = list.stream().collect(groupingBy(function));
		final List<ConsumptionStatistical> listConsumption = new ArrayList<>();

		for (Map.Entry<String, List<ConsumptionStatistical>> entry : dayList.entrySet())
		{
			ConsumptionStatistical uniqueValue = entry.getValue().get(0);
			listConsumption.add(uniqueValue);
		}
		return listConsumption.stream().sorted(Comparator.comparingDouble(ConsumptionStatistical::getDistance)).collect(toList());
	}

	private static List<ConsumptionStatistical> sortListYear(List<ConsumptionStatistical> list, Function<ConsumptionStatistical, Integer> function)
	{
		final Map<Integer, List<ConsumptionStatistical>> yearList = list.stream().collect(groupingBy(function));
		final List<ConsumptionStatistical> listConsumption = new ArrayList<>();

		for (Map.Entry<Integer, List<ConsumptionStatistical>> entry : yearList.entrySet())
		{
			ConsumptionStatistical uniqueValue = entry.getValue().get(0);
			listConsumption.add(uniqueValue);
		}
		return listConsumption.stream().sorted(Comparator.comparingInt(ConsumptionStatistical::getYear)).collect(toList());
	}

	private static List<ConsumptionStatistical> sortListMonth(List<ConsumptionStatistical> list)
	{
		Function<ConsumptionValue, String> comp = p -> StringUtils.join(p.getMonth(), p.getYear());
		Map<Pair<Integer, Integer>, List<ConsumptionStatistical>> monthList = list.stream()
				.collect(Collectors.groupingBy(p -> Pair.of(p.getMonth(), p.getYear()), Collectors.mapping((ConsumptionStatistical p) -> p, toList())));
		final List<ConsumptionStatistical> listConsumption = new ArrayList<>();

		for (Map.Entry<Pair<Integer, Integer>, List<ConsumptionStatistical>> entry : monthList.entrySet())
		{
			listConsumption.add(entry.getValue().get(0));
		}

		return listConsumption.stream().sorted(Comparator.comparingDouble(ConsumptionStatistical::getDistance)).collect(toList());

	}

	public static void avgConsumptionDay(StaticticalModel staticticalModel)
	{
		double avg = 0.0;
		double liters = 0;
		double distance = 0;

		int month;
		int year;
		int day;

		List<ConsumptionStatistical> consumptionStatisticals = staticticalModel.getListConsumption();
		staticticalModel.setConsumptionStatisticalsDay(sortList(consumptionStatisticals, ConsumptionStatistical::getDate));

		for (int i = 0; i < staticticalModel.getConsumptionStatisticalsDay().size(); i++)
		{
			day = staticticalModel.getConsumptionStatisticalsDay().get(i).getDay();
			month = staticticalModel.getConsumptionStatisticalsDay().get(i).getMonth();
			year = staticticalModel.getConsumptionStatisticalsDay().get(i).getYear();

			int counter = 0;
			int size = consumptionStatisticals.size();

			if (size == 1)
			{
				avg = 0;
			}
			else
			{
				if (size != 0)
				{
					for (int j = 1; j < size; j++)
					{
						if (consumptionStatisticals.get(j).getDay() == day && consumptionStatisticals.get(j).getMonth() == month && consumptionStatisticals.get(j).getYear() == year)
						{
							counter++;
							if (j == size - 1 && counter > 1)
							{
								avg = consumptionStatisticals.get(j).getConsumption();
								break;
							}
							liters += consumptionStatisticals.get(j - 1).getPetrol();
							distance += consumptionStatisticals.get(j).getTemporaryDistance();
						}
						counter = 0;

					}
					if (avg == 0.0 && distance != 0.0)
					{
						avg = (liters / distance) * 100;
					}
					else
					{
						avg = 0;
					}
					distance = 0;
					liters = 0;
				}

			}

			ConsumptionValue consumptionValue = new ConsumptionValue(staticticalModel.getConsumptionStatisticalsDay().get(i).getDate(), avg, String.valueOf(year), day, month);
			staticticalModel.getConsumptionValues().add(consumptionValue);
			avg = 0;
		}

		clearLists(staticticalModel);
	}

	public static void avgConsumptionMonth(StaticticalModel staticticalModel)
	{
		double avg = 0.0;
		double liters = 0;
		double distance = 0;

		int month;
		int year;
		int day;

		List<ConsumptionStatistical> consumptionStatisticals = staticticalModel.getListConsumption();
		staticticalModel.setConsumptionStatisticalsMonth(sortListMonth(consumptionStatisticals));

		for (int i = 0; i < staticticalModel.getConsumptionStatisticalsMonth().size(); i++)
		{
			day = staticticalModel.getConsumptionStatisticalsMonth().get(i).getDay();
			month = staticticalModel.getConsumptionStatisticalsMonth().get(i).getMonth();
			year = staticticalModel.getConsumptionStatisticalsMonth().get(i).getYear();

			int counter = 0;
			int size = consumptionStatisticals.size();

			if (size == 1)
			{
				avg = 0;
			}
			else
			{
				if (size != 0)
				{
					for (int j = 1; j < size; j++)
					{
						if (consumptionStatisticals.get(j).getMonth() == month && consumptionStatisticals.get(j).getYear() == year)
						{
							counter++;
							if (j == size - 1 && counter > 1)
							{
								avg = consumptionStatisticals.get(j).getConsumption();
								break;
							}
							liters += consumptionStatisticals.get(j - 1).getPetrol();
							distance += consumptionStatisticals.get(j).getTemporaryDistance();
						}
						counter = 0;

					}
					if (avg == 0.0 && distance != 0.0)
					{
						avg = (liters / distance) * 100;
					}
					else
					{
						avg = 0;
					}
					distance = 0;
					liters = 0;
				}

			}

			ConsumptionValue consumptionValue = new ConsumptionValue(staticticalModel.getConsumptionStatisticalsMonth().get(i).getDate(), avg, String.valueOf(year), day, month);
			staticticalModel.getConsumptionValues().add(consumptionValue);
			avg = 0;
		}

		clearLists(staticticalModel);
	}

	public static void avgConsumptionYear(StaticticalModel staticticalModel)
	{
		double avg = 0.0;
		double liters = 0;
		double distance = 0;

		int month;
		int year;
		int day;

		List<ConsumptionStatistical> consumptionStatisticals = staticticalModel.getListConsumption();
		staticticalModel.setConsumptionStatisticalsYear(sortListYear(consumptionStatisticals, ConsumptionStatistical::getYear));

		for (int i = 0; i < staticticalModel.getConsumptionStatisticalsYear().size(); i++)
		{
			day = staticticalModel.getConsumptionStatisticalsYear().get(i).getDay();
			month = staticticalModel.getConsumptionStatisticalsYear().get(i).getMonth();
			year = staticticalModel.getConsumptionStatisticalsYear().get(i).getYear();

			int counter = 0;
			int size = consumptionStatisticals.size();

			if (size == 1)
			{
				avg = 0;
			}
			else
			{
				if (size != 0)
				{
					for (int j = 1; j < size; j++)
					{
						if (consumptionStatisticals.get(j).getYear() == year)
						{
							counter++;
							if (j == size - 1 && counter > 1)
							{
								avg = consumptionStatisticals.get(j).getConsumption();
								break;
							}
							liters += consumptionStatisticals.get(j - 1).getPetrol();
							distance += consumptionStatisticals.get(j).getTemporaryDistance();
						}
						counter = 0;

					}
					if (avg == 0.0 && distance != 0.0)
					{
						avg = (liters / distance) * 100;
					}
					else
					{
						avg = 0;
					}
					distance = 0;
					liters = 0;
				}

			}

			ConsumptionValue consumptionValue = new ConsumptionValue(staticticalModel.getConsumptionStatisticalsYear().get(i).getDate(), avg, String.valueOf(year), day, month);
			staticticalModel.getConsumptionValues().add(consumptionValue);
			avg = 0;
		}
		clearLists(staticticalModel);
	}

	public static void setBarChartStyle(StaticticalModel staticticalModel, boolean monthFlag, boolean yearFlag, float limitValue)
	{
		LimitLine limitLine = new LimitLine(limitValue, String.format("Average (%.1f l)", limitValue));
		limitLine.setLineColor(Color.RED);
		limitLine.setLineWidth(1f);

		XAxis xAxis = staticticalModel.getBarChart().getXAxis();
		xAxis.setGranularity(1f);
		xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount(staticticalModel, monthFlag, yearFlag)));
		xAxis.setDrawGridLines(false);
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setTextSize(10f);
		xAxis.setAxisLineWidth(1f);

		YAxis yAxis = staticticalModel.getBarChart().getAxisLeft();
		YAxis rightYAxis = staticticalModel.getBarChart().getAxisRight();
		rightYAxis.setEnabled(false);
		yAxis.setDrawGridLines(false);
		yAxis.setZeroLineWidth(2f);
		yAxis.setTextSize(12f);
		yAxis.setValueFormatter(new MyYAxisValueFormatter());
		yAxis.addLimitLine(limitLine);
		yAxis.setAxisLineWidth(1f);
		yAxis.setLabelCount(6, true);

	}

	private static class MyYAxisValueFormatter extends IndexAxisValueFormatter
	{
		@Override
		public String getFormattedValue(float value)
		{
			BigDecimal roundedCOnsumption = new BigDecimal(Float.toString(value));
			roundedCOnsumption = roundedCOnsumption.setScale(0, BigDecimal.ROUND_HALF_UP);

			return String.format("%.0f l/100km", roundedCOnsumption);
		}
	}

	public static ArrayList<String> getAreaCount(StaticticalModel staticticalModel, boolean monthFlag, boolean yearFlag)
	{
		int day;
		int month;
		String year;

		ArrayList<String> label = new ArrayList<>();
		for (int i = 0; i < staticticalModel.getConsumptionValues().size(); i++)
		{
			day = staticticalModel.getConsumptionValues().get(i).getDay();
			month = staticticalModel.getConsumptionValues().get(i).getMonth();
			year = staticticalModel.getConsumptionValues().get(i).getYear();

			if (monthFlag == true)
			{
				label.add(String.format("%s - %s", getMonth(month), year));
			}
			else if (yearFlag == true)
			{
				label.add(String.format("%s", year));
			}
			else
			{
				label.add(String.format("%d.%d.%s", day, month, year));
			}
		}
		return label;
	}

	// vrne ime meseca
	private static String getMonth(int month)
	{
		String monthName = new DateFormatSymbols().getMonths()[month - 1].substring(0, 3);
		return monthName;
	}

	public static void setBarData(StaticticalModel staticticalModel)
	{
		BarDataSet barDataSet = new BarDataSet(staticticalModel.getBarEntries(), staticticalModel.getDataSetLabel());
		BarData consumptionData = new BarData(barDataSet);
		staticticalModel.getBarChart().setTouchEnabled(true);

		consumptionData.setBarWidth(0.8f);
		consumptionData.setValueTextSize(10f);
		consumptionData.setValueTextColor(Color.BLACK);

		staticticalModel.getBarChart().setData(consumptionData);
		staticticalModel.getBarChart().setVisibleXRangeMinimum(4);
		staticticalModel.getBarChart().setVisibleXRangeMaximum(4);
		staticticalModel.getBarChart().moveViewToX(staticticalModel.getBarEntries().size());
		staticticalModel.getBarChart().getDescription().setEnabled(false);

		Legend legend = staticticalModel.getBarChart().getLegend();
		legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

		Description desc = new Description();
		desc.setText("Consumption average");
		desc.setTextSize(13f);

		staticticalModel.getBarChart().invalidate();
	}

}
