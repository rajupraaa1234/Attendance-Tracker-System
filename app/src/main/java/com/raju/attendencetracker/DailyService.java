package com.raju.attendencetracker;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

public class DailyService extends BroadcastReceiver {
    public static final String CHANNEL_ID = "notifyLemubit";
    SqllitDatabase mydb;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context,Attendance.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Attendance.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);
        mydb=new SqllitDatabase(context);
        Cursor res=mydb.getAllData();
        String str="nothing";
        while(res.moveToNext()){
            str=res.getString(1);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_looks)
                .setContentTitle("Hello" + str)
                .setContentText("You Forgot to mark attendance please mark")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());
    }
}
