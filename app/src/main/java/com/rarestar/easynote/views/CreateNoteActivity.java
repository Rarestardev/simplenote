package com.rarestar.easynote.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rarestar.easynote.R;
import com.rarestar.easynote.utility.DatabaseHelper;
import com.rarestar.easynote.utility.Note;
import com.rarestar.easynote.utility.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back,more,priority;
    TextView textView_save_note,HighPriority,MediumPriority,LowPriority,NoPriority;
    EditText editText_subject,editText_note;
    LinearLayout layout_more,priority_layout;
    private DatabaseHelper db;
    List<Note> noteList = new ArrayList<>();
    String PriorityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
    }// onCreate

    private void initViews() {
        // Activity Views :
        back = findViewById(R.id.back);
        more = findViewById(R.id.more);
        priority = findViewById(R.id.priority);
        textView_save_note = findViewById(R.id.textView_save_note);
        editText_subject = findViewById(R.id.editText_subject);
        editText_note = findViewById(R.id.editText_note);
        layout_more = findViewById(R.id.layout_more);
        priority_layout = findViewById(R.id.priority_layout);
        HighPriority = findViewById(R.id.HighPriority);
        MediumPriority = findViewById(R.id.MediumPriority);
        LowPriority = findViewById(R.id.LowPriority);
        NoPriority = findViewById(R.id.NoPriority);

        db = new DatabaseHelper(CreateNoteActivity.this);
        onClickViews();
    }//initViews

    private void onClickViews() {
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        priority.setOnClickListener(this);
        textView_save_note.setOnClickListener(this);
    }//onClickViews

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                msgDialog();
                break;
            case R.id.more:
                menuDialog();
                break;
            case R.id.textView_save_note:
                if (editText_note.getText().toString().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this,"No notes for save!",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    String subject = editText_subject.getText().toString();
                    String notes = editText_note.getText().toString();
                    if (PriorityMode == null){
                        PriorityMode = "No";
                    }
                    SaveNote(notes,subject,PriorityMode);
                }
                break;
            case R.id.priority:
                if (priority_layout.getVisibility() == View.GONE){
                    priority_layout.setVisibility(View.VISIBLE);
                    SetPriority();
                } else if (priority_layout.getVisibility() == View.VISIBLE) {
                    priority_layout.setVisibility(View.GONE);
                }
                break;
        }//switch
    }// onclick

    private void SetPriority() {
        HighPriority.setOnClickListener(view -> {
            PriorityMode = "High";
            priority.setImageResource(R.drawable.high_priority);
            priority_layout.setVisibility(View.GONE);
        });
        MediumPriority.setOnClickListener(view -> {
            PriorityMode = "Medium";
            priority.setImageResource(R.drawable.medium_priority);
            priority_layout.setVisibility(View.GONE);
        });
        LowPriority.setOnClickListener(view -> {
            PriorityMode = "Low";
            priority.setImageResource(R.drawable.low_priority);
            priority_layout.setVisibility(View.GONE);
        });
        NoPriority.setOnClickListener(view -> {
            PriorityMode = "No";
            priority.setImageResource(R.drawable.ic_flag);
            priority_layout.setVisibility(View.GONE);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void SaveNote(String notes, String subject , String priority) {
        long id = db.insertNote(notes,subject,priority);
        Note n = db.getNote(id);

        if (n != null){
            noteList.add(0,n);
        }
        RecyclerAdapter adapter = new RecyclerAdapter(CreateNoteActivity.this,noteList);
        adapter.notifyDataSetChanged();
        Toast.makeText(CreateNoteActivity.this,"Saved success!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateNoteActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    private void menuDialog() {
        if (layout_more.getVisibility() == View.GONE){
            layout_more.setVisibility(View.VISIBLE);
        }else {
            layout_more.setVisibility(View.GONE);
        }
    }//menu dialog

    private void msgDialog() {
        if (editText_note.getText().toString().isEmpty()){
            Intent intent = new Intent(CreateNoteActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            AlertDialog.Builder msg = new AlertDialog.Builder(CreateNoteActivity.this);
            msg.setIcon(R.drawable.baseline_warning_24);
            msg.setCancelable(false);
            msg.setTitle("Warning!");
            msg.setMessage("Your notes are not saved?");
            msg.setNegativeButton("Discard", (dialogInterface, i) -> {
                Toast.makeText(CreateNoteActivity.this,"Discard Note!",Toast.LENGTH_SHORT).show();
                finish();
            });
            msg.setPositiveButton("Save", (dialogInterface, i) -> {
                String subject = editText_subject.getText().toString();
                String notes = editText_note.getText().toString();
                SaveNote(notes,subject,PriorityMode);
            });
            msg.create();
            msg.show();
        }
    }
    @Override
    public void onBackPressed() {
        if (layout_more.getVisibility() == View.VISIBLE){
            layout_more.setVisibility(View.GONE);
        }else {
            msgDialog();
        }
    }
}