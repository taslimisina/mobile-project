package ir.sharif.mobile.project;

import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ir.sharif.mobile.project.ui.repository.RepositoryHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RepositoryHolder.init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Task-management")
                .setContentText("text")
                .setSmallIcon(R.drawable.coin_logo)
                .setLargeIcon(((BitmapDrawable) getApplicationContext().getResources()
                        .getDrawable(R.drawable.coin_logo)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1165, builder.build());
        Log.i("isEnable?", String.valueOf(notificationManager.areNotificationsEnabled()));
    }
}