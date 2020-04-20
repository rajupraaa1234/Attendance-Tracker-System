package com.raju.attendencetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    SqllitDatabase mydb;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemClock.sleep(3000);
        mydb=new SqllitDatabase(this);
        boolean flow=checkregister();
        Cursor cursor=mydb.allenrolledsubject();
        int count=cursor.getCount();
        int flag=0;

        // check for notification//
        if(count>0){
            alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, DailyService.class);
            alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
             int todaycount = todaymarkattendance();
             if(count>0 && todaycount!=count){
                 createNotificationchannel();
                 flag=1;
                 Calendar calendar = Calendar.getInstance();
                 calendar.setTimeInMillis(System.currentTimeMillis());
                 calendar.set(Calendar.HOUR_OF_DAY,20);
                 alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                         AlarmManager.INTERVAL_DAY, alarmIntent);
             }
             else if(alarmMgr!= null && count==todaycount){
                 alarmMgr.cancel(alarmIntent);
                 flag=0;
             }
        }
        int check_short=0;
        while(cursor.moveToNext()){
            String sub=cursor.getString(1);
            Attendance obj=new Attendance();
             ArrayList<Integer> arr  =obj.current_and_projected(sub,mydb);
            if(arr.get(0)<80){
                check_short++;
                break;
            }
        }
        if(check_short>0){
            alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, ShortAttendanceService.class);
            alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            createNotificationchannel();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,20);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
        else if(alarmMgr!= null && check_short==0 && flag==0){
            alarmMgr.cancel(alarmIntent);
        }
        if(flow==false) {
            Intent nextactivity = new Intent(MainActivity.this, Student.class);
            startActivity(nextactivity);
            finish();
        }
        else{
               Intent nextactivity = new Intent(MainActivity.this, student_dashboard.class);
               startActivity(nextactivity);
               finish();
        }
    }
    public boolean checkregister(){

        Cursor res=mydb.getAllData();
        if(res.getCount()>0)
            return true;
        return false;
    }
    public int todaymarkattendance(){
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH)+1;
        int DATE = calendar.get(Calendar.DATE);
        String str = DATE + "/" + MONTH + "/" + YEAR;
        int count=mydb.getalltodayattendance(str);
        return count;
    }
    public void createNotificationchannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int important= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel= new NotificationChannel("notifyLemubit",name,important);
            channel.setDescription(description);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
