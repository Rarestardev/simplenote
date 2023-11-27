package com.rarestar.easynote.utility;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rarestar.easynote.R;
import com.rarestar.easynote.views.CreateNoteActivity;
import com.rarestar.easynote.views.ShowNoteActivity;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Note> NoteList;
    int priorityIconDrawable;
    public static int position;
    public RecyclerAdapter(Context context, List<Note> noteList) {
        this.context = context;
        NoteList = noteList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_layout_vertical,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.textView_subject.setText(NoteList.get(position).getSubject());
        holder.textView_notes.setText(NoteList.get(position).getNotes());
        holder.textView_date.setText(NoteList.get(position).getDate());

        String mode = NoteList.get(position).getPriority();

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
        holder.icon_priority.setImageResource(priorityIconDrawable);
    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout notes_click;
        TextView textView_subject,textView_date,textView_notes;
        ImageView icon_priority;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_click = itemView.findViewById(R.id.notes_click);

            textView_subject = itemView.findViewById(R.id.textView_subject);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_notes = itemView.findViewById(R.id.textView_notes);
            icon_priority = itemView.findViewById(R.id.icon_priority);

            notes_click.setOnClickListener(view -> {
                String text_note = textView_notes.getText().toString();
                String subject = textView_subject.getText().toString();
                Intent intent = new Intent(context, ShowNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Note",text_note);
                intent.putExtra("Subject",subject);
                intent.putExtra("Date",NoteList.get(getAdapterPosition()).getDate());
                intent.putExtra("Priority",NoteList.get(getAdapterPosition()).getPriority());
                intent.putExtra("ID",NoteList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }// view holder
    }// Class
}
