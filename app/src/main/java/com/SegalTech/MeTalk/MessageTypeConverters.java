package com.SegalTech.MeTalk;

import androidx.room.TypeConverter;

import java.util.Date;

public class MessageTypeConverters {

    @TypeConverter
    public static Date fromTimestamp(long value)
    {
        return new Date(value);
    }

    @TypeConverter
    public static long fromDate(Date date)
    {
        return date.getTime();
    }
}
