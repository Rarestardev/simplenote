package com.rarestar.easynote.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rarestar.easynote.R;
import com.rarestar.easynote.views.MainActivity;

import java.util.List;

public class Detail_Adapter extends RecyclerView.Adapter <Detail_Adapter.ViewHolder> {
    Context context;
    List<Note> noteList;
    View view;
    int priorityIconDrawable;
    public Detail_Adapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public Detail_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.priority_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Detail_Adapter.ViewHolder holder, int position) {
        holder.textView_subject.setText(noteList.get(position).getSubject());
        holder.textView_date.setText(noteList.get(position).getDate());


        if (MainActivity.infoActivityForIntent.equals("Priority")){
            String mode = noteList.get(position).getPriority();
            switch (mode){
                case "High":
                    priorityIconDrawable = R.drawable.high_priority;
                    break;
                case "Medium":
                    priorityIconDrawable = R.drawable.medium_priority;
                    break;
                case "Low":
                    priorityIconDrawable = R.drawable.low_priority;
                    break;
                case "No":
                    priorityIconDrawable = R.drawable.ic_flag;
                    break;
            }
            holder.flag.setImageResource(priorityIconDrawable);
        }else if (MainActivity.infoActivityForIntent.equals("Subjects")){
            holder.flag.setVisibility(View.GONE);
        }else if (MainActivity.infoActivityForIntent.equals("Tags")){

        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_subject,textView_date;
        ImageView flag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_subject = itemView.findViewById(R.id.textView_subject);
            textView_date = itemView.findViewById(R.id.textView_date);
            flag = itemView.findViewById(R.id.flag);
        }
    }
}
