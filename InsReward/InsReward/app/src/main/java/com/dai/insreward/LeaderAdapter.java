package com.dai.insreward;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class LeaderAdapter extends RecyclerView.Adapter<MyViewHolder_Leader> {
    private List<UserProfiles> leaderList;
    private LeaderBoard leaderBoard;


    LeaderAdapter(List<UserProfiles> leaList, LeaderBoard lead){
        this.leaderList = leaList;
        this.leaderBoard = lead;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_Leader holder, int position) {
        UserProfiles leader = leaderList.get(position);

        String a = leader.getImageBytes();
        Bitmap photo = StringToBitmap(a);

        holder.fullName.setText(leader.getLastName() + ", " + leader.getFirstName());
        holder.pointsToReward.setText(leader.getPointsToAward().toString());
        holder.positionAndPosition.setText(leader.getPosition() + ", " + leader.getDepartment());
        holder.img.setImageBitmap(photo);

    }


    @NonNull
    @Override
    public MyViewHolder_Leader onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leader_list, parent, false);

        itemView.setOnClickListener(leaderBoard);
        return new MyViewHolder_Leader(itemView);
    }

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
    @Override
    public int getItemCount() {
        return leaderList.size();
    }
}
