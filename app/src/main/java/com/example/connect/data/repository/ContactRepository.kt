package com.example.connect.data.repository

import com.example.connect.data.local.Contact
import com.example.connect.data.local.ContactData
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAllContacts() : Flow<List<ContactData>>
    fun getFilteredContacts(filter:String): Flow<List<ContactData>>
    fun getFavouriteContact() : Flow<List<ContactData>>
    fun getUnFavouriteContact() : Flow<List<ContactData>>
    fun getFavouriteContactWithTag(tag: String) : Flow<List<ContactData>>
    fun getUnFavouriteContactWithTag(tag: String) : Flow<List<ContactData>>
    fun getContactDetails(id: Int) : Flow<Contact>
    suspend fun insert(contact: Contact)
    suspend fun update(contact: Contact)
    suspend fun delete(contactId: Int)


}