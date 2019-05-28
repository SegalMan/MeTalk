package com.SegalTech.MeTalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final String ORIGIN_KEY = "Origin";
    static final String MESSAGE_NAV_ARG_KEY = "message";
    static final String MESSAGE_FROM_TODAY_FORMAT = "HH:mm";
    static final String MESSAGE_FROM_BEFORE_FORMAT = "dd/MM/yyyy HH:mm";
    static final String MSG_BOX_KEY = "messageBox";

    private MessageRecyclerUtils.MessageAdapter adapter = new
            MessageRecyclerUtils.MessageAdapter();
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Save UUID in sp if doesn't exist yet
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String origin = sp.getString(ORIGIN_KEY, null);
        if (origin == null)
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(ORIGIN_KEY, UUID.randomUUID().toString());
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0)
        {
            super.onBackPressed();
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
    }

    MessageRecyclerUtils.MessageAdapter getAdapter()
    {
        return this.adapter;
    }

    void setUsername(String username)
    {
        this.username = username;
    }

    String getUsername()
    {
        return this.username;
    }
}
