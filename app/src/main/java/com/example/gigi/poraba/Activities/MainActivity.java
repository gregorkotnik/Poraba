package com.example.gigi.poraba.Activities;

import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Fragments.CalculateStatisticsFragment;
import com.example.gigi.poraba.Fragments.ConsumptionFragment;
import com.example.gigi.poraba.Fragments.StatisticalFragmentController;
import com.example.gigi.poraba.Models.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

	private DrawerLayout drawerLayout;

	public static final String DEFAULT = "N/A";
	public static final Float DEFAULT_FLOAT = 0.0f;

	Button btnSetConsumption, btnStatistics, btnManualCalculate, btnInfo, btnExit, btnLogout, hamMenu;
	TextView tvUserLoginName, tvOdometer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		if (checkLoginData().getName().toString() == "")
		{
			Intent cars = new Intent(getApplicationContext(), UserLogin.class);
			startActivity(cars);
			finish();
		}
		// ---------------Navigation drawer---------------------------------------------------------
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		drawerLayout = findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{
				switch (item.getItemId())
				{
					case R.id.nav_cars:
						Intent i = new Intent(getApplicationContext(), UserLogin.class);
						startActivity(i);
						finish();
						break;
					case R.id.nav_consumption:
						getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ConsumptionFragment()).commit();
						break;
					case R.id.nav_statistics:
						getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StatisticalFragmentController()).commit();
						break;
					case R.id.nav_calculate:
						getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new CalculateStatisticsFragment()).commit();
						break;

					case R.id.nav_exit:
						new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Closing Application")
								.setMessage("Are you sure you want to close this application?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										getSharedPreferences("LoginData", 0).edit().clear().commit();
										finish();
									}

								}).setNegativeButton("No", null).show();

						break;

				}
				drawerLayout.closeDrawer(GravityCompat.START);
				return true;
			}
		});
		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ConsumptionFragment()).commit();
			navigationView.setCheckedItem(R.id.nav_consumption);
		}
	}

	// Buttons in MainActivity uncomment after fragment test
	// Preverimo, ce imamo podatke v shared preferences drugače gremo na aktiviti UserLogin
	public User checkLoginData()
	{
		SharedPreferences sharedPref = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
		String name = sharedPref.getString("Name", "");
		User LoginUser = new User(name, 0.0);
		return LoginUser;

	}

	@Override
	public void onBackPressed()
	{
		if (drawerLayout.isDrawerOpen(Gravity.START))
		{
			drawerLayout.closeDrawer(Gravity.START);
			// delete shared preferences
		}
		else
		{
			super.onBackPressed();
			// this.getSharedPreferences("LoginData",0).edit().clear().commit();
		}
	}

}
