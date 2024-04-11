package com.example.connect.data.repository

import com.example.connect.data.local.Contact
import com.example.connect.data.local.ContactDao
import com.example.connect.data.local.ContactData
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(private val contactDao: ContactDao) : ContactRepository {
    override fun getAllContacts(): Flow<List<ContactData>> = contactDao.getAllContacts()

    override fun getFilteredContacts(filter:String): Flow<List<ContactData>> = contactDao.getFilteredContacts(filter)

    override fun getFavouriteContact(): Flow<List<ContactData>> = contactDao.getUnFavouriteContact()

    override fun getUnFavouriteContact(): Flow<List<ContactData>> = contactDao.getUnFavouriteContact()

    override fun getFavouriteContactWithTag(tag: String): Flow<List<ContactData>> = contactDao.getFavouriteContactWithTag(tag)

    override fun getUnFavouriteContactWithTag(tag: String): Flow<List<ContactData>> = contactDao.getUnFavouriteContactWithTag(tag)

    override fun getContactDetails(id: Int): Flow<Contact> = contactDao.getContactDetails(id)

    override suspend fun insert(contact: Contact) = contactDao.insert(contact)

    override suspend fun update(contact: Contact) = contactDao.update(contact)

    override suspend fun delete(contactId: Int) = contactDao.delete(contactId)
}