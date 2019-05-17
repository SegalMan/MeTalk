package com.SegalTech.MeTalk;
import android.app.Application;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeTalk extends Application {

    final String LOG_TAG = "Application";
    public MessageViewModel messageViewModel;

    @Override
    public void onCreate() {
        super.onCreate();

        messageViewModel = new MessageViewModel(this);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int count = messageViewModel.count();
                if (count > 0)
                {
                    Log.i(LOG_TAG, "Local DB contains " +
                            messageViewModel.count() + " messages");
                }
                else
                {
                    Log.i(LOG_TAG, "Local DB is empty.");
                }
            }
        });

        executor.shutdown();
    }
}
