package com.kenzz.reviewapp.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.kenzz.reviewapp.R;

public class RemoteViewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_views);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remoteViews_btn_1:
                normalNotification();
                break;
            case R.id.remoteViews_btn_2:
                customNotification();
                break;
        }
    }

    private void normalNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("WonNiMa")//首次通知出现上升动画
                .setContentText("This is a notification!!!")
                .setAutoCancel(true)
                .setContentTitle("Title")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentInfo("This is info")
                .setStyle(new NotificationCompat.InboxStyle().setBigContentTitle("new notification"))
                .setContentIntent(PendingIntent.getActivity(this,100,new Intent(this,RemoteViewsActivity.class),PendingIntent.FLAG_NO_CREATE))
                .setWhen(System.currentTimeMillis());

        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    private void customNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),R.layout.notification_sample);
        builder.setContent(remoteView);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setStyle(new NotificationCompat.BigPictureStyle());
        builder.setCustomBigContentView(remoteView);
        // builder.setCustomContentView();
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }
}
