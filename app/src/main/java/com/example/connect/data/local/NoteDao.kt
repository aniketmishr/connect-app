package com.example.connect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Query("DELETE FROM note WHERE Id = :noteId")
    suspend fun delete(noteId:Int)
    @Query("select * from note where contactId = :contactId order by id desc")
    fun getAllNotes(contactId: Int) : Flow<List<Note>>
    @Query("select * from note where id = :id")
    fun getNote(id: Int) : Flow<Note>
}