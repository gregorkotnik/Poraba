package com.example.gigi.poraba.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gigi.poraba.R;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class manualCalculate extends AppCompatActivity {

    EditText etDistance,etFuel,etPrice;
    TextView tvConsumation,tvFinalPrice;
    Button btnCalculate;
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_calculate);
        etDistance=(EditText)findViewById(R.id.etDistance);
        etFuel=(EditText)findViewById(R.id.etFuel);
        etPrice=(EditText)findViewById(R.id.etPrice);
        tvConsumation=(TextView)findViewById(R.id.tvConsumation);
        tvFinalPrice=(TextView)findViewById(R.id.tvFinalPrice);
        btnCalculate=(Button)findViewById(R.id.btnCalculate);

    }

    public void Calculate(View v)
    {

        if(TextUtils.isEmpty(etDistance.getText()))
        {
            etDistance.setError("Polje ne sme biti prazno");

            return;
        }
        else if(CheckDouble(etDistance.getText().toString())==false)
        {
            etDistance.setError("Vnesite število primer:700.3");
            return;
        }
        if(TextUtils.isEmpty(etFuel.getText()))
        {
            etFuel.setError("Polje ne sme biti prazno");
            return;
        }
        else if(CheckDouble(etFuel.getText().toString())==false)
        {
            etFuel.setError("Vnesite število pimer:50.5");
            return;
        }
        if(TextUtils.isEmpty(etPrice.getText()))
        {
            etPrice.setError("Polje ne sme biti prazno");
            return;
        }
        else if(CheckDouble(etPrice.getText().toString())==false)
        {
            etPrice.setError("Vnesite število primer 1.25");
            return;
        }

        double Distance=Double.parseDouble(etDistance.getText().toString());
        double Fuel=Double.parseDouble(etFuel.getText().toString());
        double Price=Double.parseDouble(etPrice.getText().toString());

        double finalConsumation=Fuel/Distance*100;
        double finalPrice=Fuel*Price;
        DecimalFormat df = new DecimalFormat("#.##");
        finalConsumation = Double.valueOf(df.format(finalConsumation));
        finalPrice=Double.valueOf(df.format(finalPrice));

        tvConsumation.setText("Vaša poraba je: "+Double.toString(finalConsumation)+ " l/km");
        tvFinalPrice.setText("Skupna cena natočena goriva je: " +Double.toString(finalPrice)+ " €");

    }

    public static boolean CheckDouble(String number)
    {
        return DOUBLE_PATTERN.matcher(number).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(manualCalculate.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
