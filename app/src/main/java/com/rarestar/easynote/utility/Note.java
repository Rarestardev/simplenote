package com.rarestar.easynote.utility;

public class Note {
    int id;
    String subject,notes,date,tag,priority;

    public static final String DB_TABLE = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE= "text_note";
    public static final String COLUMN_SUBJECT= "subject";
    public static final String COLUMN_TAG= "tag";
    public static final String COLUMN_PRIORITY= "priority";
    public static final String COLUMN_TIME= "timestamp";

    public static final String CREATE_TABLE =
            "create table " + DB_TABLE + "(" + COLUMN_ID + " integer primary key autoincrement," +
            COLUMN_SUBJECT + " text," + COLUMN_NOTE + " text," + COLUMN_TAG + " text," + COLUMN_PRIORITY + " text," + COLUMN_TIME + " datetime )";
    public Note(){

    }
    public Note(int id,String notes , String date ,String subject,String priority,String tag) {
        this.id=id;
        this.notes=notes;
        this.date=date;
        this.subject=subject;
        this.tag=tag;
        this.priority=priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
