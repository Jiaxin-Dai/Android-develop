package com.dai.insreward;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<MyViewHolder_Reward> {
    private List<Rewards> rewardsList;
    private ProfileDetail profileDetail;

    public RewardAdapter(List<Rewards> rewardsList, ProfileDetail profileDetail) {
        this.rewardsList = rewardsList;
        this.profileDetail = profileDetail;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_Reward holder, int position) {
       Rewards rewards = rewardsList.get(position);

       holder.date.setText(rewards.getDate());
       holder.fullName.setText(rewards.getFullName());
       holder.pointsToReward.setText(rewards.getPointsToRewards().toString());
       holder.comment.setText(rewards.getComment());
    }

    @NonNull
    @Override
    public MyViewHolder_Reward onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_his_list, parent, false);

        return new MyViewHolder_Reward(itemView);
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }
}
