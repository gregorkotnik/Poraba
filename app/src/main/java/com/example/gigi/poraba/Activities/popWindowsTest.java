package com.example.gigi.poraba.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.R;

public class popWindowsTest extends Activity
{

    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindows);

        Button btnLogin=(Button)findViewById(R.id.btnPopUpLogin);

        databaseHelper=new DatabaseHelper(this);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width= dm.widthPixels;
        int hegth= dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(hegth*.5));


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Cursor date=databaseHelper.getUsersData();
                startActivity(new Intent(popWindowsTest.this, UserLogin.class));

                finish();
            }
        });

    }
}
