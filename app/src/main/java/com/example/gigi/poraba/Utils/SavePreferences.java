package com.example.gigi.poraba.Utils;

import com.example.gigi.poraba.Constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePreferences
{
	public static void putAverage(Context context, String key, float value)
	{
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static float getAverage(Context context, String key)
	{
		SharedPreferences preferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		float average = preferences.getFloat(key,0f);
		
		return average;
	}

}
