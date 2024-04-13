package com.example.connect.data.local

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "contact")
data class Contact (
    @PrimaryKey(autoGenerate = true)
    val contactId: Int = 0,
    val photo: String?,  //null issue in all type-converter
    val favourite: Boolean = false,
    val name: String,
    val tag: String?,
    val highlight: String?,
    val mobile: Long?,
    val email: String?,
    val dob: Date?,
    val college: String?,
    val address: String?,
    val workplace: String?,
    val createdDate: Date?,
    val modifiedDate: Date?,
    //social links
    val linkedIn: String?,
    val facebook: String?,
    val youtube: String?,
    val twitter: String?,
    val instagram: String?,
    val github: String?,
    val hyperlink: String?
)

data class ContactData(
    val contactId: Int,
    val photo: String?,  //null issue in all type-converter
    val name: String,
    val tag: String?,
    val favourite: Boolean
)

@Entity(tableName = "tags")
data class Tags(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0 ,
    val contactId: Int,
    val title: String,
    val body: String,
    val createdDate: Date?,
    val modifiedDate: Date?
)
