package com.example.connect.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.local.Contact
import com.example.connect.data.local.ContactData
import com.example.connect.data.local.Tags
import com.example.connect.data.repository.ContactRepository
import com.example.connect.data.repository.TagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val contactRepository: ContactRepository,
    private val tagsRepository: TagsRepository
): ViewModel() {

    var contacts : List<ContactData> = listOf()
        private set

    var tagsList : StateFlow<List<Tags>> =
        tagsRepository.getAllTags()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                emptyList()
            )
        private set

    var tagsDropDown by mutableStateOf("All")
        private set

    var uiState by mutableStateOf(ContactListUiState())

    init {
        viewModelScope.launch {
            contactRepository.getAllContacts().collect{value->
                contacts = value
                updateContactListUiState()
            }
        }
    }

    fun updateContactListUiState() {
        uiState = ContactListUiState(
            favContactList = applyTagConstraintToFavContactList(tagsDropDown, getFavContacts(contacts)),
            unFavContactListMap = applyTagConstraintToUnFavContactListMap(tagsDropDown,getUnFavContactListMap(contacts))
        )
    }

    fun updateTagsDropDown(newTag: String) {
        tagsDropDown = newTag
    }

    private fun getFavContacts(contactList: List<ContactData>): List<ContactData>{
        val favContactList = mutableListOf<ContactData>()
        contactList.forEach{ contact ->
            if (contact.favourite) {
                favContactList.add(contact)
            }
        }

        Log.d("ViewModel", favContactList.toString())
        return favContactList
    }

    private fun applyTagConstraintToFavContactList(tag: String, contactsList: List<ContactData>) : List<ContactData> {
        val favContactList = mutableListOf<ContactData>()
        if (tag == "All") {
            return contactsList
        } else {
            contactsList.forEach{ contact ->
                if (contact.tag == tag) {
                    favContactList.add(contact)
                }
            }
        }
        Log.d("ViewModel", "COntacts ${contactsList.toString()}")
        Log.d("ViewModel", favContactList.toString())
        return favContactList
    }

    private fun getUnFavContactListMap(contactList: List<ContactData>): Map<String,List<ContactData>> {
        val contactsMap = mutableMapOf<String,MutableList<ContactData>>()
        contactList.forEach{contact ->
            if (!contact.favourite) {
                val firstChar = contact.name.first().uppercaseChar().toString()
                if (!contactsMap.containsKey(firstChar)) {
                    contactsMap[firstChar] = mutableListOf()
                }
                contactsMap[firstChar]?.add(contact)
            }

        }
        Log.d("ViewModel", contactsMap.toString())
        return contactsMap.filterNot { it.value.isEmpty() }.toSortedMap()
    }

    private fun applyTagConstraintToUnFavContactListMap(tag: String,contactsMap: Map<String,List<ContactData>>) : Map<String,List<ContactData>> {
        if (tag == "All") {
            return contactsMap
        } else {
            val filteredMap = contactsMap.mapValues { (_,list) ->
                list.filter { it.tag == tag }
            }
            Log.d("ViewModel", filteredMap.toString())
            return filteredMap.filterNot { it.value.isEmpty() }.toSortedMap() //TODO("use algorithm")

        }
    }

    fun openGmailApp(uri: String,context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uri)

        val activities = context.packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY)
        if (activities.isNotEmpty()){
            context.startActivity(intent)
        } else{
            Toast.makeText(context,"Gmail is not installed ", Toast.LENGTH_SHORT).show()
        }
    }

    fun openBrowser(context: Context,url:String){
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch(e: Exception){
            Toast.makeText(context, "Browser Can't be opened",Toast.LENGTH_SHORT).show()
        }
    }

    fun shareApp(context: Context, msg:String){
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,msg)
        }
        val chooserIntent = Intent.createChooser(shareIntent,null)
        context.startActivity(shareIntent)
    }

}

data class ContactListUiState(
    val favContactList: List<ContactData> = emptyList(),
    val unFavContactListMap: Map<String,List<ContactData>> = emptyMap(),

)

