package com.example.gigi.poraba.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Activities.MainActivity;
import com.example.gigi.poraba.Models.User;
import com.example.gigi.poraba.R;
import com.example.gigi.poraba.Utils.SavePreferences;

import java.util.List;


public class UserAdapter extends ArrayAdapter<User> {

    Context ctx;
    int listLayoutRes;
    List<User> listOfusers;
    DatabaseHelper databaseHelper;
    TextView tv;
    boolean flgSelected;
    ImageButton btnLogin;
    SharedPreferences sharedPreferences;


    public UserAdapter(@NonNull Context ctx, int listLayoutRes, @NonNull List<User> listOfusers,DatabaseHelper databaseHelper,TextView tv,/*ImageButton btnLogin,*/SharedPreferences sharedPreferences) {
        super(ctx, listLayoutRes, listOfusers);
        this.ctx=ctx;
        this.listLayoutRes=listLayoutRes;
        this.listOfusers=listOfusers;
        this.databaseHelper=databaseHelper;
        this.tv=tv;
        //this.btnLogin=btnLogin;
        this.sharedPreferences=sharedPreferences;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(ctx);
        View view=layoutInflater.inflate(listLayoutRes,null);

        final User user=listOfusers.get(position);

        //getting views
        TextView tvUserRow=view.findViewById(R.id.tvUserRow);

        tvUserRow.setText(String.valueOf(user.getName()));
        //getting buttons
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Name",user.getName().toString());
                editor.commit();
                //String SetName=user.getName().toString();
                Intent i = new Intent(ctx, MainActivity.class);
                ctx.startActivity(i);
                Toast.makeText(getContext(),user.getName().toString(),Toast.LENGTH_SHORT).show();
                //((Activity)convertView.getContext()).finish();
                ((Activity)ctx).finish();




               // btnLogin.setEnabled(true);
            }
        });

        ImageButton ibtnEditUserRow=view.findViewById(R.id.ibtnEditUserRow);
        ImageButton ibtnDeleteUserRow=view.findViewById(R.id.ibtnDeleteUserRow);


        ibtnEditUserRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Toast.makeText(ctx,"test",Toast.LENGTH_LONG).show();
               updateuUser(user);
            }
        });
        //Delete button on user row
        ibtnDeleteUserRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
                builder.setTitle("Ste prepricani?");
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.DeleteUser(user.getName());
                        reloadUsersFromDB();
                    }
                });
                builder.setNegativeButton("Preklici", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                //Toast.makeText(ctx,"Delete"+user.getName().toString(),Toast.LENGTH_LONG).show();
                //databaseHelper.DeleteUser(user.getName());
            }
        });
        return view;
    }


    private void updateuUser(final User user)
    {
        final AlertDialog.Builder builder= new AlertDialog.Builder(ctx,R.style.DialogTheme);


        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.activity_new_user,null);
        builder.setView(view);

        final EditText etAddNewUser = view.findViewById(R.id.etAddNewUser);
        final EditText etTankCapacity = view.findViewById(R.id.etTankCapacity);

        final Spinner spinnerTypeOfFuel=view.findViewById(R.id.spinner);
        final Spinner spinnerUnit=view.findViewById(R.id.spinnerUnit);
        final Spinner spinnerConsupmtionUnit=view.findViewById(R.id.spinnerConsumptionUnit);
        final Spinner spinnerDistanceUnit=view.findViewById(R.id.spinnerDistanceUnit);


        final AlertDialog dialog=builder.create();
        dialog.show();

        etAddNewUser.setText(user.getName().toString());
        etTankCapacity.setText(String.valueOf(user.getFuelCapacity()));

        selectSpinnerValue(spinnerTypeOfFuel,user.getTypeOfFuel().toString());
        selectSpinnerValue(spinnerUnit,user.getFuelUnit().toString());
        selectSpinnerValue(spinnerConsupmtionUnit,user.getConsumptionUnit().toString());
        selectSpinnerValue(spinnerDistanceUnit,user.getDistanceUnit());



        view.findViewById(R.id.btnAddNewUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //tukaj ne pozabiti odometer!!
                String userName=etAddNewUser.getText().toString().trim();
                String tankCapacity=etTankCapacity.getText().toString().trim();

                String typeOfFuel=spinnerTypeOfFuel.getSelectedItem().toString();
                String fuelUnit=spinnerUnit.getSelectedItem().toString();
                String consuptionUnit=spinnerConsupmtionUnit.getSelectedItem().toString();
                String distanceUnit=spinnerDistanceUnit.getSelectedItem().toString();

                if(userName.isEmpty())
                {
                    etAddNewUser.setError("Polje ne sme biti prazno!");
                    etAddNewUser.requestFocus();
                    return;
                }
                if(tankCapacity.isEmpty())
                {
                    etTankCapacity.setError("Polje ne sme biti prazno");
                    etTankCapacity.requestFocus();
                    return;
                }

                databaseHelper.UpdateUser(new String[]{userName, tankCapacity, typeOfFuel,fuelUnit,consuptionUnit,distanceUnit}, String.valueOf(user.getName()));
                Toast.makeText(ctx,"Updated",Toast.LENGTH_SHORT).show();
                reloadUsersFromDB();
                dialog.dismiss();
            }
        });



    }

    private void reloadUsersFromDB(){

        Cursor result = databaseHelper.getUsersData();
        listOfusers.clear();
        while (result.moveToNext()) {
            listOfusers.add(new User(
                    result.getString(1),
                    result.getDouble(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getDouble(7)
            ));
        }
        result.close();
        notifyDataSetChanged();

    }

    private void selectSpinnerValue(Spinner spinner, String myString)
    {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }

}
