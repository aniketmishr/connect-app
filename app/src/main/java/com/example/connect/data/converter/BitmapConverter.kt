package com.example.connect.data.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {

    @TypeConverter
    fun fromBitmapToByteArray(value: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100 , outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun fromByteArrayToBitmap(value: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(value, 0, value.size)
    }
}