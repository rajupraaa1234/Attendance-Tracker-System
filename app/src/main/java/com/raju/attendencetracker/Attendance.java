package com.raju.attendencetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.time.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Attendance extends AppCompatActivity implements SingleOptionDailog.SingleChoiceListner {
    TextView date_btn;
    Button present_btn;
    Button cancel_btn;
    Button absent_btn;
    Button leave;
    TextView student;
    SqllitDatabase mydb;
    List<String> list;
    Spinner spinner;
    String currdate="nothing";
    int choose=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        date_btn=findViewById(R.id.choosedata);
        mydb=new SqllitDatabase(this);
        spinner=findViewById(R.id.mark_spinner);
        leave=findViewById(R.id.take_leave);
        present_btn=findViewById(R.id.present_btn);
        student=findViewById(R.id.stuname);
        cancel_btn=findViewById(R.id.cancel_btn);
        absent_btn=findViewById(R.id.absent_btn);
        Cursor res=mydb.getAllData();
        String str="nothing";
        while(res.moveToNext()){
            str=res.getString(1);
        }
        student.setText(str);
        handlerdatebtn();
        Cursor cursor=mydb.subgetAllData();
        list=new ArrayList<String>();
        list.add("Select Subject");
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));
        }

        date_btn.setText("Today Date " + currdate);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setGravity(Gravity.CENTER);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerdatebtn();
                checkdate();
            }
        });
        mark_attendance();
    }
    public void handlerdatebtn(){
        Calendar calendar=Calendar.getInstance();
        final int YEAR=calendar.get(Calendar.YEAR);
        final int MONTH=calendar.get(Calendar.MONTH);
        final int DATE=calendar.get(Calendar.DATE);
        int mon=MONTH+1;
        String str = DATE + "/" +mon+ "/" +YEAR;
        currdate=str;

    }
    public void mark_attendance(){
        present_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(currdate.equals("nothing"))
                    StyleableToast.makeText(Attendance.this,"Please Choose Date For Mark Attendance...",R.style.exampleToast).show();
                else if(currdate.equals("Not Valid Date"))
                    StyleableToast.makeText(Attendance.this,"Date Not Valid...",R.style.exampleToast).show();
                else if(choose==0 && list.size()==1)
                    StyleableToast.makeText(Attendance.this,"You must enroll in atleast one subject",R.style.exampleToast).show();
                else if(choose==0)
                    StyleableToast.makeText(Attendance.this,"Please Choose Subject for Mark Attendance...",R.style.exampleToast).show();
                else {
                    if (ckeckforlefthour()) {
                        handlerdatebtn();
                        String mark_subject = list.get(choose);
                        String mark_date = currdate;
                        if (mydb.checkattendance(mark_subject, mark_date) && mydb.checkleave(mark_subject, mark_date)) {
                            boolean ch = mydb.mark_attendance(mark_subject, mark_date, "present");
                            if (ch) {
                                StyleableToast.makeText(Attendance.this, "Attendance Added Successfully...", R.style.exampleToast).show();
                                Intent nextactivity = new Intent(Attendance.this, student_dashboard.class);
                                startActivity(nextactivity);
                                finish();
                            } else {
                                StyleableToast.makeText(Attendance.this, "Attendance Not Added", R.style.exampleToast).show();
                            }
                        } else
                            StyleableToast.makeText(Attendance.this, "Attendance Already Marked...", R.style.exampleToast).show();
                    }
                    else{
                        StyleableToast.makeText(Attendance.this, "Your total course hour's are finish", R.style.exampleToast).show();
                    }
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currdate.equals("nothing"))
                    StyleableToast.makeText(Attendance.this,"Please Choose Date For Mark Attendance...",R.style.exampleToast).show();
                else if(choose==0 && list.size()==1)
                    StyleableToast.makeText(Attendance.this,"You must enroll in atleast one subject",R.style.exampleToast).show();
                else if(choose==0)
                    StyleableToast.makeText(Attendance.this,"Please Choose Subject for Mark Attendance...",R.style.exampleToast).show();
                else {
                    if (ckeckforlefthour()) {
                        handlerdatebtn();
                        String mark_subject = list.get(choose);
                        String mark_date = currdate;
                        if (mydb.checkattendance(mark_subject, mark_date) && mydb.checkleave(mark_subject, mark_date)) {
                            boolean ch = mydb.mark_attendance(mark_subject, mark_date, "cancel");
                            if (ch)
                                StyleableToast.makeText(Attendance.this, "Attendance canceled Successfully...", R.style.exampleToast).show();
                            else {
                                StyleableToast.makeText(Attendance.this, "Attendance Not canceled...", R.style.exampleToast).show();
                            }
                        } else
                            StyleableToast.makeText(Attendance.this, "Attendance Already Marked...", R.style.exampleToast).show();
                    }
                    else{
                        StyleableToast.makeText(Attendance.this, "Your total course hour's are finish", R.style.exampleToast).show();
                    }
                }
            }
        });

        absent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currdate.equals("nothing"))
                    StyleableToast.makeText(Attendance.this,"Please Choose Date For Mark Attendance...",R.style.exampleToast).show();
                else if(choose==0 && list.size()==1)
                    StyleableToast.makeText(Attendance.this,"You must enroll in atleast one subject",R.style.exampleToast).show();
                else if(choose==0)
                    StyleableToast.makeText(Attendance.this,"Please Choose Subject for Mark Attendance...",R.style.exampleToast).show();
                else {
                    if (ckeckforlefthour()) {
                        handlerdatebtn();
                        String mark_subject = list.get(choose);
                        String mark_date = currdate;
                        if (mydb.checkattendance(mark_subject, mark_date) && mydb.checkleave(mark_subject, mark_date)) {
                            boolean ch = mydb.mark_attendance(mark_subject, mark_date, "absent");
                            if (ch)
                                StyleableToast.makeText(Attendance.this, "Absent Attendance Marked Successfully...", R.style.exampleToast).show();
                            else {
                                StyleableToast.makeText(Attendance.this, "Absent Attendance Not Marked...", R.style.exampleToast).show();
                            }
                        } else
                            StyleableToast.makeText(Attendance.this, "Attendance Already Marked...", R.style.exampleToast).show();
                    }
                    else{
                        StyleableToast.makeText(Attendance.this, "Your total course hour's are finish", R.style.exampleToast).show();
                    }
                }
            }
        });
    }
    public ArrayList<Integer> current_and_projected(String subject,SqllitDatabase mydb1){
        ArrayList<Integer> arr=mydb1.subgetAllAttendance(subject);
        Cursor cursor=mydb1.LeaveAllData(subject);
        Cursor cursor1 =mydb1.gethour(subject);
        String hour="0";
        if(cursor1.getCount()>0) {
            while (cursor1.moveToNext()) {
                hour = cursor1.getString(2).toString();
            }
        }
        int sum=cursor.getCount();
        arr.add(sum);
        int present=arr.get(0);
        int absent=arr.get(1);
        int cancel=arr.get(2);
        int total=arr.get(3);
        int leave =sum;
        int percent =1000;
        if(total!=0)
            percent =(present*100)/total;
        int totalhour=Integer.parseInt(hour)-sum;
        int want=(int)Math.ceil((80*totalhour)/(double)100);
        int remaining_want=want-present;
        int remaining_class=totalhour-total;
        int estimate=remaining_class-remaining_want;
        ArrayList<Integer> ar1=new ArrayList<>();
        ar1.add(percent);
        ar1.add(estimate);
        ar1.add(absent);
        ar1.add(sum);
        return ar1;
    }
    public ArrayList<Integer> View_Attendance(String subject, SqllitDatabase mydb1){
        ArrayList<Integer> arr=mydb1.subgetAllAttendance(subject);
        return arr;
    }
    public boolean modify_attendance(String sub,String date,String type,SqllitDatabase mydb1){
        return mydb1.updateData(sub,date,type);
    }
    public void checkdate(){
        if(currdate.equals("nothing")){
            StyleableToast.makeText(Attendance.this,"Please Choose Date For Take Leave...",R.style.exampleToast).show();
        }
        else if(list.get(choose).equals("Select Subject")){
            StyleableToast.makeText(Attendance.this,"You must enroll in atleast one subject",R.style.exampleToast).show();
        }
        else{
            if(ckeckforlefthour()) {
                findlistner();
            }
            else{
                StyleableToast.makeText(Attendance.this, "Your total course hour's are finish", R.style.exampleToast).show();
            }
        }
    }

    public void findlistner(){
        DialogFragment singlechoicedialog =new SingleOptionDailog();
        singlechoicedialog.setCancelable(false);
        singlechoicedialog.show(getSupportFragmentManager(),"single choice dialog");
    }
    @Override
    public void onPositiveButtonClicked(String[] arr, int position) {
        String mark_subject=list.get(choose);
        String mark_date=currdate;
        String type = arr[position];
        if(mydb.checkattendance(mark_subject,mark_date)){
            if(mydb.checkleave(mark_subject,mark_date)){
                if(check()){
                    if (mydb.addleave(mark_date,1, type,mark_subject)) {
                        StyleableToast.makeText(Attendance.this, "Leave Added Successfully...", R.style.exampleToast).show();
                        Intent nextactivity = new Intent(Attendance.this, student_dashboard.class);
                        startActivity(nextactivity);
                        finish();
                    }
                    else{
                        StyleableToast.makeText(Attendance.this, "Leave Not Added...", R.style.exampleToast).show();
                    }
                }else {
                    StyleableToast.makeText(Attendance.this, "You can't take more than 8 leave", R.style.exampleToast).show();
                }
            }else{
                StyleableToast.makeText(Attendance.this,"Today Leave Already Taken...",R.style.exampleToast).show();
            }
        }
        else{
            StyleableToast.makeText(Attendance.this,"Today Attendance Already Marked...",R.style.exampleToast).show();
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }
    public boolean check(){
        String mark_subject=list.get(choose);
        Cursor cursor=mydb.LeaveAllData(mark_subject);
        int sum=0;
        while(cursor.moveToNext()){
            sum=sum + Integer.parseInt(cursor.getString(1));
        }
        if(sum>=8)
            return false;
        return true;
    }
    public boolean ckeckforlefthour(){
        String sub=list.get(choose);
        Cursor cursor1 =mydb.gethour(sub);
        String totalhour="0";
        if(cursor1.getCount()>0) {
            while (cursor1.moveToNext()) {
                totalhour = cursor1.getString(2).toString();
            }
        }
        Attendance obj=new Attendance();
        List<Integer> arr =obj.View_Attendance(sub,mydb);
        Cursor cursor=mydb.LeaveAllData(sub);
        int sum=cursor.getCount();
        int attend=arr.get(0);
        int absent=arr.get(1);
        int cancel=arr.get(2);
        int total=arr.get(3)+sum;
        if(total>=Integer.parseInt(totalhour)){
            return false;
        }
        return true;
    }
}
