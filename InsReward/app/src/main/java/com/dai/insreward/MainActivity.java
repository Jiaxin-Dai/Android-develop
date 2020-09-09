package com.dai.insreward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private UserProfiles user;
    private String uName;
    private String pswd;
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor prefsEditor;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupHomeIndicator(getSupportActionBar()); //set logo

        myPrefs = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        prefsEditor = myPrefs.edit();

        String myData = myPrefs.getString("DATA", null);
        String myData2 = myPrefs.getString("DATA2", null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main, menu);
        return true;
    }
    public void createProfile(View v) {
        Intent intent = new Intent(MainActivity.this, CreatePro.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        startActivity(intent);
    }
    //-------------set logo-----------------
    static void setupHomeIndicator(ActionBar actionBar) {

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.icon);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    //-----------------------------------

    public void login(View v) {
        checkbox = findViewById(R.id.checkBox);
        if (!checkbox.isChecked()) {


            uName = ((EditText) findViewById(R.id.name)).getText().toString();
            pswd = ((EditText) findViewById(R.id.password)).getText().toString();


            new LoginAPIAyncTask(this).execute(uName, pswd);
        } else {

            uName = ((EditText) findViewById(R.id.name)).getText().toString();
            pswd = ((EditText) findViewById(R.id.password)).getText().toString();
            prefsEditor.putString("DATA", uName);
            prefsEditor.putString("DATA2", pswd);
            prefsEditor.apply();

            new LoginAPIAyncTask(this).execute(uName, pswd);
        }
    }

    public void getLogin(UserProfiles userProfiles){
        user = userProfiles;
        Intent intent = new Intent(MainActivity.this, ProfileDetail.class);

        intent.putExtra("user",user);
        startActivity(intent);

    }

}
