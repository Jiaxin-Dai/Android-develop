package com.dai.insreward;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoard extends AppCompatActivity implements View.OnClickListener{
    private UserProfiles user;


    private LeaderAdapter leaderAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UserProfiles> userProfilesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyViewHolder_Leader myViewHolderLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        user = (UserProfiles)getIntent().getSerializableExtra("user");

        new GetAllProfilesAPIAyncTask(this).execute(user.getUsername(),user.getPassword());

        //userProfilesList.add(user);

        recyclerView = findViewById(R.id.rewards);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        leaderAdapter = new LeaderAdapter(userProfilesList, this);
        recyclerView.setAdapter(leaderAdapter);



    }

    public void getAllUser(List<UserProfiles> userList){
        userProfilesList.addAll(userList);
        //Collections.sort(userProfilesList);
        leaderAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_leader, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        final int pos = recyclerView.getChildLayoutPosition(view);
//        String uNameSource = user.getUsername();
//        String pswdSource = user.getPassword();
        String uNameTarget = userProfilesList.get(pos).getUsername();
        String lastName = userProfilesList.get(pos).getLastName();
        String firstName = userProfilesList.get(pos).getFirstName();
        Integer pointAwarded = userProfilesList.get(pos).setPointsAwarded();
        String department = userProfilesList.get(pos).getDepartment();
        String position = userProfilesList.get(pos).getPosition();
        String story = userProfilesList.get(pos).getStory();
        String img = userProfilesList.get(pos).getImageBytes();
        UserProfiles target = new UserProfiles(uNameTarget,firstName,lastName,department,position,story,pointAwarded,img);

        Intent i = new Intent(LeaderBoard.this, Award.class);
        i.putExtra("target",target);
        i.putExtra("user",user);
        startActivity(i);



    }



}
