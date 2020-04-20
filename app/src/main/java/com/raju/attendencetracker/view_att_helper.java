package com.raju.attendencetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class view_att_helper extends AppCompatActivity {
    SqllitDatabase mydb;
    TextView current;
    TextView Estimated;
    TextView showabsent;
    TextView showleave;
    Button view_btn;
    Spinner spinner;
    List<String> list;
    TextView student;
    int choose=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_att_helper);
        current=findViewById(R.id.current_plain_text);
        Estimated=findViewById(R.id.estimated);
        view_btn=findViewById(R.id.view_btn1);
        spinner =findViewById(R.id.choose_subject);
        showleave=findViewById(R.id.leaveshow);
        showabsent=findViewById(R.id.absentshow);
        mydb=new SqllitDatabase(this);
        student=findViewById(R.id.stuname);
        Cursor res=mydb.getAllData();
        String str="nothing";
        while(res.moveToNext()){
            str=res.getString(1);
        }
        student.setText(str);
        list=new ArrayList<String>();
        Cursor cursor=mydb.subgetAllData();
        list.add("Select Subject");
        while(cursor.moveToNext()){
            list.add(cursor.getString(1));
        }
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
        helper();
    }
    public void helper(){
        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose==0 && list.size()==1)
                    StyleableToast.makeText(view_att_helper.this,"You must enroll in atleast one subject",R.style.exampleToast).show();
                else if(choose==0)
                    StyleableToast.makeText(view_att_helper.this,"Select Subject",R.style.exampleToast).show();
                else{
                    Attendance obj=new Attendance();
                    ArrayList<Integer> arr = obj.current_and_projected(list.get(choose),mydb);
                    String str1="";
                    if(arr.get(0)==1000){
                        str1="Your class is not started yet";
                    }
                    else
                        str1="Your current attendance percentage : " + arr.get(0) + "%";
                    String str3="Absent classes : " + arr.get(2);
                    String str4="Taken Leave : " + arr.get(3);
                    String str2="";
                    if(arr.get(1)<0){
                        str2="Your attendance is too low so you can't make 80 percent attendance in this course";
                    }
                    else
                        str2="Number of hour you can missed : "  + arr.get(1);
                    current.setText(str1);
                    Estimated.setText(str2);
                    showabsent.setText(str3);
                    showleave.setText(str4);

                    if(arr.get(0)<80)
                        current.setTextColor(getResources().getColor(R.color.danger));
                    else
                        current.setTextColor(getResources().getColor(R.color.colorPrimary));
                    showabsent.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Estimated.setTextColor(getResources().getColor(R.color.colorPrimary));
                        showleave.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
    }
}
