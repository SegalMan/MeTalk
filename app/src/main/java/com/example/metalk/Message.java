package com.example.metalk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

class Message {
    final String messageText;
    final String messageTime;
    static ArrayList<Message> messages = new ArrayList<>();

    public Message(String messageText)
    {
        this.messageText = messageText;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        this.messageTime = dateFormatter.format(Calendar.getInstance().getTime());
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || this.getClass() != obj.getClass())
        {
            return false;
        }

        if (this == obj)
        {
            return true;
        }

        Message m = (Message) obj;

        return this.messageText.equals(m.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageText);
    }
}
