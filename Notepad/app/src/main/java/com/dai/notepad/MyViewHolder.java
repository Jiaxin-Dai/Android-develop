package com.dai.notepad;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView time;
    public TextView overView;
    public LinearLayout linearLayout;

    public MyViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.title);
        time = view.findViewById(R.id.time);
        overView = view.findViewById(R.id.text);
        linearLayout = view.findViewById(R.id.list_row);
    }
}
