package com.kenzz.reviewapp.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

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
        }
    }

    private void normalNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("WonNiMa")
                .setContentText("This is a notification!!!")
                .setAutoCancel(true)
                .setContentTitle("Title")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(PendingIntent.getActivity(this, 100, new Intent(this, RemoteViewsActivity.class), 0));

        NotificationManagerCompat.from(this).notify(0, builder.build());
    }
}
