package com.example.metalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

import static com.example.metalk.Message.messages;

public class MainActivity extends AppCompatActivity {

    static final String MSG_BOX_KEY = "messageBox";
    static final String EMPTY_MSG_TOAST_TEXT = "But you didn't write anything :(";

    private MessageRecyclerUtils.MessageAdapter adapter = new
            MessageRecyclerUtils.MessageAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure RecyclerView
        final RecyclerView messageRecycler = findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        messageRecycler.setAdapter(adapter);
        messageRecycler.setItemAnimator(new DefaultItemAnimator());

        final Button sendButton = findViewById(R.id.send_button);
        final EditText messageBox = findViewById(R.id.message_box);

        // Sets button onClickListener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Shows empty message error via Toast
                if (messageBox.getText().toString().isEmpty())
                {
                    Toast t = Toast.makeText(getApplicationContext(), EMPTY_MSG_TOAST_TEXT,
                            Toast.LENGTH_SHORT);

                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }

                // Re-submits message list with new message
                else
                {
                    messages.add(new Message(messageBox.getText().toString()));
                    adapter.submitList(new ArrayList<>(messages));
                    messageBox.setText("");
                    messageBox.clearFocus();
                }
            }
        });

        // Retains RecyclerView and EditText contents on changed configuration
        if (savedInstanceState != null)
        {
            adapter.submitList(messages);
            messageBox.setText(savedInstanceState.getCharSequence(MSG_BOX_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save edittext contents in case of OnDestroy
        EditText messageBox = findViewById(R.id.message_box);
        outState.putCharSequence(MSG_BOX_KEY, messageBox.getText().toString());
    }
}
