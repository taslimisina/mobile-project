package ir.sharif.mobile.simple_task_management.repository;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_TEXT = "notification_text";

    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        String notification_text = intent.getStringExtra(NOTIFICATION_TEXT);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notificationId, notification);
        RepositoryHolder.getNotificationManager().scheduleNotification(24*60*60*1000, notificationId,
                notification_text);
    }
}
