package com.example.connect.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connect.data.local.ContactData
import com.example.connect.data.repository.ContactRepository
import kotlinx.coroutines.launch

class SearchContactViewModel(
    private val contactRepository: ContactRepository
): ViewModel() {
    var searchText by mutableStateOf("")
    var filteredContactList by mutableStateOf(listOf<ContactData>())

    fun updateSearchText(newText:String){
        searchText = newText
    }

    fun updateFilteredContactList(){
        viewModelScope.launch {
            contactRepository.getFilteredContacts(filter = searchText).collect{value->
                filteredContactList = value
            }
        }
    }
}