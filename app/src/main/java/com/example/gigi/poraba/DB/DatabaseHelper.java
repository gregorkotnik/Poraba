package com.example.gigi.poraba.DB;

import com.example.gigi.poraba.Models.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GIGI on 17/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "consumptionGigi1.db";

	// vrstice za porabo
	public static final String TABLE_NAME = "consumation_table";
	public static final String COL_1 = "ID";
	public static final String COL_2 = "PETROL";
	public static final String COL_3 = "DISTANCE";
	public static final String COL_4 = "PRICE";
	public static final String COL_5 = "DATE";
	public static final String COL_6 = "DISTANCE_TMP";
	public static final String COL_7 = "CONSUMPTION";
	public static final String COL_8 = "TOTAL_PRICE";
	public static final String COL_9 = "USER_NAME_FK";
	public static final String COL_10 = "USER_ID_FK";

	// public static final String COL_9="USER_NAME_FK";

	// vrstice za Uporabnika
	public static final String TABLE_USER = "User_table";
	public static final String USER_ID = "USER_ID";
	public static final String USER_NAME = "USER_NAME";
	public static final String ODOMETER = "ODOMETER";
	public static final String TYPE_OF_FUEL = "TYPE_OF_FUEL";
	public static final String FUEL_UNIT_ = "FUEL_UNIT";
	public static final String CONSUMPTION_UNIT = "CONSUMPTION_UNIT";
	public static final String DISTANCE_UNIT = "DISTANCE_UNIT";
	public static final String FUEL_CAPACITY = "FUEL_CAPACITY";

	// public static final String COL_4="DATE";

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, 1);
		// samo za prevejanje
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table " + TABLE_USER + " (USER_ID INTEGER PRIMARY KEY AUTOINCREMENT" + ",USER_NAME TEXT,ODOMETER DOUBLE,TYPE_OF_FUEL TEXT,FUEL_UNIT TEXT,CONSUMPTION_UNIT TEXT,"
				+ "DISTANCE_UNIT TEXT, FUEL_CAPACITY DOUBLE)");
		db.execSQL("create table " + TABLE_NAME
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PETROL DOUBLE ,DISTANCE DOUBLE ,PRICE DOUBLE,DATE TEXT,DISTANCE_TMP DOUBLE ,CONSUMPTION DOUBLE,TOTAL_PRICE DOUBLE, USER_ID_FK INTEGER,USER_NAME_FK TEXT,FOREIGN KEY(USER_ID_FK) REFERENCES TABLE_USER(USER_ID))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	// vstavimo uporabnika
	public boolean insertUserData(User user)
	{
		String userName = user.getName();
		double odometer = user.getOdometer();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_NAME, userName);
		contentValues.put(ODOMETER, odometer);

		long result = db.insert(TABLE_USER, null, contentValues);
		return result != -1;

	}

	// vstavimo uporabnika
	public boolean insertUserDataNew(User user)
	{
		String userName = user.getName();
		double odometer = 0.0;
		String TypeOfFuel = user.getTypeOfFuel();
		String FuelUnit = user.getFuelUnit();
		String ConsumptionUnit = user.getConsumptionUnit();
		String DistanceUnit = user.getDistanceUnit();
		double FuelCapacity = user.getFuelCapacity();

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(USER_NAME, userName);
		contentValues.put(ODOMETER, odometer);
		contentValues.put(TYPE_OF_FUEL, TypeOfFuel);
		contentValues.put(FUEL_UNIT_, FuelUnit);
		contentValues.put(CONSUMPTION_UNIT, ConsumptionUnit);
		contentValues.put(DISTANCE_UNIT, DistanceUnit);
		contentValues.put(FUEL_CAPACITY, FuelCapacity);

		long result = db.insert(TABLE_USER, null, contentValues);
		return result != -1;

	}

	// getUserID metoda, ki bo glede na ime vrnala id, pozneje bom ta id skupaj shranil v tuji kljuc v tabela poraba
	public boolean insertData(double petrol, double distance, double price, String date, double distance_tmp, double consumption, double totalPrice, String userName, int userID)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_2, petrol);
		contentValues.put(COL_3, distance);
		contentValues.put(COL_4, price);
		contentValues.put(COL_5, date);
		contentValues.put(COL_6, distance_tmp);
		contentValues.put(COL_7, consumption);
		contentValues.put(COL_8, totalPrice);
		contentValues.put(COL_9, userName);
		contentValues.put(COL_10, userID);

		long result = db.insert(TABLE_NAME, null, contentValues);
		return result != -1;

	}

	public Cursor getFuelConsumptionData(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USER_NAME_FK = '" + name.trim() + "'" + "GROUP BY DISTANCE", null);
		return result;
	}

	// ------------------------DAY-----------------------------
	public Cursor getFuelConsumptionDataAvgPerDay(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db
				.rawQuery("SELECT strftime('%m', Date), strftime('%Y', Date), strftime('%d',Date) FROM " + TABLE_NAME + " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DISTANCE ASC", null);
		return result;
	}

	// get avg per day
	public Cursor getFuelConsumptionDataAvgPer_Day(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery(
				"SELECT DISTINCT strftime('%m', Date),strftime('%Y', Date),strftime('%d', Date) FROM " + TABLE_NAME + " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DATE ASC", null);
		return result;
	}

	// ---------------------MONTH-------------------------------
	// get average per month
	public Cursor getFuelConsumptionAvgData(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor result = db.rawQuery("SELECT  strftime('%m', Date),strftime('%Y', Date), strftime('%d', Date), CONSUMPTION,PETROL,DISTANCE_TMP,DISTANCE,DATE  FROM " + TABLE_NAME
				+ " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DISTANCE ASC", null);

		return result;
	}

	public Cursor getFuelConsumptionDataAvg_Month(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT DISTINCT strftime('%m', Date),strftime('%Y', Date) FROM " + TABLE_NAME + " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DATE ASC", null);

		return result;
	}

	// ---------------------YEAR-------------------------------
	// get average per year
	public Cursor getFuelConsumptionDataAvgPerYear(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT strftime('%m', Date), strftime('%Y', Date), strftime('%d', Date), CONSUMPTION,PETROL,DISTANCE_TMP,DISTANCE,DATE  FROM " + TABLE_NAME
				+ " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DISTANCE ASC", null);
		return result;
	}

	public Cursor getFuelConsumptionDataAvg_Year(String name)// String name/*tuji ključ id uporabnika v where pogoju*/)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT DISTINCT strftime('%Y', Date) FROM " + TABLE_NAME + " WHERE USER_NAME_FK = '" + name.trim() + "'" + " ORDER BY DATE ASC", null);

		return result;
	}

	public boolean UpdateConsumption(String[] consumption, String id)
	{

		// tukaj dodaj reusult Curosor in preverjaj rezultat tudi v delete metodi

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		final String Consumption = consumption[0];
		final String Milage = consumption[1];
		final String Petrol = consumption[2];
		final String NewOdometer = consumption[3];
		final String PriceFinalUpdate = consumption[4];

		contentValues.put("CONSUMPTION", Consumption);
		contentValues.put("DISTANCE_TMP", Milage);
		contentValues.put("PETROL", Petrol);
		contentValues.put("DISTANCE", NewOdometer);
		contentValues.put("TOTAL_PRICE", PriceFinalUpdate);

		long result = db.update("consumation_table", contentValues, "ID =" + Integer.parseInt(id), null);

		return result != -1;
	}

	// Delete consumption

	public void DeleteConsumption(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		// delete from db
		db.delete("consumation_table", "ID =" + id, null);
	}

	public void UpdateUser(String[] user, String userName)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		final String user_name = user[0];
		final String tankCapacity = user[1];
		final String typeOfFuel = user[2];
		final String fuelUnit = user[3];
		final String consuptionUnit = user[4];
		final String distanceUnit = user[5];

		contentValues.put("USER_NAME", user_name);
		contentValues.put("FUEL_CAPACITY", tankCapacity);
		contentValues.put("TYPE_OF_FUEL", typeOfFuel);
		contentValues.put("FUEL_UNIT", fuelUnit);
		contentValues.put("CONSUMPTION_UNIT", consuptionUnit);
		contentValues.put("DISTANCE_UNIT", distanceUnit);

		database.update("User_table", contentValues, "USER_NAME ='" + userName.trim() + "'", null);
	}

	public void DeleteUser(String uName)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("User_table", "USER_NAME = '" + uName.trim() + "'", null);
		db.delete("consumation_table", "USER_NAME_FK = '" + uName.trim() + "'", null);

	}

	// metoda, ki vrne uporabnik id glede na ime v sharedpreferences
	public Cursor getUserId(String name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE USER_NAME = '" + name.trim() + "'", null); // 13.11 tukaj
		// String a = result.getString(result.getColumnIndex("USER_ID")); //tukaj je težava 13.11. 2018
		return result;
	}

	public Cursor getUsersData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
		return result;
	}

	public Cursor getOdometer(String user_name)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		// SELECT * FROM consumation_table ORDER BY Id DESC LIMIT 1
		Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_name_fk = '" + user_name.trim() + "'" + " ORDER BY DISTANCE" + " DESC LIMIT 1 ", null);
		return result;
	}

	public void UpdateConsumptionAfterDelete(String tmpDistance, String consumption, String id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		final String Milage = tmpDistance;
		final String Consumption = consumption;
		contentValues.put("DISTANCE_TMP", Milage);
		contentValues.put("CONSUMPTION", Consumption);
		db.update("consumation_table", contentValues, "ID =" + Integer.parseInt(id), null);
	}

	// public void UpdatePriceAfterEdit()
	public void UpdateOdometer(String Distance, String Distance_tmp, String Consumption, String id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		final String Milage = Distance;
		contentValues.put("DISTANCE", Milage);
		contentValues.put("DISTANCE_TMP", Distance_tmp);
		contentValues.put("CONSUMPTION", Consumption);
		db.update("consumation_table", contentValues, "ID =" + Integer.parseInt(id), null);
	}

	// metoda, ki vrne uporabnik id glede na ime v sharedpreferences
	public Cursor getStatisticalData(String name)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor result = db.rawQuery("Select sum(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month','-1 month')) then TOTAL_PRICE else null end) as test"
				+ ",MAX(DISTANCE)||' km' as stanje_stevca" + ",SUM(DISTANCE_TMP) ||' km' as Skupna_razdalja" + ",SUM(PETROL) || ' l' as Skupno_gorivo" + ",SUM(TOTAL_PRICE) || ' €' as Skupni_strosek"
				+ ",COUNT(DATE) as Skupno_St_Polnjenj" + ",count(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year')) then DATE else 0 end) as Leto_Do_Datuma"
				+ ",count(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year','-12 month')) then DATE else 0 end) as Prejsnje_leto"
				+ ",count(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month')) then DATE else 0 end) as Ta_Mesec"
				+ ",count(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month','-1 month')) then DATE else 0 end) as Prejsnji_mesec" + ",MAX(PETROL) || ' l' as MAX_Polnjenje"
				+ ",MIN(PETROL) || ' l' as MIN_Polnjenje" + ",SUM(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year')) then PETROL else 0 end) ||' l' as Leto_Do_Datuma_Gorivo"
				+ ",SUM(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year','-12 month')) then PETROL else 0 end) || ' l' as Prejsnje_leto_Gorivo"
				+ ",SUM(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month')) then PETROL else 0  end) || ' l' as Ta_Mesec_Gorivo"
				+ ",SUM(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month','-1 month')) then PETROL else 0  end) || ' l' as Prejsnji_Mesec_Gorivo"
				+ ",SUM(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year')) then TOTAL_PRICE else 0 end) || ' €' as Leto_Do_Datuma_Stroski"
				+ ",SUM(case when strftime('%Y',DATE) =strftime('%Y',date('now','start of year','-12 month')) then TOTAL_PRICE  else 0 end) || ' €' as Prejsnje_leto_Stroski"
				+ ",SUM(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month')) then TOTAL_PRICE  else 0 end) || ' €' as Ta_Mesec_Stroski"
				+ ",SUM(case when strftime('%Y%m',DATE) =strftime('%Y%m',date('now','start of month','-1 month')) then TOTAL_PRICE  else 0 end) || ' €' as Prejsnji_Mesec_Stroski"
				+ ",MAX(TOTAL_PRICE ||' €') as Najvisji_racun" + ",MIN(TOTAL_PRICE ||' €')  as Najnizji_racun" + ",MIN(PRICE || ' €/liter') as Najboljsa_cena"
				+ ",MAX(PRICE || ' €/liter') as Najslabsa_cena" + ",round(MIN((TOTAL_PRICE/DISTANCE_TMP)),2) || ' €/km'  as Najboljsa_cena_na_kilometer"
				+ ",round(MAX((TOTAL_PRICE/DISTANCE_TMP)),2) || ' €/km' as Najslabsa_cena_na_kilometer" + ",AVG(PETROL) || ' l' as Povprecno_polnjenje"
				+ ",AVG(TOTAL_PRICE) || ' €' as Povprecnen_racun_polnjenja" + ",round(AVG((TOTAL_PRICE/DISTANCE_TMP)),2) || ' €/km' as Povprecna_cena_na_kilometer"
				+ ",round(SUM(TOTAL_PRICE)/(julianday('now')-julianday(MIN(DATE))),2) || ' €' as Povprecni_strosek_na_dan"
				+ ",round(SUM(TOTAL_PRICE)/((julianday('now')-julianday(MIN(DATE)))/30),2) || ' €' as Povprecni_strosek_na_mesec"
				+ ",round(SUM(DISTANCE_TMP)/(julianday('now')-julianday(MIN(DATE))),2) || ' km' as Povprecni_kilometri_na_dan"
				+ ",round(SUM(DISTANCE_TMP)/((julianday('now')-julianday(MIN(DATE)))/30),2) || ' km' as Povprecni_kilometri_na_mesec"
				+ ",((Sum(PETROL) - ( SELECT PETROL from consumation_table order by DISTANCE DESC limit 1 ))/SUM(DISTANCE_TMP)*100) || ' l/100km' as Povrpecna_poraba_goriva"
				+ ",(Select MIN(consumption) from consumation_table where consumption!=0)|| ' l/100km' as Najmanjsa_poraba_goriva" + ",MAX(consumption) || ' l/100km' as Najvecja_poraba_goriva"
				+ " from " + TABLE_NAME + " WHERE user_name_fk = '" + name.trim() + "'", null);

		return result;
	}

}
