package com.example.gigi.poraba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class FuelConsumptionAdapterNew extends ArrayAdapter<FuelConsumption> {

    Context ctx;
    int listLayoutRes;
    List<FuelConsumption> consumptionList;
    DatabaseHelper databaseHelper;



    public FuelConsumptionAdapterNew(@NonNull Context ctx, int listLayoutRes, List<FuelConsumption> consumptionList,DatabaseHelper databaseHelper) {
        super(ctx,listLayoutRes,consumptionList);
        this.databaseHelper=databaseHelper;
        this.ctx=ctx;
        this.listLayoutRes=listLayoutRes;
        this.consumptionList=consumptionList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view= inflater.inflate(listLayoutRes,null);

        //getting consumption at specified position
        final FuelConsumption fuelConsumption=consumptionList.get(position);

        //getting views
        TextView textViewConsumption=view.findViewById(R.id.tvConsumptionL);
        TextView textViewFuelLoaded=view.findViewById(R.id.tvFuelLoaded);
        TextView textViewMilage=view.findViewById(R.id.tvMilage);

        //adding data to views

        textViewConsumption.setText(String.valueOf(fuelConsumption.getConsumption()));
        textViewFuelLoaded.setText(String.valueOf(fuelConsumption.getFuel()));
        textViewMilage.setText(String.valueOf(fuelConsumption.getMileage()));

        //buttons for update and delete

        Button buttonEdit=view.findViewById(R.id.btnEditConsumption);
        Button buttonDelete=view.findViewById(R.id.btnDeleteConsumption);


        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConsumption(fuelConsumption);

            }
        });

        //delete consumption

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
                databaseHelper=new DatabaseHelper(ctx);
                builder.setTitle("Ste prepričani,da želite izbrisati zapis");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.DeleteConsumption(fuelConsumption.getId());
                        reloadConsumption(String.valueOf(fuelConsumption.getUserName()));
                    }
                });
                builder.setNegativeButton("Preklici", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        return view;

    }

    private void updateConsumption(final FuelConsumption fuelConsumption)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        databaseHelper=new DatabaseHelper(ctx);
        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.update_consumption_dialog,null);
        builder.setView(view);

        final EditText editTextConsumption=view.findViewById(R.id.etConsuptionL);
        final EditText editTextMilage=view.findViewById(R.id.etMilageUpdate);
        final EditText editTextPetrol=view.findViewById(R.id.etPetrolL);

        editTextConsumption.setText(String.valueOf(fuelConsumption.getConsumption()));
        editTextMilage.setText(String.valueOf(fuelConsumption.getMileage()));
        editTextPetrol.setText(String.valueOf(fuelConsumption.getFuel()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnUpdateConsumption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ConsumptionL=editTextConsumption.getText().toString().trim();
                String Milage=editTextMilage.getText().toString().trim();
                String PetrolL=editTextPetrol.getText().toString().trim();

                if(ConsumptionL.isEmpty())
                {
                    editTextConsumption.setError("Polje ne sme biti prazno");
                    editTextConsumption.requestFocus();
                    return;
                }
                if(Milage.isEmpty())
                {
                    editTextMilage.setError("Polje ne sme biti prazno");
                    editTextMilage.requestFocus();
                    return;
                }
                if(PetrolL.isEmpty()) {
                    editTextPetrol.setError("Polje ne sme biti prazno");
                    editTextPetrol.requestFocus();
                    return;
                }

                databaseHelper.UpdateConsumption(new String[]{ConsumptionL, Milage, PetrolL}, String.valueOf(fuelConsumption.getId()));


                Toast.makeText(ctx,"Consumption Updated",Toast.LENGTH_SHORT).show();
                //fuelConsumption.getid();
                reloadConsumption(String.valueOf(fuelConsumption.getUserName()));

                dialog.dismiss();

            }
        });

    }

    private void reloadConsumption(String con)
    {

        Cursor cursor=databaseHelper.getFuelConsumptionData(con);
        if(cursor.moveToFirst()) {
            consumptionList.clear();

            do{
                consumptionList.add(new FuelConsumption(
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6),
                        cursor.getInt(7),
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //consumptionList.clear();
        notifyDataSetChanged();
    }
}
