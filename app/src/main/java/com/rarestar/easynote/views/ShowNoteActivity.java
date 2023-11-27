package com.rarestar.easynote.views;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rarestar.easynote.R;
import com.rarestar.easynote.utility.Note;

import java.util.List;

public class ShowNoteActivity extends AppCompatActivity {
    TextView textView_subject,priority,textView_date,textView_note, textView_new;

    List<Note> noteList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        textView_subject = findViewById(R.id.textView_subject);
        textView_subject.setText("Subject : " + getIntent().getStringExtra("Subject"));

        priority = findViewById(R.id.priority);
        priority.setText("Priority :" + getIntent().getStringExtra("Priority"));

        textView_date = findViewById(R.id.textView_date);
        textView_date.setText("Create Date :" + getIntent().getStringExtra("Date"));

        textView_note = findViewById(R.id.textView_note);
        textView_note.setText(getIntent().getStringExtra("Note"));

        textView_new = findViewById(R.id.textView_new);
        textView_new.setOnClickListener(view -> {
            Intent intent = new Intent(ShowNoteActivity.this,CreateNoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}