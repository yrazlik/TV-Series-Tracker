package com.yrazlik.tvseriestracker;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yrazlik.tvseriestracker.activities.TvSeriesTrackerPushActivity;

import java.util.Date;

public class TvSeriesTrackerFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "FirebaseMessage";
    public static final String FIREBASE_PUSH_TOPIC = "push_topic";
    public static final String CHANNEL_ID = "TVSERIESTRACKERAPP_CHANNEL";

    private Intent notificationIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            notificationIntent = new Intent(this, TvSeriesTrackerPushActivity.class);
            notificationIntent.setAction(Long.toString(System.currentTimeMillis())); //THIS IS NECESSARY, OTHERWISE ONLY THE FIRST NOTIFICATION WILL WORK
            setParameters(remoteMessage);
        } catch (Exception e) {
            notificationIntent = null;
        }

        if (notificationIntent != null) {
            showNotification(remoteMessage);
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        int notificationId = generateRandomUniqueInt();
        final NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pIntent = PendingIntent.getActivity(this, generateRandomUniqueInt(), notificationIntent, 0);

        mgr.notify(notificationId, createPushNotification(getTicker(remoteMessage), getNotificationTitle(remoteMessage), getBody(remoteMessage), pIntent));

    }

    private String getNotificationTitle(RemoteMessage remoteMessage) {
        if(remoteMessage != null &&  remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            try {
                return remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TITLE);
            }catch (Exception ignored) {}
        }
        return getString(R.string.app_name);
    }

    private String getNotificationLocale(RemoteMessage remoteMessage) {
        if(remoteMessage != null &&  remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            try {
                return remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_LOCALE);
            }catch (Exception ignored) {}
        }
        return "";
    }

    private String getTicker(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            if (Commons.isValidString(remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TICKER))) {
                return remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TICKER);
            }
        }
        return getString(R.string.app_name);
    }

    private String getBody(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            if (Commons.isValidString(remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY))) {
                return remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY);
            }
        }
        return getString(R.string.app_name);
    }

    private void setParameters(RemoteMessage remoteMessage) {

        try {
            notificationIntent.putExtra(TvSeriesTrackerNotification.PARAMETER_IS_PUSH, true);
        } catch (Exception ignored) {}

        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {

            try {
                notificationIntent.putExtra(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TITLE, remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TITLE));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(TvSeriesTrackerNotification.PUSH_NOTIFICATION_LOCALE, remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_LOCALE));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(TvSeriesTrackerNotification.DEEPLINK_EXTRA, remoteMessage.getData().get(TvSeriesTrackerNotification.DEEPLINK_EXTRA));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TICKER, remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TICKER));
            } catch (Exception ignored) {}

            try {
                notificationIntent.putExtra(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY, remoteMessage.getData().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY));
            } catch (Exception ignored) {}
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    private Notification createPushNotification(CharSequence ticker, CharSequence title, CharSequence contentText, PendingIntent pIntent) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setTicker(ticker).setContentTitle(title)
                    .setContentText(contentText).setContentIntent(pIntent).setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));

            builder.setAutoCancel(true);
            Notification notif = builder.build();

            notif.defaults |= Notification.DEFAULT_SOUND;
            notif.defaults |= Notification.DEFAULT_LIGHTS;
            notif.defaults |= Notification.DEFAULT_VIBRATE;
            notif.defaults |= Notification.FLAG_AUTO_CANCEL;

            return notif;
        }else {
            //createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_tv/*.push_icon_lollipop*/)
                    .setTicker(ticker).setContentTitle(title).setContentText(contentText).setContentIntent(pIntent)
                    .setColor(getResources().getColor(R.color.colorPrimary)).setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));;

            builder.setAutoCancel(true);
            Notification notif = builder.build();

            notif.defaults |= Notification.DEFAULT_SOUND;
            notif.defaults |= Notification.DEFAULT_LIGHTS;
            notif.defaults |= Notification.DEFAULT_VIBRATE;
            notif.defaults |= Notification.FLAG_AUTO_CANCEL;

            return notif;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int generateRandomUniqueInt() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }

}
