package com.example.gigi.poraba.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gigi.poraba.Adapters.UserAdapter;
import com.example.gigi.poraba.DB.DatabaseHelper;
import com.example.gigi.poraba.Models.User;
import com.example.gigi.poraba.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserLogin extends AppCompatActivity {


    EditText etName,etOdometer;
    TextView tvNameUserLogin;
    ImageButton btnNewUser,btnLove;
    DatabaseHelper consimptionDb;
    User loginUser;
    DatabaseHelper dbusers;


    ArrayList<String> listOfUsers;
    ArrayList<String> listOfusersOdometer;

    List<User> ListOfUsers;
    ListView listViewUsers;
    ListAdapter listAdapter;
    UserAdapter userAdapter;

    SharedPreferences sharePref;
    public ProgressBar spinner;
    //ArrayList<User> listofUsers;


    //Zastavica za novega uporabnika
    boolean flagNewUser=false;
    public boolean flgSelected;

    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_login);
        dbusers=new DatabaseHelper(this);

        ListOfUsers=new ArrayList<>();
        sharePref=getSharedPreferences("LoginData", Context.MODE_PRIVATE);

        btnNewUser=(ImageButton)findViewById(R.id.btnNewUser);
        spinner=(ProgressBar) findViewById(R.id.progressBarUsers);
        consimptionDb=new DatabaseHelper(this);


        //Polnjenje listvieva z userji

        listViewUsers=(ListView) findViewById(R.id.lvUsers);

        listOfUsers= new ArrayList<>();
        listOfusersOdometer=new ArrayList<>();
        final ArrayList<User> listuserrr=new ArrayList<>();
        spinner.setVisibility(View.GONE);

//        final Cursor data=dbusers.getUsersData();
//
//
//        if(data.getCount()==0)
//        {
//            listViewUsers.setFilterText("Ni podatkov");
//            Toast.makeText(UserLogin.this,"V bazi ni uporabnikov",Toast.LENGTH_LONG).show();
//        }
//
//        else
//        {
            //ShowUserFromDB();
            showUsersFromDB_Async();

//        }


//        btnLove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AlertDialog.Builder builder= new AlertDialog.Builder(UserLogin.this,R.style.DialogTheme);
//
//
//                LayoutInflater inflater=LayoutInflater.from(getBaseContext());
//                view=inflater.inflate(R.layout.love,null);
//                builder.setView(view);
//
//                final AlertDialog dialog=builder.create();
//                dialog.show();
//            }
//        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        showUsersFromDB_Async();
        //Toast.makeText(this,"DELA",Toast.LENGTH_LONG).show();
    }


    private void ShowUserFromDB() {
        Cursor result = dbusers.getUsersData();
        ListOfUsers.clear();
        while (result.moveToNext()) {
            ListOfUsers.add(new User(
                    result.getString(1),
                    result.getDouble(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6),
                    result.getDouble(7)

                    //TUKAJ DODAJ OSTALE KOLONE, DA BODO DELALI SPINNERJI V ARRAYADDAPTERJU
            ));
        }

        result.close();

        //listAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listOfUsers);
        //userAdapter.setAdapter(ListOfUsers);

        //creating the adapter object
        flgSelected=false;
        userAdapter=new UserAdapter(this,R.layout.display_user_row,ListOfUsers,dbusers,tvNameUserLogin,sharePref);
        listViewUsers.setAdapter(userAdapter);
        //adding adapter to listView
        //listViewConsumptions.setAdapter(fuelConsumptionAdapterNew);

    }

    private void showUsersFromDB_Async() {
        LoadUsersFromDB loadUsersFromDB = new LoadUsersFromDB();
        loadUsersFromDB.execute();

    }

    private class LoadUsersFromDB extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor result = dbusers.getUsersData();
            ListOfUsers.clear();
            while (result.moveToNext()) {
                ListOfUsers.add(new User(
                        result.getString(1),
                        result.getDouble(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6),
                        result.getDouble(7)

                        //TUKAJ DODAJ OSTALE KOLONE, DA BODO DELALI SPINNERJI V ARRAYADDAPTERJU
                ));
            }

            result.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            spinner.setVisibility(View.GONE);
            ShowUsers();


        }
    }

    private void ShowUsers()
    {
        flgSelected=false;
        if(ListOfUsers.size()==0)
        {
            listViewUsers.setFilterText("Ni podatkov");
            Toast.makeText(UserLogin.this,"V bazi ni uporabnikov",Toast.LENGTH_LONG).show();
        }
        userAdapter=new UserAdapter(this,R.layout.display_user_row,ListOfUsers,dbusers,tvNameUserLogin,sharePref);
        listViewUsers.setAdapter(userAdapter);
    }
    public void DeleteUSer(View v)
    {
        //Izrišemo iz baze in liste userjev
            if(tvNameUserLogin.getText()!=null) {

                String userName=tvNameUserLogin.getText().toString();
            }
    }


    public void AddNewUser(View v)
    {
        Intent intent = new Intent(this, NewUser.class);
        intent.putExtra("listOfUsers",listOfUsers);
        startActivity(intent);

    }

    public static boolean CheckDouble(String number)
    {
        return DOUBLE_PATTERN.matcher(number).matches();
    }




}
