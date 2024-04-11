package com.example.connect.data.converter

import androidx.room.TypeConverter
import java.util.Date


class DateConverter {
    @TypeConverter
    fun fromDateToLong(value: Date) : Long{
        return value.time
    }
    @TypeConverter
    fun fromLongToDate(value: Long): Date {
        return Date(value)
    }
}