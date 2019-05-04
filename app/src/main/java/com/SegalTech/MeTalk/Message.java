package com.SegalTech.MeTalk;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "messages")
class Message {

    @PrimaryKey(autoGenerate = true)
    int messageId = 0;

    @ColumnInfo(name = "message_text")
    String messageText;

    @ColumnInfo(name = "timestamp")
    Date messageTime;

    Message(String messageText)
    {
        this.messageText = messageText;
        this.messageTime = Calendar.getInstance().getTime();
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

        return this.messageId == ((Message) obj).messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageText);
    }
}