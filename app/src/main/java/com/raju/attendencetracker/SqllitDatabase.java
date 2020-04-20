package com.raju.attendencetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqllitDatabase extends SQLiteOpenHelper {

    public static final String Database_name = "Attendence.db";
    public static final String Table_name = "Student_table";
    public static final String Table_1 = "Subject_table";
    public static final String Table_2 = "Attendance_table";
    public static final String Table_3 = "Leave_table";
    //subject table
    public static final String sub_Col_1 = "subject_key";
    public static final String sub_Col_2 = "subject_name";
    public static final String sub_Col_3 = "subject_hour";
    //student table
    public static final String Col_1 = "Roll_number";
    public static final String Col_2 = "Student_name";
    public static final String Col_3 = "Semester";
    // Attendance Table
    public static final String att_Col_1 = "date";
    public static final String att_Col_2 = "subject";
    public static final String att_Col_3 = "attend_class";
    public static final String att_Col_4 = "miss_class";
    public static final String att_Col_5 = "cancel_class";
    public static final String att_Col_6 = "total_lecture";
    // Leave Table
    public static final String leave_Col_1 = "date";
    public static final String leave_Col_2 = "No_of_day";
    public static final String leave_Col_3 = "Leave_type";
    public static final String leave_Col_4 = "subject";

    public SqllitDatabase(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + Table_name + "(Roll_number TEXT PRIMARY KEY,Student_name TEXT,Semester TEXT)");
        db.execSQL(" create table " + Table_1 + "(subject_key INTEGER PRIMARY KEY AUTOINCREMENT,subject_name TEXT,subject_hour TEXT)");
        db.execSQL(" create table " + Table_2 + "(date TEXT,subject TEXT,attend_class INTEGER,miss_class INTEGER,cancel_class INTEGER,total_lecture INTEGER, PRIMARY KEY (date,subject))");
        db.execSQL(" create table " + Table_3 + "(date TEXT,No_of_day INTEGER,Leave_type TEXT,subject TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + Table_name);
        db.execSQL(" DROP TABLE IF EXISTS " + Table_1);
        db.execSQL(" DROP TABLE IF EXISTS " + Table_2);
        db.execSQL(" DROP TABLE IF EXISTS " + Table_3);
        onCreate(db);
    }

    public boolean insert(String roll, String name, String sem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_1, roll);
        contentValues.put(Col_2, name);
        contentValues.put(Col_3, sem);
        long result = db.insert(Table_name, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Table_name, null);
        return res;
    }

    public Cursor subgetAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Table_1, null);
        return res;
    }

    public boolean checksubject(String str) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_1);
        String[] sqlSelector = {"subject_key", "subject_name","subject_hour"};
        Cursor cursor = qb.query(db, sqlSelector, "subject_name LIKE ?", new String[]{"%" + str + "%"}, null, null, null);
        if (cursor.getCount() == 0)
            return true;
        return false;
    }

    public boolean subject_inserted(String str,String hr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sub_Col_2, str);
        contentValues.put(sub_Col_3, hr);
        long result = db.insert(Table_1, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean delete_subject(String str) {
        SQLiteDatabase db = this.getWritableDatabase();
        int temp = db.delete(Table_1, "subject_name = ?", new String[]{str});
        if (temp > 0)
            return true;
        return false;
    }

    public boolean delchecksubject(String str) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_1);
        String[] sqlSelector = {"subject_key", "subject_name","subject_hour"};
        Cursor cursor = qb.query(db, sqlSelector, "subject_name LIKE ?", new String[]{"%" + str + "%"}, null, null, null);
        if (cursor.getCount() == 0)
            return false;
        return true;
    }



    public boolean checkattendance(String sub, String date) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_2);
        String[] sqlSelector = {"date", "subject", "attend_class", "miss_class", "cancel_class", "total_lecture"};
        Cursor cursor = qb.query(db, sqlSelector, "date LIKE ? AND subject LIKE ?", new String[]{"%" + date + "%", "%" + sub + "%"}, null, null, null);
        if (cursor.getCount() == 0)
            return true;
        return false;
    }

    public boolean mark_attendance(String sub, String date, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(att_Col_1, date);
        contentValues.put(att_Col_2, sub);
        if (type.equals("present")) {
            contentValues.put(att_Col_3, 1);
            contentValues.put(att_Col_6, 1);
        } else if (type.equals("cencel")) {
            contentValues.put(att_Col_5, 1);
        } else if (type.equals("absent")) {
            contentValues.put(att_Col_4, 1);
            contentValues.put(att_Col_6, 1);
        }
        long result = db.insert(Table_2, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public ArrayList<Integer> subgetAllAttendance(String str) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        ArrayList<Integer> arr = new ArrayList<Integer>();
        qb.setTables(Table_2);
        String[] sqlSelector = {"date", "subject", "attend_class", "miss_class", "cancel_class", "total_lecture"};
        Cursor cursorp = qb.query(db, sqlSelector, "subject LIKE ? AND attend_class LIKE ?", new String[]{"%" + str + "%", "%" + "1" + "%"}, null, null, null);
        Cursor cursorm = qb.query(db, sqlSelector, "subject LIKE ? AND miss_class LIKE ?", new String[]{"%" + str + "%", "%" + "1" + "%"}, null, null, null);
        Cursor cursorc = qb.query(db, sqlSelector, "subject LIKE ? AND cancel_class LIKE ?", new String[]{"%" + str + "%", "%" + "1" + "%"}, null, null, null);
        Cursor cursort = qb.query(db, sqlSelector, "subject LIKE ? AND total_lecture LIKE ?", new String[]{"%" + str + "%", "%" + "1" + "%"}, null, null, null);
        arr.add(cursorp.getCount());
        arr.add(cursorm.getCount());
        arr.add(cursorc.getCount());
        arr.add(cursort.getCount());
        return arr;
    }

    public boolean checkleave(String sub,String date) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_3);
        String[] sqlSelector = {"date", "No_of_day", "Leave_type","subject"};
        Cursor cursor = qb.query(db, sqlSelector, "date LIKE ? AND subject LIKE ?", new String[]{"%" + date + "%", "%" + sub + "%"}, null, null, null);
        //Cursor cursor = qb.query(db, sqlSelector, "date LIKE ? AND subject LIKE ?", new String[]{"%" + date + "%", "%" + sub + "%"}, null, null, null);
        if (cursor.getCount() == 0)
            return true;
        return false;
    }

    public boolean addleave(String date, int day, String type,String sub) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(leave_Col_1, date);
        contentValues.put(leave_Col_2, day);
        contentValues.put(leave_Col_3, type);
        contentValues.put(leave_Col_4,sub);
        long result = db.insert(Table_3, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor LeaveAllData(String sub){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_3);
        String[] sqlSelector = {"date", "No_of_day", "Leave_type","subject"};
        Cursor cursor = qb.query(db, sqlSelector, "subject LIKE ?", new String[]{"%" + sub + "%"}, null, null, null);
        return cursor;
    }

    public Cursor getAttendamce(String date, String sub) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_2);
        String[] sqlSelector = {"date", "subject", "attend_class", "miss_class", "cancel_class", "total_lecture"};
        Cursor cursor=qb.query(db,sqlSelector,"date LIKE ? AND subject LIKE ?",new String[]{"%"+date+"%","%"+sub+"%"},null,null,null);
        return cursor;
    }
    public boolean updateData(String sub,String date,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(att_Col_1, date);
        contentValues.put(att_Col_2, sub);
        if (type.equals("present")) {
            contentValues.put(att_Col_3, 1);
            contentValues.put(att_Col_6, 1);
            contentValues.put(att_Col_4, 0);
            contentValues.put(att_Col_5, 0);
        } else if (type.equals("cencel")) {
            contentValues.put(att_Col_5, 1);
            contentValues.put(att_Col_6, 0);
            contentValues.put(att_Col_3, 0);
            contentValues.put(att_Col_4, 0);
        } else if (type.equals("absent")) {
            contentValues.put(att_Col_4, 1);
            contentValues.put(att_Col_6, 1);
            contentValues.put(att_Col_3, 0);
            contentValues.put(att_Col_5, 0);
        }
        db.update(Table_2, contentValues, "date = ? AND subject = ?",new String[] { date,sub });
        return true;
    }
    public Cursor allenrolledsubject(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Table_1, null);
        return res;
    }
    public int getalltodayattendance(String date){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_2);
        String[] sqlSelector = {"date", "subject", "attend_class", "miss_class", "cancel_class", "total_lecture"};
        Cursor cursor=qb.query(db,sqlSelector,"date LIKE ?",new String[]{"%"+date+"%"},null,null,null);
        return cursor.getCount();
    }
    public boolean deletesubject(String sub){
        SQLiteDatabase db = this.getWritableDatabase();
        int temp = db.delete(Table_2, "subject = ?", new String[]{sub});
        if (temp > 0)
            return true;
        return false;
    }
    public boolean deletesubjectfromleave(String sub){
        SQLiteDatabase db = this.getWritableDatabase();
        int temp = db.delete(Table_3, "subject = ?", new String[]{sub});
        if (temp > 0)
            return true;
        return false;
    }
    public boolean removeleave(String sub,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        int temp = db.delete(Table_3, "date = ? AND subject = ? ", new String[]{date,sub});
        if (temp > 0)
            return true;
        return false;
    }
    public boolean deleteattendance(String sub,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        int temp = db.delete(Table_2, "date = ? AND subject = ? ", new String[]{date,sub});
        if (temp > 0)
            return true;
        return false;
    }
    public Cursor gethour(String sub){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_1);
        String[] sqlSelector = {"subject_key","subject_name","subject_hour"};
        Cursor cursor=qb.query(db,sqlSelector,"subject_name LIKE ?",new String[]{"%"+sub+"%"},null,null,null);
        return cursor;
    }

}