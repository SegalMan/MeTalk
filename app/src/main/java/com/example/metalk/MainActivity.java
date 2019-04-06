package com.example.metalk;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static final String CURRENT_MSG_KEY = "currentMessage";
    static final String MSG_BOX_KEY = "messageBox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView currentMessage = (TextView)findViewById(R.id.textView);
        Button sendButton = (Button)findViewById(R.id.button);
        final EditText messageBox = (EditText) findViewById(R.id.editText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMessage.setText(messageBox.getText());
                messageBox.setText("");
            }
        });

        if (savedInstanceState != null)
        {
            currentMessage.setText(savedInstanceState.getCharSequence(CURRENT_MSG_KEY));
            messageBox.setText(savedInstanceState.getCharSequence(MSG_BOX_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView currentMessage = (TextView)findViewById(R.id.textView);
        outState.putCharSequence(CURRENT_MSG_KEY, currentMessage.getText().toString());

        EditText messageBox = (EditText)findViewById(R.id.editText);
        outState.putCharSequence(MSG_BOX_KEY, messageBox.getText().toString());
    }
}
