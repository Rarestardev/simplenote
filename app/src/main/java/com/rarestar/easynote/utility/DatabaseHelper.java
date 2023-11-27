package com.rarestar.easynote.utility;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "note_easy";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Note.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + Note.DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public long insertNote(String note,String subject,String priority){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE,note);
        values.put(Note.COLUMN_SUBJECT,subject);
        values.put(Note.COLUMN_PRIORITY,priority);


        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MMM d");
        Date date = new Date();

        values.put(Note.COLUMN_TIME,dateFormat.format(date));

        long id = db.insert(Note.DB_TABLE,null,values);
        db.close();
        return id;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Note.DB_TABLE,
                new String[] {Note.COLUMN_ID,
                        Note.COLUMN_NOTE,
                        Note.COLUMN_SUBJECT,
                        Note.COLUMN_PRIORITY,
                        Note.COLUMN_TAG,
                        Note.COLUMN_TIME},
                Note.COLUMN_ID + "=? ",
                new String[]{String.valueOf(id)},
                null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }

        assert cursor != null;
        @SuppressLint("Range")
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_SUBJECT)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_PRIORITY)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TAG)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME))
        );
        cursor.close();
        return note;
    }

    @SuppressLint("Range")
    public List<Note> getNoteSortByPriority(String priority){
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Note.DB_TABLE + " WHERE " + Note.COLUMN_PRIORITY + "=" + priority;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNotes(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setSubject(cursor.getString(cursor.getColumnIndex(Note.COLUMN_SUBJECT)));
                note.setTag(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TAG)));
                note.setPriority(cursor.getString(cursor.getColumnIndex(Note.COLUMN_PRIORITY)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME)));

                notes.add(note);
            }while (cursor.moveToNext());
        }
        db.close();
        System.out.println(notes);
        return notes;
    }
    @SuppressLint("Range")
    public List<Note> search(String subject){
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Note.DB_TABLE + " WHERE " + Note.COLUMN_SUBJECT + " LIKE " + subject;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNotes(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setSubject(cursor.getString(cursor.getColumnIndex(Note.COLUMN_SUBJECT)));
                note.setTag(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TAG)));
                note.setPriority(cursor.getString(cursor.getColumnIndex(Note.COLUMN_PRIORITY)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME)));

                notes.add(note);
            }while (cursor.moveToNext());
        }
        db.close();
        System.out.println(notes);
        return notes;
    }

    @SuppressLint("Range")
    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Note.DB_TABLE + " ORDER BY " +
                Note.COLUMN_ID+ " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNotes(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setSubject(cursor.getString(cursor.getColumnIndex(Note.COLUMN_SUBJECT)));
                note.setTag(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TAG)));
                note.setPriority(cursor.getString(cursor.getColumnIndex(Note.COLUMN_PRIORITY)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIME)));

                notes.add(note);
            }while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    public int getNotesCount(){
        String countQuery = "SELECT * FROM " + Note.DB_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_SUBJECT,note.getSubject());
        values.put(Note.COLUMN_NOTE,note.getNotes());

        return db.update(Note.DB_TABLE,values,Note.COLUMN_ID+ "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.DB_TABLE , Note.COLUMN_ID+"=?",
                new String[] {String.valueOf(note.getId())});
        db.close();
    }
}