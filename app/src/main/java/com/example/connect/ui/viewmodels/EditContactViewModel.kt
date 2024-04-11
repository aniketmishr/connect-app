package com.example.connect.ui.viewmodels

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.local.Contact
import com.example.connect.data.local.Tags
import com.example.connect.data.repository.ContactRepository
import com.example.connect.data.repository.TagsRepository
import com.example.connect.ui.model.ContactModel
import com.example.connect.ui.model.toContact
import com.example.connect.ui.model.toContactModel
import com.example.connect.ui.presentation.EditContactDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditContactViewModel(
    private val contactRepository: ContactRepository,
    private val tagsRepository: TagsRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    val contactId:Int = checkNotNull(savedStateHandle[EditContactDestination.contactIdArg])
    var editContactUiState by mutableStateOf(ContactModel())  // initialising Ui State
       private set      //read - only for other files

    var contactPhotoUri by mutableStateOf<Uri?>(null)  // Uri to be used before saving image to db
        private set

    val tagsList: StateFlow<List<Tags>> = tagsRepository.getAllTags()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf<Tags>()
        )


    init {
        Log.d("Save", "ContactID: ${contactId.toString()}")
        viewModelScope.launch {
            if (contactId != 0) {

                contactRepository.getContactDetails(contactId).collect{value ->
                    editContactUiState = value.toContactModel()
                    Log.d("Save", "Before Saving: ${editContactUiState.toContact().toString()}")
                }
            }
        }
    }

    fun updateUiState(newState: ContactModel) {
        Log.d("Debug", "Contact Add State updated")
        editContactUiState = newState
    }

    fun validateInput(): Boolean {
        return editContactUiState.name.isNotBlank()
    }



    suspend fun saveContact() {
        if (validateInput()) {
            Log.d("Save", "Before Saving: ${editContactUiState.toContact().toString()}")
            contactRepository.insert(editContactUiState.toContact())
        }

    }


    fun dateToString(date: Date): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }


    fun onPhotoPickerSelectionChanged(uri:Uri?, context: Context) {

        if (uri!=null) {
            contactPhotoUri = uri
        }
    }

    fun saveImageToInputStorage(context: Context) {
        val inputStream = contactPhotoUri?.let { context.contentResolver.openInputStream(it) }
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        inputStream?.use { input ->
            outputStream.use { output->
                input.copyTo(output)
            }
        }
        updateUiState(editContactUiState.copy(photo = fileName))
    }

}

