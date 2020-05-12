package com.dcinspirations.aff;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BaseApp extends Application {

    public static final String CHANNEL_1_ID= "channel1";
    public static final String CHANNEL_2_ID= "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel gc = new NotificationChannel(
                    CHANNEL_1_ID,
                    "aff_channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            gc.setDescription("AFF channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(gc);
        }
    }
}
