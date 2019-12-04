package com.example.gigi.poraba.Models;

import android.os.AsyncTask;

public class ConsumationToList extends AsyncTask<TaskParam, Void, Void>
{

	StaticticalModel staticticalModel;

	public ConsumationToList(StaticticalModel viewModel)
	{
		this.staticticalModel = viewModel;
	};

	@Override
	protected Void doInBackground(TaskParam... taskParams)
	{
	    return null;
	}

	public StaticticalModel getStaticticalModel()
	{
		return staticticalModel;
	}

	public void setStaticticalModel(StaticticalModel staticticalModel)
	{
		this.staticticalModel = staticticalModel;
	}

}
