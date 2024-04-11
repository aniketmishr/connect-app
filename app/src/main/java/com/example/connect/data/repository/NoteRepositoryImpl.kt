package com.example.connect.data.repository

import com.example.connect.data.local.Note
import com.example.connect.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun insert(note: Note) = noteDao.insert(note)

    override suspend fun update(note: Note) = noteDao.update(note)

    override suspend fun delete(noteId:Int) = noteDao.delete(noteId)

    override fun getAllNotes(contactId: Int): Flow<List<Note>> = noteDao.getAllNotes(contactId)

    override fun getNote(id: Int): Flow<Note> = noteDao.getNote(id)

}