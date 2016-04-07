package com.tvlistings.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tvlistings.R;
import com.tvlistings.constants.UrlConstants;

/**
 * Created by Rohit on 4/6/2016.
 */
public class SendNotification {

    public void setNotificationData (Context context, String data) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("TVListings")
                .setContentText(data);
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(UrlConstants.notificationID++, mBuilder.build());
    }

}
