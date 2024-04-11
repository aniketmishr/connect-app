package com.example.connect

import com.example.connect.data.local.Contact
import com.example.connect.data.local.ContactData
import com.example.connect.data.local.Note
import java.util.Date

class Faker {

    fun getFavouriteContactList(): List<ContactData> {
        val favouriteContactList = listOf<ContactData>(
            ContactData(contactId = 1, photo = null, name = "Aniket Mishra", tag = null, favourite = true),
            ContactData(contactId = 2, photo = null, name = "Mukesh Agrrawal", tag = null, favourite = true),
            ContactData(contactId = 3, photo = null, name = "Himanshu Kumawat", tag = null, favourite = true),
            ContactData(contactId = 4, photo = null, name = "Rudra Raj Singh Sisodia Rajput from rajasthan LPS SChool", tag = null, favourite = true),
            ContactData(contactId = 5, photo = null, name = "Dmitri Lavasko Myshi", tag = null, favourite = true),
            ContactData(contactId = 6, photo = null, name = "Yana", tag = null, favourite = true),
        )
        return favouriteContactList
    }

    fun getUnfavouriteContactListMap(): Map<String, List<ContactData>>{
        val unfavouriteContactListMap = mapOf<String, List<ContactData>>(
            "A" to listOf<ContactData>(
                ContactData(contactId = 1, photo = null, name = "Aniket Mishra", tag = null, favourite = true),
                ContactData(contactId = 2, photo = null, name = "Anshu Sharma", tag = null, favourite = true),
                ContactData(contactId = 3, photo = null, name = "Agam Kumawat", tag = null, favourite = true),
                ContactData(contactId = 4, photo = null, name = "Anjana singh", tag = null, favourite = true)),
            "B" to listOf<ContactData>(
            ContactData(contactId = 5, photo = null, name = "Bhanu pratap Singh", tag = null, favourite = true),
            ContactData(contactId = 6, photo = null, name = "Barkha Salvi", tag = null, favourite = true),
            ContactData(contactId = 7, photo = null, name = "Binu Sharma", tag = null, favourite = true),
            ContactData(contactId = 8, photo = null, name = "Ben Stokes", tag = null, favourite = true)),
            "C" to listOf<ContactData>(
                ContactData(contactId = 9, photo = null, name = "Chetan Gupta", tag = null, favourite = true),
                ContactData(contactId = 10, photo = null, name = "Charvi Singhal", tag = null, favourite = true),
                ContactData(contactId = 11, photo = null, name = "Chetna Puri", tag = null, favourite = true)),
            "D" to listOf<ContactData>(
                ContactData(contactId = 12, photo = null, name = "Dhanu pratap Singh", tag = null, favourite = true),
                ContactData(contactId = 13, photo = null, name = "Darkha Salvi", tag = null, favourite = true),
                ContactData(contactId = 14, photo = null, name = "Dinu Sharma", tag = null, favourite = true),
                ContactData(contactId = 15, photo = null, name = "Den Stokes", tag = null, favourite = true)),
            "E" to listOf<ContactData>(
                ContactData(contactId = 16, photo = null, name = "Ehanu pratap Singh", tag = null, favourite = true),
                ContactData(contactId = 17, photo = null, name = "Earkha Salvi", tag = null, favourite = true),
                ContactData(contactId = 18, photo = null, name = "Einu Sharma", tag = null, favourite = true),
                ContactData(contactId = 19, photo = null, name = "Een Stokes", tag = null, favourite = true)),
            "F" to listOf<ContactData>(
                ContactData(contactId = 20, photo = null, name = "Fhanu pratap Singh", tag = null, favourite = true),
                ContactData(contactId = 21, photo = null, name = "Farkha Salvi", tag = null, favourite = true),
                ContactData(contactId = 22, photo = null, name = "Finu Sharma", tag = null, favourite = true),
                ContactData(contactId = 23, photo = null, name = "Fen Stokes", tag = null, favourite = true)),
            "B" to listOf<ContactData>(
                ContactData(contactId = 24, photo = null, name = "Fhanu pratap Singh", tag = null, favourite = true),
                ContactData(contactId = 25, photo = null, name = "Farkha Salvi", tag = null, favourite = true),
                ContactData(contactId = 26, photo = null, name = "Finu Sharma", tag = null, favourite = true),
                ContactData(contactId = 27, photo = null, name = "Fen Stokes", tag = null, favourite = true)),
            )

        return unfavouriteContactListMap
    }

    fun getContactDetialsInfo() :Contact {
        val contact = Contact(
            contactId = 1,
            name = "Aniket Mishra",
            photo = null,
            favourite = true,
            tag = "Family",
            highlight = "A Passionate Coder , Currently pursuing Bachleors in Computer Science Major",
            mobile = 9379979798,
            work = 838373838,
            email = "aniketmishra@gmail.com",
            dob = Date(),
            college = "CTAE",
            address = "Vishnu nagar Kankroil , Rajsamand",
            workplace = "Apple Silicon Valley",
            createdDate = null,
            modifiedDate = null,
            snapchat = "aniket",
            facebook = "aniket",
            linkedIn = "aniket",
            youtube = "aniket",
            twitter = "aniket",
            instagram = "aniket",
            messenger = "aniket",
            discord = "aniket",
            github = "aniket",
            hyperlink = "aniket"
        )
        return contact
    }

    fun getNotesOfContact(): List<Note> {
        val notes = listOf<Note>(
            Note(id = 1, contactId = 1, title = "Title 1 ",
                body = "The most common argument against this is that the explicit examination of product design and development becomes a serious problem. Remembering that criteria of the arrangement of the first-class package needs to be processed together with the The Program of Professional Capacity",
                createdDate = null, modifiedDate = null),
            Note(id = 2, contactId = 1, title = "Title 2 ",
                body = "The most common argument against this is that the explicit examination of product design and development becomes a serious problem. Remembering that criteria of the arrangement of the first-class package needs to be processed together with the The Program of Professional Capacity",
                createdDate = null, modifiedDate = null),
            Note(id = 3, contactId = 1, title = "Title 3 ",
                body = "The most common argument against this is that the explicit examination of product design and development becomes a serious problem. Remembering that criteria of the arrangement of the first-class package needs to be processed together with the The Program of Professional Capacity",
                createdDate = null, modifiedDate = null),
            Note(id = 4, contactId = 1, title = "Title 4 ",
                body = "The most common argument against this is that the explicit examination of product design and development becomes a serious problem. Remembering that criteria of the arrangement of the first-class package needs to be processed together with the The Program of Professional Capacity",
                createdDate = null, modifiedDate = null),
            Note(id = 5, contactId = 1, title = "Title 5 ",
                body = "The most common argument against this is that the explicit examination of product design and development becomes a serious problem. Remembering that criteria of the arrangement of the first-class package needs to be processed together with the The Program of Professional Capacity",
                createdDate = null, modifiedDate = null) )
        return notes

    }
}
