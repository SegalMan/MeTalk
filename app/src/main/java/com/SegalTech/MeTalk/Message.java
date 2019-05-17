package com.SegalTech.MeTalk;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "messages")
class Message {

    final static String FIREBASE_MESSAGE_TEXT_FIELD_NAME = "messageText";
    final static String FIREBASE_MESSAGE_TIME_FIELD_NAME = "messageTime";

    @PrimaryKey
    long messageId;

    @ColumnInfo(name = "message_text")
    String messageText;

    @ColumnInfo(name = "timestamp")
    Date messageTime;

    public Message(String messageText)
    {
        this.messageText = messageText;
        this.messageTime = Calendar.getInstance().getTime();
        this.messageId = messageTime.getTime();
    }

    @Ignore
    public Message(String firebaseId, Map<String, Object> values)
    {
        this.messageText = (String)values.get(FIREBASE_MESSAGE_TEXT_FIELD_NAME);
        this.messageTime = ((Timestamp)values.get(FIREBASE_MESSAGE_TIME_FIELD_NAME)).toDate();
        this.messageId = Long.parseLong(firebaseId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || this.getClass() != obj.getClass())
        {
            return false;
        }

        Message m = (Message) obj;

        return ((this.messageId == m.messageId) && (Objects.equals(this.messageTime, m.messageTime))
                && (Objects.equals(this.messageText, m.messageText)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageText);
    }
}