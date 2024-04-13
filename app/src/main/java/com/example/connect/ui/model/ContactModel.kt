package com.example.connect.ui.model

import com.example.connect.data.local.Contact
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ContactModel(
    val contactId:Int = 0,
    val favourite: Boolean = false,
    val photo: String? = null,  //null issue in all type-converter
    val name: String = "",
    val tag: String = "No Tag",
    val highlight: String = "",
    val mobile: String = "",
    val email: String = "",
    val dob: String = "",
    val college: String = "",
    val address: String = "",
    val workplace: String ="",
    //social links
    val linkedIn: String = "",
    val facebook: String = "",
    val youtube: String = "",
    val twitter: String = "",
    val instagram: String = "",
    val github: String = "",
    val hyperlink: String = ""
)

fun ContactModel.toContact(): Contact = Contact(
    contactId = contactId,
    favourite = favourite,
    photo = photo, //may be issue arise
    name = name,
    tag = tag , //may be issue arise
    highlight = if (highlight.isBlank()) {null} else {highlight},
    mobile = mobile.toLongOrNull(),
    email = if (email.isBlank()) {null} else {email},
    dob = if (dob.isBlank()) {null} else {
        stringToDate(dob)
    },//may be issue arise
    college = if (college.isBlank()) {null} else {college},
    address = if (address.isBlank()) {null} else {address},
    workplace = if (workplace.isBlank()) {null} else {workplace},
    createdDate = Date(), //may be issue arrise
    modifiedDate = null,
    linkedIn = if (linkedIn.isBlank()) {null} else {linkedIn},
    facebook = if (facebook.isBlank()) {null} else {facebook},
    youtube = if (youtube.isBlank()) {null} else {youtube},
    twitter = if (twitter.isBlank()) {null} else {twitter},
    instagram = if (instagram.isBlank()) {null} else {instagram},
    github = if (github.isBlank()) {null} else {github},
    hyperlink = if (hyperlink.isBlank()) {null} else {hyperlink}
)

fun stringToDate(dateStr: String): Date? {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.parse(dateStr)
}

fun dateToString(date: Date): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(date)
}

fun Contact.toContactModel() : ContactModel = ContactModel(
    contactId = contactId,
    favourite = favourite,
    photo = photo,
    name = name,
    tag = tag?: "No Tag",
    highlight = highlight?: "",
    mobile = mobile?.toString() ?: "",
    email = email?: "",
    dob = if (dob==null) "" else dateToString(dob),
    college = college?: "",
    address = address?: "",
    workplace = workplace?: "",
    linkedIn = linkedIn?:"",
    facebook = facebook?: "",
    youtube = youtube?: "",
    twitter = twitter?:"",
    instagram =instagram?: "",
    github = github?:"",
    hyperlink = hyperlink?: ""
)