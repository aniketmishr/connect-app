package com.example.connect.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.connect.ConnectApplication
import com.example.connect.ui.viewmodels.ContactDetailsViewModel
import com.example.connect.ui.viewmodels.ContactListViewModel
import com.example.connect.ui.viewmodels.EditContactViewModel
import com.example.connect.ui.viewmodels.EditNoteViewModel
import com.example.connect.ui.viewmodels.SearchContactViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
        //initializer for ContactListViewModel
        initializer {
            ContactListViewModel(
                connectApplication().container.contactRepository,
                connectApplication().container.tagsRepository
            )
        }

        //initializer for ContactDetailViewModel
        initializer {
            ContactDetailsViewModel(
                this.createSavedStateHandle(),
                connectApplication().container.contactRepository,
                connectApplication().container.noteRepository
            )
        }

        //initializer for EditContactViewModel
        initializer {
            EditContactViewModel(
                connectApplication().container.contactRepository,
                connectApplication().container.tagsRepository,
                this.createSavedStateHandle()
            )
        }

        //initializer for EditNoteViewModel
        initializer {
            EditNoteViewModel(
                this.createSavedStateHandle(),
                connectApplication().container.noteRepository

            )
        }

        //initializer for ContactListViewModel
        initializer {
            SearchContactViewModel(
                connectApplication().container.contactRepository
            )
        }

    }
}


fun CreationExtras.connectApplication(): ConnectApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ConnectApplication)