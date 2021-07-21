package ir.sharif.mobile.project.ui.repository;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.util.NotificationUtil;

public class NotificationManager {

    private Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void scheduleNotification(long delayMillis, int reminderId, String text) {
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(context, "TASK_MANAGEMENT");
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context, notificationChannelId)
                .setContentTitle("Task-management")
                .setContentText(text)
                .setSmallIcon(R.drawable.coin_logo)
                .setLargeIcon(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.coin_logo)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, (int) reminderId, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);
        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, reminderId);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) reminderId,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayMillis;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
