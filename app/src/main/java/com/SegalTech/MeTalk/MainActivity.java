package com.SegalTech.MeTalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MessageRecyclerUtils.MessageLongClickCallback {

    static final String MSG_BOX_KEY = "messageBox";
    static final String EMPTY_MSG_TOAST_TEXT = "But you didn't write anything :(";

    private MessageRecyclerUtils.MessageAdapter adapter = new
            MessageRecyclerUtils.MessageAdapter();

    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageViewModel = ViewModelProviders.of(this).
                get(MessageViewModel.class);

        messageViewModel.getAllMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.submitList(messages);
            }
        });

        // Configure RecyclerView
        final RecyclerView messageRecycler = findViewById(R.id.message_recycler);
        messageRecycler.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));
        messageRecycler.setAdapter(adapter);
        adapter.callback = this;
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
                    messageViewModel.insert(new Message(messageBox.getText().toString()));
//                    adapter.notifyDataSetChanged();
//                    messages.add(new Message(messageBox.getText().toString()));
//                    adapter.submitList(new ArrayList<>(messages));
                    messageBox.setText("");
                    messageBox.clearFocus();
                }
            }
        });

        // Retains RecyclerView and EditText contents on changed configuration
        if (savedInstanceState != null)
        {
            messageBox.setText(savedInstanceState.getCharSequence(MSG_BOX_KEY));
        }
    }

    @Override
    public void onLongClickMessage(final Message message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageViewModel.delete(message);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save edittext contents in case of OnDestroy
        EditText messageBox = findViewById(R.id.message_box);
        outState.putCharSequence(MSG_BOX_KEY, messageBox.getText().toString());
    }
}
