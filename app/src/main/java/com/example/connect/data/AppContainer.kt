package com.example.connect.data

import android.content.Context
import com.example.connect.data.local.AppDatabase
import com.example.connect.data.repository.ContactRepository
import com.example.connect.data.repository.ContactRepositoryImpl
import com.example.connect.data.repository.NoteRepository
import com.example.connect.data.repository.NoteRepositoryImpl
import com.example.connect.data.repository.TagsRepository
import com.example.connect.data.repository.TagsRepositoryImpl

interface Container {
    val contactRepository: ContactRepository
    val noteRepository: NoteRepository
    val tagsRepository: TagsRepository
}

class AppContainer(context: Context) : Container {

    val databaseInstance by lazy { AppDatabase.getDatabase(context) }
    override val contactRepository: ContactRepository = ContactRepositoryImpl(databaseInstance.contactDao())
    override val noteRepository: NoteRepository = NoteRepositoryImpl(databaseInstance.noteDao())
    override val tagsRepository: TagsRepository = TagsRepositoryImpl(databaseInstance.tagsDao())

}