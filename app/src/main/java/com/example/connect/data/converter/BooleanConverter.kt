package com.example.connect.data.converter

import androidx.room.TypeConverter

class BooleanConverter {
    @TypeConverter
    fun fromBooleanToInt(value: Boolean): Int{
        return if (value){
            1
        } else {
            0
        }
    }
    @TypeConverter
    fun fromIntToBoolean(value: Int) : Boolean {
        return value == 1
    }
}