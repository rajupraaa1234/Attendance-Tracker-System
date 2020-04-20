package com.raju.attendencetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;


public class Subject extends AppCompatActivity {
    SqllitDatabase mydb;
    String sem;
    private TextInputLayout subname;
    private TextInputLayout hour;
    Spinner spinner;
    List<String> list;
    Button add_btn;
    Button rem_btn;
    TextView student;
    int choose=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        mydb=new SqllitDatabase(this);
        spinner=findViewById(R.id.spinner_sub);
        subname=findViewById(R.id.subject);
        hour=findViewById(R.id.hour);
        add_btn=findViewById(R.id.add_subject_btn);
        rem_btn=findViewById(R.id.removesubject);
        student =findViewById(R.id.stuname);
        Cursor res=mydb.getAllData();
        String str="nothing";
        while(res.moveToNext()){
            str=res.getString(1);
        }
        student.setText(str);
        list=new ArrayList<String>();
        list.add("choose subject to remove");
        Cursor cursor= mydb.allenrolledsubject();
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Subject.this);
                    builder.setTitle("Alert")
                            .setMessage("Are you sure to remove this Subject?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str=spinner.getSelectedItem().toString();
                                    deletesubject(str);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   // Toast.makeText(Subject.this, "No", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addsubject();
        removesubject();
    }

    public void addsubject(){
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(TextUtils.isEmpty(subname.getEditText().getText().toString())){
                      StyleableToast.makeText(Subject.this,"Please Enter subject",R.style.exampleToast).show();
                  }
                  else if(TextUtils.isEmpty(hour.getEditText().getText().toString())){
                      StyleableToast.makeText(Subject.this,"Please Enter Total class hour",R.style.exampleToast).show();
                  }
                  else if(!isNumeric(hour.getEditText().getText().toString())){
                      StyleableToast.makeText(Subject.this,"Please Enter integer value for class hour",R.style.exampleToast).show();
                  }
                  else{
                      String str=subname.getEditText().getText().toString();
                      String hr=hour.getEditText().getText().toString();
                      if(mydb.checksubject(str)){
                          boolean succ=mydb.subject_inserted(str,hr);
                          if(succ){
                              String st=str +" Added Successfully...";
                              StyleableToast.makeText(Subject.this,st,R.style.exampleToast).show();
                          }
                          else{
                              String st=str +" Not Added";
                              StyleableToast.makeText(Subject.this,st,R.style.exampleToast).show();
                          }
                      }
                      else{
                          String st=str +" Already Added...";
                          StyleableToast.makeText(Subject.this,st,R.style.exampleToast).show();
                      }
                      Intent nextactivity = new Intent(Subject.this, student_dashboard.class);
                      startActivity(nextactivity);
                      finish();
                  }
        }
        });
    }
    public void removesubject(){
          rem_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AddSubject();
              }
          });
    }


    public void AddSubject(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setVisibility(View.VISIBLE);
    }
    public void deletesubject(String str){
        if(mydb.delchecksubject(str)){
            if(mydb.delete_subject(str)){
                if(mydb.deletesubject(str) && mydb.deletesubjectfromleave(str)){
                }
                else{
                    StyleableToast.makeText(Subject.this,"Not remove properly",R.style.exampleToast).show();
                }
                 String st=str + " Successfully Deleted...";
                 StyleableToast.makeText(Subject.this,st,R.style.exampleToast).show();
                 //String st=str + "Su
            }
            else{
                String st=str + " Not Deleted...";
                StyleableToast.makeText(Subject.this,st,R.style.exampleToast).show();
            }
        }
        else{
            StyleableToast.makeText(Subject.this,"You Haven't Enrolled this Subject..",R.style.exampleToast).show();
        }
        Intent nextactivity = new Intent(Subject.this, student_dashboard.class);
        startActivity(nextactivity);
        finish();
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
