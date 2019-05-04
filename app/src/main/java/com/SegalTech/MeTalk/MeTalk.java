package com.SegalTech.MeTalk;

import android.app.Application;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeTalk extends Application {

    final String LOG_TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "Current count is " +
                        MessageDatabase.getDatabase(getApplicationContext()).
                                messageDao().count() + " messages");
            }
        });

        executor.shutdown();
    }
}
