package com.dai.insreward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Award extends AppCompatActivity {
    private UserProfiles target;
    private UserProfiles source;

//    private String admin;
    private String username;
//    private String firstname;
//    private String lastname;
//    private String location;
//    private String department;
//    private String img;
//    private String oldpsw;
    private String psw;
//    private String pta;
//    private String story;
//    private String position;
//    private List<Rewards> rewardsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        setupHomeIndicator(getSupportActionBar());//set logo


        target = (UserProfiles)getIntent().getSerializableExtra("target");

        username = getIntent().getStringExtra("username");

        psw = getIntent().getStringExtra("password");


        String a = target.getImageBytes();
        Bitmap photo = StringToBitmap(a);
        ((ImageView) findViewById(R.id.photo)).setImageBitmap(photo);

        ((TextView) findViewById(R.id.fullname)).setText(target.getFirstName() + ", " + target.getLastName());
        ((TextView) findViewById(R.id.points_ed)).setText(target.setPointsAwarded().toString());
        ((TextView) findViewById(R.id.department)).setText(target.getDepartment());
        ((TextView) findViewById(R.id.position)).setText(target.getPosition());
        ((TextView) findViewById(R.id.story)).setText(target.getStory());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_award, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.saveAward){


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");// HH:mm:ss

            Date d = new Date(System.currentTimeMillis());
            String date = simpleDateFormat.format(d);



//            String uNameSource = source.getUsername();
//            String pswdSource = source.getPassword();
            String uNameTarget = target.getUsername();
            String fullNameTarget = ((TextView)findViewById(R.id.fullname)).getText().toString();

            String notes = ((EditText)findViewById(R.id.comment)).getText().toString();
            String value = ((EditText)findViewById(R.id.points_to_rew)).getText().toString();


            new RewardsAPIAsyncTask(this).execute(username,psw,uNameTarget,fullNameTarget,date,notes,value);
            OpenLeader();

            return true;
        }
        else
            return super.onOptionsItemSelected(item);

    }
    public void OpenLeader() {
        Intent intent = new Intent(Award.this, LeaderBoard.class);
//        intent.putExtra("user",source);
        intent.putExtra("username",username);
        intent.putExtra("password",psw);
        startActivity(intent);
    }

    //---------------set logo-------------------
    static void setupHomeIndicator(ActionBar actionBar) {

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.icon);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    //-------------------------------------------

    public Bitmap StringToBitmap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
