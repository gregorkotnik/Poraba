package com.example.gigi.poraba.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonParserUtils
{
	private static Gson gson;

	public static Gson getGsonParser()
	{
		if (gson == null)
		{
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
		}
		return gson;
	}
}
