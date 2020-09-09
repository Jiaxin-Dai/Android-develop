package com.dai.notepad;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Note> notelists;
    private MainActivity mainAct;
    private static Note note;
    onItemClick onitemClick;

    public NoteListAdapter(List<Note> notList, MainActivity mainActivity) {
        this.notelists = notList;
        mainAct = mainActivity;
    }

    public void setOnitemClick(onItemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        note = notelists.get(position);
        if (note != null) {
            holder.title.setText(note.getTitle());
            holder.time.setText(note.getTime());
            holder.overView.setText(note.getText());
        }

        if (onitemClick != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.setSelectedNum(position);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notelists.size();
    }

    public void RemoveData(int position){
        notelists.remove(position);
        //通知适配器item内容删除
        notifyItemRemoved(position);
    }


}

interface onItemClick {
    public void setSelectedNum(int position);
}
