package com.example.connect.ui.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.connect.data.local.ContactData
import com.example.connect.ui.AppViewModelProvider
import com.example.connect.ui.navigation.NavigationDestination
import com.example.connect.ui.viewmodels.SearchContactViewModel

object SearchContactDestination: NavigationDestination {
    override val route = "search_contacts"
}

@Composable
fun SearchContactScreen(
    viewModel: SearchContactViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToContactDetails: (Int)->Unit,
    onNavigateBackClick:()->Unit
) {
    Scaffold(
        topBar = {
            ContactTopAppBar(
                modifier = Modifier
                , onSearchClick = { /*TODO*/ },
                updateSearchText = viewModel::updateSearchText,
                searchText = viewModel.searchText,
                updateFilteredContactList = viewModel::updateFilteredContactList,
                onNavigateBackClick = onNavigateBackClick
            )
        }
        
    )
    { contentPadding ->
        //Content
        Box( modifier = Modifier.padding(contentPadding)) {
            ContactSearchScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(233, 236, 239)),
                filteredContactData = viewModel.filteredContactList,
                navigateToContactDetails = navigateToContactDetails
            )
        }
    }
}

@Composable
fun ContactSearchScreenBody(
    modifier: Modifier,
    filteredContactData: List<ContactData>,
    navigateToContactDetails: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ){
        items(filteredContactData) { contact ->
            UnfavouriteContactItem(contact = contact, navigateToContactDetails = navigateToContactDetails)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private  fun ContactTopAppBar(
    modifier: Modifier,
    onSearchClick : () -> Unit,
    searchText:String,
    updateSearchText: (String)->Unit,
    updateFilteredContactList: ()->Unit,
    onNavigateBackClick: () -> Unit){

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(222, 226, 230)
        ),
        title = { Text(text = "") },
        actions = {
            ActionsForTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                onSearchClick = onSearchClick,
                updateFilteredContactList = updateFilteredContactList,
                searchText = searchText,
                updateSearchText = updateSearchText,
                onNavigateBackClick = onNavigateBackClick
            )
        }
    )
}

@Composable
private fun ActionsForTopAppBar(
    modifier: Modifier,
    onSearchClick : () -> Unit,
    searchText: String,
    updateSearchText: (String)->Unit,
    updateFilteredContactList: ()->Unit,
    onNavigateBackClick: () -> Unit
) {
    Box(modifier = modifier)
    {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {onNavigateBackClick()}) {
                Image(modifier= Modifier
                    .size(50.dp, 50.dp),
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Navigate Back"
                )
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp))

            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        Log.d("Search", "text->$it")
                        updateSearchText(it)
                        updateFilteredContactList()
                                    },
                    modifier = Modifier
                        .background(Color(230, 237, 240))
                        .padding(top = 7.dp, bottom = 7.dp, start = 20.dp, end = 20.dp),
                    textStyle = TextStyle(background = Color.Transparent,
                        fontSize = 20.sp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
}