package com.dai.insreward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileDetail extends AppCompatActivity {

    private UserProfiles user;

    private RewardAdapter rewardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Rewards> rewardsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyViewHolder_Reward myViewHolderReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        setupHomeIndicator(getSupportActionBar());//set logo


        user = (UserProfiles)getIntent().getSerializableExtra("user") ;


        String a = user.getImageBytes();
        Bitmap photo = StringToBitmap(a);
        ((ImageView) findViewById(R.id.photo)).setImageBitmap(photo);

        ((TextView) findViewById(R.id.fullname)).setText(user.getFirstName() + ", " + user.getLastName());
        ((TextView) findViewById(R.id.username)).setText("(" + user.getUsername() + ")");
        ((TextView) findViewById(R.id.location)).setText(user.getLocation());
        ((TextView) findViewById(R.id.points_ed)).setText(user.setPointsAwarded().toString());
        ((TextView) findViewById(R.id.department)).setText(user.getDepartment());
        ((TextView) findViewById(R.id.position)).setText(user.getPosition());
        ((TextView) findViewById(R.id.points_to_aw2)).setText(user.getPointsToAward().toString());
        ((TextView) findViewById(R.id.story)).setText(user.getStory());

        for (int i = 0; i < user.countRewards() ; i++) {
            String date = user.getRewards().get(i).getDate();
            String fullName = user.getRewards().get(i).getFullName();
            String comment = user.getRewards().get(i).getComment();
            Integer pointToReward = user.getRewards().get(i).getPointsToRewards();
            Rewards rewards = new Rewards(date,fullName,pointToReward,comment);
            rewardsList.add(rewards);
        }

        recyclerView = findViewById(R.id.rewards);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rewardAdapter = new RewardAdapter(rewardsList, this);
        recyclerView.setAdapter(rewardAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.edit:
                Intent intent1 = new Intent(ProfileDetail.this, EditProfile.class);
                intent1.putExtra("user",user);
                startActivity(intent1);
                return true;
            case R.id.rewards:
                Intent intent2 = new Intent(ProfileDetail.this, LeaderBoard.class);
                intent2.putExtra("user",user);
                startActivity(intent2);
            default:
                return super.onOptionsItemSelected(item);
        }

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
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
