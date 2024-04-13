package com.example.connect.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.local.Note
import com.example.connect.data.repository.NoteRepository
import com.example.connect.ui.presentation.EditNoteDestination
import kotlinx.coroutines.launch
import java.util.Date

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    val noteRepository: NoteRepository
) : ViewModel(){

    val noteId:Int = checkNotNull(savedStateHandle[EditNoteDestination.noteIdArg])?:0
    val contactId:Int = checkNotNull(savedStateHandle[EditNoteDestination.contactIdArg])
    var uiState by mutableStateOf(EditNoteUiState())
        private set

    init{
        if (noteId != 0 ){
            viewModelScope.launch {
                noteRepository.getNote(noteId).collect{value->
                    if (value != null) {     // checks if the value is not null, when we delete the note -> do not update the state
                        uiState = EditNoteUiState(
                            title = value.title,
                            body = value.body
                        )
                    }

                }
            }
        }
    }
    suspend fun saveNote(){
        if (validateInput()){
            noteRepository.insert(Note(id = noteId,contactId = contactId, title = uiState.title, body = uiState.body, createdDate = Date(), modifiedDate = null))
        }
    }

    fun validateInput(): Boolean{
        return uiState.title.isNotBlank()
    }

    fun updateUiState(newState: EditNoteUiState){
        uiState = newState
    }

    fun onDeleteNote(){
        viewModelScope.launch{
            Log.d("note", noteId.toString())
            noteRepository.delete(noteId = noteId)
        }
    }
}

data class EditNoteUiState(
    val title: String = "Title",
    val body: String = "Body"
)