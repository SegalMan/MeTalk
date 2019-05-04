package com.SegalTech.MeTalk;

import androidx.room.TypeConverter;

import java.util.Date;

class MessageTypeConverters {

    @TypeConverter
    static Date fromTimestamp(long value)
    {
        return new Date(value);
    }

    @TypeConverter
    static long fromDate(Date date)
    {
        return date.getTime();
    }
}
