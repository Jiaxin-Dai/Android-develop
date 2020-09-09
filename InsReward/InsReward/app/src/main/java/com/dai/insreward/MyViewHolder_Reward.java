package com.dai.insreward;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_Reward extends RecyclerView.ViewHolder {
    TextView date;
    TextView fullName;
    TextView pointsToReward;
    TextView comment;

    public MyViewHolder_Reward(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        fullName = itemView.findViewById(R.id.fullname);
        pointsToReward = itemView.findViewById(R.id.points_to_rew);
        comment = itemView.findViewById(R.id.comment);

    }
}
