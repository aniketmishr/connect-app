package com.example.connect.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.R
import com.example.connect.data.local.Contact
import com.example.connect.data.local.Note
import com.example.connect.data.repository.ContactRepository
import com.example.connect.data.repository.NoteRepository
import com.example.connect.ui.model.ContactModel
import com.example.connect.ui.model.toContact
import com.example.connect.ui.model.toContactModel
import com.example.connect.ui.presentation.EditContactDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    val contactRepository: ContactRepository,
    val noteRepository: NoteRepository
    ) : ViewModel(){

    val contactId:Int = checkNotNull(savedStateHandle[EditContactDestination.contactIdArg])

    var contactDetails: StateFlow<Contact> =
        contactRepository.getContactDetails(contactId)
            .filterNotNull() // to handle delete contact issue
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ContactModel().toContact()
            )
        private set

    var notes: StateFlow<List<Note>> =
        noteRepository.getAllNotes(contactId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = listOf<Note>()
            )
        private set

    fun getContactSocialMediaLinks(contact: Contact): Map<Int, String?> {
        val contact = contact.toContactModel()
        val map = mutableMapOf(
            R.drawable.instagram to if (contact.instagram == "") null else "https://www.instagram.com/${contact.instagram}/",
            R.drawable.linkedin to if (contact.linkedIn == "") null else "https://www.linkedin.com/in/${contact.linkedIn}/",
            R.drawable.facebook to if (contact.facebook == "") null else "https://www.facebook.com/${contact.facebook}/",
            R.drawable.youtube to if (contact.youtube == "" ) null else "https://youtube.com/@${contact.youtube}",
            R.drawable.twitter to if (contact.twitter == "") null else "https://twitter.com/${contact.twitter}",
            R.drawable.github to if (contact.github == "") null else "https://www.github.com/${contact.github}",
            R.drawable.hyperlink to if (contact.hyperlink == "") null else contact.hyperlink
        )
        return map

    }

    fun openBrowser(context: Context, url:String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch(e: Exception){
            Toast.makeText(context, "Browser Can't be opened", Toast.LENGTH_SHORT).show()
        }
    }

    fun enableCall(context: Context, phoneNumber:String){
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            intent.putExtra("videoCall",true)
            context.startActivity(intent)
        } catch(e: Exception){
            Toast.makeText(context, "Cannot Video Call", Toast.LENGTH_SHORT).show()
        }
    }

    fun enableVideoCall(context: Context, phoneNumber:String){
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            context.startActivity(intent)
        } catch(e: Exception){
            Toast.makeText(context, "Cannot Call", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(context: Context, phoneNumber: String){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
        }
        try {
            context.startActivity(intent)
        } catch(e: Exception){
            Toast.makeText(context, "Cannot Message", Toast.LENGTH_SHORT).show()
        }
    }

    fun makeContactFavourite(contact: Contact, value: Boolean){
        viewModelScope.launch {
            contactRepository.insert(contact.copy(favourite = value))
        }
    }

    fun getContactDetailsForDetailsTab(contact: Contact) : Map<String, String?> {
        val contact = contact.toContactModel()
        val map = mapOf(
            "Highlight" to contact.highlight,
            "Mobile Number" to contact.mobile,
            "Work Number" to contact.work,
            "Email" to contact.email,
            "Date Of Birth" to contact.dob,
            "College" to contact.college,
            "Address" to contact.address,
            "Workplace" to contact.workplace
        )
        return map.filterValues { it.isNotEmpty() }
    }

    fun getNoteCardRandomColor() : Color{
        val listColors = listOf<Color>(
            Color(207, 142, 142, 255),
            Color(216, 214, 116, 255),
            Color(170, 216, 153, 255),
            Color(149, 216, 187, 255),
            Color(161, 185, 221, 255),
            Color(203, 168, 224, 255),
            Color(224, 168, 208, 255)
        )

        return listColors.random()
    }

    fun deleteContact() {
        Log.d("Delete", contactId.toString())
        viewModelScope.launch {
            contactRepository.delete(contactId)
        }
    }
}

