package com.raju.attendencetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class student_dashboard extends AppCompatActivity {
    ImageView addSubject;
    ImageView markattendance;
    ImageView viewattendance;
    ImageView viewattendance1;
    TextView name;
    SqllitDatabase mydb;
    ImageView modify_attendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        addSubject=findViewById(R.id.addsubject);
        markattendance=findViewById(R.id.mark_attendance);
        viewattendance=findViewById(R.id.view_atndnce);
        viewattendance1=findViewById(R.id.view_atndnce1);
        name=findViewById(R.id.stuname);
        modify_attendance=findViewById(R.id.modify);
        mydb=new SqllitDatabase(this);
        Cursor res=mydb.getAllData();
        String str="nothing";
        while(res.moveToNext()){
            str=res.getString(1);
        }
        name.setText(str);
        AddSubject();
        Markattendance();
        View_Attendance_percent();
        View_Attendance();
        Modify();

    }
   public void AddSubject(){
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(student_dashboard.this,Subject.class);
                startActivity(nextactivity);
            }
        });
   }
   public void Markattendance(){
        markattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(student_dashboard.this,Attendance.class);
                startActivity(nextactivity);
            }
        });
   }
   public void View_Attendance_percent(){
        viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(student_dashboard.this,view_att_helper.class);
                startActivity(nextactivity);
            }
        });
   }
   public void View_Attendance(){
        viewattendance1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(student_dashboard.this,view_att_helper1.class);
                startActivity(nextactivity);
            }
        });
   }
   public void Modify(){
        modify_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextactivity = new Intent(student_dashboard.this,Modify_helper.class);
                startActivity(nextactivity);
            }
        });
   }
    public void onBackPressed()
    {
        finish();
        moveTaskToBack(true);
    }
}
