package com.example.connect.data.repository

import com.example.connect.data.local.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(noteId:Int)
    fun getAllNotes(contactId: Int) : Flow<List<Note>>
    fun getNote(id: Int) : Flow<Note>
}