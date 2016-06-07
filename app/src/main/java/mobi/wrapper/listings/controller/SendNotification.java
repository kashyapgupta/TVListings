package mobi.wrapper.listings.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;

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
