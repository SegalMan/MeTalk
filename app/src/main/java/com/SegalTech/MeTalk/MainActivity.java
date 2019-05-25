package com.SegalTech.MeTalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final String ORIGIN_KEY = "Origin";
    static final String MESSAGE_NAV_ARG_KEY = "message";
    static final String MESSAGE_FROM_TODAY_FORMAT = "HH:mm";
    static final String MESSAGE_FROM_BEFORE_FORMAT = "dd/MM/yyyy HH:mm";
    static final String MSG_BOX_KEY = "messageBox";

    public MessageRecyclerUtils.MessageAdapter adapter = new
            MessageRecyclerUtils.MessageAdapter();
    public String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MeTalk application = (MeTalk) getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application.messageViewModel.getAllMessages().observe(this,
                new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.submitList(messages);
            }
        });

        application.messageViewModel.insertAllMessages(application.messageViewModel.getAllMessages().
                getValue());

        // Save UUID in sp if doesn't exist yet
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String origin = sp.getString(ORIGIN_KEY, null);
        if (origin == null)
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(ORIGIN_KEY, UUID.randomUUID().toString());
            editor.apply();
        }

        NavHostFragment host = NavHostFragment.create(R.navigation.nav_graph);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.container, host)
                .setPrimaryNavigationFragment(host).commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save edittext contents in case of OnDestroy
        EditText messageBox = findViewById(R.id.message_box);
        outState.putCharSequence(MSG_BOX_KEY, messageBox.getText().toString());
    }
}
