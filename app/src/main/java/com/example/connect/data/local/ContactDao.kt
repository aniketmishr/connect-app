package com.example.connect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("select contactId, name, photo,tag,favourite from contact order by name collate NOCASE asc")
    fun getAllContacts() : Flow<List<ContactData>>

    @Query("select contactId, name, photo,tag,favourite from contact where name like '%' || :filter || '%' order by name collate NOCASE asc")
    fun getFilteredContacts(filter:String) : Flow<List<ContactData>>

    @Query("select contactId, name, photo,tag, favourite from contact where favourite = 1 order by name asc")
    fun getFavouriteContact() : Flow<List<ContactData>>

    @Query("select contactId, name, photo,tag, favourite from contact where favourite = 0 order by name asc")
    fun getUnFavouriteContact() : Flow<List<ContactData>>

    @Query("select contactId, name, photo,tag,favourite from contact where favourite = 1 and tag = :tag order by name asc")
    fun getFavouriteContactWithTag(tag: String) : Flow<List<ContactData>>

    @Query("select contactId, name, photo,tag,favourite from contact where favourite = 0 and tag = :tag  order by name asc")
    fun getUnFavouriteContactWithTag(tag: String) : Flow<List<ContactData>>

    @Query("select * from contact where contactId = :id")
    fun getContactDetails(id: Int) : Flow<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Query("DELETE FROM contact WHERE contactId = :contactId")
    suspend fun delete(contactId: Int)
}