package com.rarestar.easynote.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rarestar.easynote.R;
import com.rarestar.easynote.utility.DatabaseHelper;
import com.rarestar.easynote.utility.Detail_Adapter;
import com.rarestar.easynote.utility.Note;
import com.rarestar.easynote.utility.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView textView_title,textView_newNote;
    ImageView back;
    RecyclerView recyclerView;
    List<Note> list = new ArrayList<>();
    String whichActivity = MainActivity.infoActivityForIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        ReadableFromDatabaseSetRecycler();
    }
    private void ReadableFromDatabaseSetRecycler(){
        DatabaseHelper db = new DatabaseHelper(DetailActivity.this);
        list.addAll(db.getAllNotes());
        Detail_Adapter adapter = new Detail_Adapter(this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void initViews(){
        textView_title = findViewById(R.id.textView_title);
        textView_title.setText(whichActivity);

        textView_newNote = findViewById(R.id.textView_newNote);
        textView_newNote.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this,CreateNoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
    }
}