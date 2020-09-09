package com.dai.insreward;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder_Leader extends RecyclerView.ViewHolder {
    public ImageView img;
    public TextView fullName;
    public TextView positionAndPosition;
    public TextView pointsToReward;

    public MyViewHolder_Leader(@NonNull View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.imageView);
        fullName = itemView.findViewById(R.id.fullname);
        positionAndPosition = itemView.findViewById(R.id.pos_dep);
        pointsToReward = itemView.findViewById(R.id.points_to_rew);
    }
}
