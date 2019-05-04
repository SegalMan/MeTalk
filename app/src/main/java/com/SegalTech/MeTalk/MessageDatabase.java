package com.SegalTech.MeTalk;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
@TypeConverters(MessageTypeConverters.class)
public abstract class MessageDatabase extends RoomDatabase {

    // Message database implemented as singleton to avoid multiple instances of DB.
    private static MessageDatabase INSTANCE;
    public abstract MessageDao messageDao();

    static MessageDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (MessageDatabase.class) {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "message_databse").build();
                }
            }
        }
        return INSTANCE;
    }
}
