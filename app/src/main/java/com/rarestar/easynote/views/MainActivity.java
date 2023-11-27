package com.rarestar.easynote.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rarestar.easynote.R;
import com.rarestar.easynote.utility.DatabaseHelper;
import com.rarestar.easynote.utility.Note;
import com.rarestar.easynote.utility.RecyclerAdapter;
import com.rarestar.easynote.utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab_add_note;
    RelativeLayout drawer,search_layout,layout_choose_priority;
    CardView cardView_profile;
    ImageView imageView_profile,close_drawer,search,ic_layout_mode,ic_showMode;
    LinearLayout subjects,tags,priority,about_us,exit,root_layout,layout_priority;
    TextView txt_appName,textView_showMode;
    TextView HighPriority,MediumPriority,LowPriority,NoPriority;
    EditText editText_search;
    RecyclerView recyclerView,recyclerView_search;
    boolean isOpenDrawer = true;
    public static String infoActivityForIntent;
    RecyclerAdapter recyclerAdapter;
    private DatabaseHelper db;
    List<Note> noteList = new ArrayList<>();

    private static final String SHER_PREF_NAME = "UI_PREF";
    private static final String SHER_DRAWER = "drawer";
    private static final String SHER_LAYOUT = "layout";

    boolean layoutMode;
    boolean tt = true;
    String state;
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindViews();
        initStateLayoutAndDrawer();
        RecyclerLongPress();
        recyclerView.refreshDrawableState();
        onClickViews();
        if (tt){
            ReadableFromDatabaseSetRecycler();
        }else {
            getNotesSortByPriority(state);
        }
        ToggleEmptyNote();
    }
    private void FindViews() {
        fab_add_note = findViewById(R.id.fab_add_note);
        drawer = findViewById(R.id.drawer);
        cardView_profile = findViewById(R.id.cardView_profile);
        imageView_profile = findViewById(R.id.imageView_profile);
        subjects = findViewById(R.id.subjects);
        tags = findViewById(R.id.tags);
        priority = findViewById(R.id.priority);
        about_us = findViewById(R.id.about_us);
        exit = findViewById(R.id.exit);
        close_drawer = findViewById(R.id.close_drawer);
        search = findViewById(R.id.search);
        txt_appName = findViewById(R.id.txt_appName);
        editText_search = findViewById(R.id.editText_search);
        root_layout = findViewById(R.id.root_layout);
        ic_layout_mode = findViewById(R.id.ic_layout_mode);
        textView_showMode = findViewById(R.id.textView_showMode);
        recyclerView = findViewById(R.id.recyclerView);
        search_layout = findViewById(R.id.search_layout);
        recyclerView_search = findViewById(R.id.recyclerView_search);
        layout_priority = findViewById(R.id.layout_priority);
        ic_showMode = findViewById(R.id.ic_showMode);
        layout_choose_priority = findViewById(R.id.layout_choose_priority);
        tt=true;
        HighPriority = findViewById(R.id.HighPriority);
        MediumPriority = findViewById(R.id.MediumPriority);
        LowPriority = findViewById(R.id.LowPriority);
        NoPriority = findViewById(R.id.NoPriority);
    }
    private void onClickViews(){
        fab_add_note.setOnClickListener(this);
        cardView_profile.setOnClickListener(this);
        subjects.setOnClickListener(this);
        tags.setOnClickListener(this);
        priority.setOnClickListener(this);
        about_us.setOnClickListener(this);
        exit.setOnClickListener(this);
        close_drawer.setOnClickListener(this);
        search.setOnClickListener(this);
        layout_choose_priority.setOnClickListener(this);
        ic_layout_mode.setOnClickListener(firstListener);

        if (layoutMode){
            ic_layout_mode.setImageResource(R.drawable.ic_horizontal);
        }else {
            ic_layout_mode.setImageResource(R.drawable.ic_vertical_layout);
        }
    }
    View.OnClickListener firstListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ic_layout_mode.setImageResource(R.drawable.ic_horizontal);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            recyclerView.refreshDrawableState();
            SaveModeRecyclerView(true);
            ic_layout_mode.setOnClickListener(secondListener);
        }
    };
    View.OnClickListener secondListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ic_layout_mode.setImageResource(R.drawable.ic_vertical_layout);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
            recyclerView.refreshDrawableState();
            SaveModeRecyclerView(false);
            ic_layout_mode.setOnClickListener(firstListener);
        }
    };

    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_note:
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.cardView_profile:
                Intent ProfileActivity = new Intent(MainActivity.this, UserProfileActivity.class);
                ProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ProfileActivity);
                break;
            case R.id.subjects:
                infoActivityForIntent = "Subjects";
                Intent subjects = new Intent(MainActivity.this, DetailActivity.class);
                subjects.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(subjects);
                break;
            case R.id.tags:
                infoActivityForIntent = "Tags";
                Intent tags = new Intent(MainActivity.this,DetailActivity.class);
                tags.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tags);
                break;
            case R.id.priority:
                infoActivityForIntent = "Priority";
                Intent priority = new Intent(MainActivity.this,DetailActivity.class);
                priority.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(priority);
                break;
            case R.id.about_us:
                DialogAboutUs();
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.close_drawer:
                if (drawer.getVisibility() == View.VISIBLE){
                    drawer.setVisibility(View.GONE);
                    close_drawer.setImageResource(R.drawable.ic_menu);
                    isOpenDrawer = false;
                }else if (drawer.getVisibility() == View.GONE){
                    drawer.setVisibility(View.VISIBLE);
                    close_drawer.setImageResource(R.drawable.ic_drawer);
                    isOpenDrawer = true;
                }
                SaveStateDrawer(isOpenDrawer);
                break;
            case R.id.layout_choose_priority:
                tt = false;
                SelectPriority();
                break;
            case R.id.search:
                SearchHandle();
                break;
        }//switch
    }// on click

    private void DialogAboutUs() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.layout_about_us);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        TextView myEmail = dialog.findViewById(R.id.myEmail);
        myEmail.setOnLongClickListener(view -> {
            String copyText = myEmail.getText().toString();
            String label = "Gmail Address";
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(label,copyText);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
            return true;
        });

        ImageView telegram = dialog.findViewById(R.id.telegram);
        telegram.setOnClickListener(view -> {
            Intent openTelegram = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(openTelegram);
            dialog.dismiss();
        });

        ImageView instagram = dialog.findViewById(R.id.instagram);
        instagram.setOnClickListener(view -> {
            Intent openInstagram = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(openInstagram);
            dialog.dismiss();
        });

        ImageView close_dialog = dialog.findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(view -> dialog.dismiss());
    }

    private void SelectPriority() {
        if (layout_priority.getVisibility() == View.GONE){
            layout_priority.setVisibility(View.VISIBLE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_drop_down_24);
            ShowNotesByPriority();
        }
        else if (layout_priority.getVisibility() == View.VISIBLE) {
            layout_priority.setVisibility(View.GONE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_right_24);
        }
    }
    @SuppressLint("SetTextI18n")
    private void ShowNotesByPriority() {
        HighPriority.setOnClickListener(view -> {
            textView_showMode.setText("High Priority");
            state = "High";
            layout_priority.setVisibility(View.GONE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_right_24);
        });
        MediumPriority.setOnClickListener(view -> {
            textView_showMode.setText("Medium Priority");
            state = "Medium";
            layout_priority.setVisibility(View.GONE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_right_24);
        });
        LowPriority.setOnClickListener(view -> {
            textView_showMode.setText("Low Priority");
            state = "Low";
            layout_priority.setVisibility(View.GONE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_right_24);
        });
        NoPriority.setOnClickListener(view -> {
            textView_showMode.setText("All Note");
            tt = true;
            layout_priority.setVisibility(View.GONE);
            ic_showMode.setImageResource(R.drawable.baseline_arrow_right_24);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getNotesSortByPriority(String priority) {
        db = new DatabaseHelper(MainActivity.this);
        noteList.addAll(db.getNoteSortByPriority(priority));
        recyclerAdapter = new RecyclerAdapter(MainActivity.this,noteList);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.refreshDrawableState();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void ToggleEmptyNote() {
        TextView NoNotes = findViewById(R.id.NoNotes);
        if (db.getNotesCount() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            NoNotes.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            NoNotes.setVisibility(View.VISIBLE);
        }
    }
    private void RecyclerLongPress(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                MainActivity.this, recyclerView, (view, position) -> showActionDialog(position)
        ));
    }

    private void showActionDialog(int position) {
        CharSequence[] items = new CharSequence[]{"Delete","Edit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose option :");
        builder.setItems(items, (dialogInterface, i) -> {
            switch (i){
                case 0:
                    deleteNote(position);
                    break;
                case 1:
                    ShowEditNoteDialog(true,noteList.get(position),position);
                    break;
            }
        });
        builder.show();
    }
    private void deleteNote(int position){
        db.deleteNote(noteList.get(position));
        noteList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
        ToggleEmptyNote();
    }
    private void ShowEditNoteDialog (final boolean shouldUpdate , final Note note , int position){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_edit_note);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView textView_subject = dialog.findViewById(R.id.textView_subject);
        EditText editText_note = dialog.findViewById(R.id.editText_note);
        if (shouldUpdate && note != null){
            editText_note.setText(note.getNotes());
            textView_subject.setText(note.getSubject());
        }

        ImageView close_dialog = dialog.findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(view -> dialog.dismiss());

        Button btn_save = dialog.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(view -> {
            if (editText_note.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this, "Enter Note!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }else{
                String text = editText_note.getText().toString();
                updateNote(text,position);
                Toast.makeText(MainActivity.this, "Note Edited!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    private void updateNote(String note,int position){
        Note n = noteList.get(position);
        n.setNotes(note);
        db.updateNote(n);
        noteList.set(position,n);
        recyclerAdapter.notifyItemChanged(position);
        ToggleEmptyNote();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void ReadableFromDatabaseSetRecycler(){
        db = new DatabaseHelper(MainActivity.this);
        noteList.addAll(db.getAllNotes());
        recyclerAdapter = new RecyclerAdapter(MainActivity.this,noteList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void SaveStateDrawer(boolean isOpenDrawer) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHER_PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHER_DRAWER,isOpenDrawer);
        editor.apply();
    }

    private void SaveModeRecyclerView(boolean Horizontal){
        SharedPreferences sharedPreferences = getSharedPreferences(SHER_PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHER_LAYOUT,Horizontal);
        editor.apply();
    }
    private void initStateLayoutAndDrawer() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHER_PREF_NAME, Context.MODE_PRIVATE);
        boolean drawerLayout = sharedPreferences.getBoolean(SHER_DRAWER,true);
        layoutMode = sharedPreferences.getBoolean(SHER_LAYOUT,true);

        if (drawerLayout){
            drawer.setVisibility(View.VISIBLE);
            close_drawer.setImageResource(R.drawable.ic_drawer);
        }else{
            drawer.setVisibility(View.GONE);
            close_drawer.setImageResource(R.drawable.ic_menu);
        }
        if (layoutMode){
            layoutManager = new GridLayoutManager(MainActivity.this,2);
            recyclerView.refreshDrawableState();
        }else{
            layoutManager = new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false);
            recyclerView.refreshDrawableState();
        }
    }
    private void SearchHandle() {
        if (editText_search.getVisibility() == View.GONE){
            txt_appName.setVisibility(View.GONE);
            editText_search.setVisibility(View.VISIBLE);
            search.setImageResource(R.drawable.ic_close);
            root_layout.setVisibility(View.GONE);
            search_layout.setVisibility(View.VISIBLE);
            editText_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence != null){
                       // Search(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }else if (editText_search.getVisibility() == View.VISIBLE){
            txt_appName.setVisibility(View.VISIBLE);
            editText_search.setVisibility(View.GONE);
            search.setImageResource(R.drawable.ic_search);
            root_layout.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void Search(String subject) {
        db = new DatabaseHelper(MainActivity.this);
        noteList.addAll(db.search(subject));
        recyclerAdapter = new RecyclerAdapter(MainActivity.this,noteList);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView_search.setAdapter(recyclerAdapter);
        recyclerView_search.refreshDrawableState();
        recyclerView_search.setLayoutManager(layoutManager);
        recyclerView_search.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        if (editText_search.getVisibility() == View.VISIBLE){
            txt_appName.setVisibility(View.VISIBLE);
            editText_search.setVisibility(View.GONE);
            search.setImageResource(R.drawable.ic_search);
            root_layout.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}