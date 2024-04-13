package com.example.connect.ui.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.connect.Faker
import com.example.connect.R
import com.example.connect.data.local.ContactData
import com.example.connect.data.local.Tags
import com.example.connect.ui.AppViewModelProvider
import com.example.connect.ui.navigation.NavigationDestination
import com.example.connect.ui.theme.ConnectTheme
import com.example.connect.ui.viewmodels.ContactListViewModel
import kotlinx.coroutines.launch
import java.io.File

object ContactListDestination: NavigationDestination {
    override val route = "contact_list"
}

@Composable
fun ContactListScreen(
    navigateToAddContact: (Int) -> Unit,
    navigateToContactDetails: (Int) -> Unit,
    navigateToSearch: () -> Unit,
    viewModel: ContactListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val _uiState = viewModel.uiState
    val tagsList  =viewModel.tagsList.collectAsState().value
    val tagsDropDown = viewModel.tagsDropDown
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope  = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState  = drawerState,
        drawerContent = { NavigationDrawerContent(viewModel::openGmailApp, viewModel::openBrowser,viewModel::shareApp)})
    {
        Scaffold(
            topBar = {
                ContactTopAppBar(modifier = Modifier
                    , onSearchClick = {navigateToSearch()}
                    , onMenuClick = {coroutineScope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }},
                    tagsList = tagsList,
                    tagsDropDown = tagsDropDown,
                    updateTagsDropDown = viewModel::updateTagsDropDown,
                    updateContactList = viewModel::updateContactListUiState
                )
            }
            ,
            floatingActionButton = {
                ContactFloatingActionButton {
                    navigateToAddContact(0)  // 0 for adding new contact
                }
            }
        )
        { contentPadding ->
            //Content
            Box( modifier = Modifier.padding(contentPadding)) {
                ContactListScreenBody(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(233, 236, 239)),
                    favouriteContactList = _uiState.favContactList,
                    unfavouiteContactListMap = _uiState.unFavContactListMap,
                    navigateToContactDetails = navigateToContactDetails
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContactListScreenBody(
    modifier: Modifier,
    favouriteContactList:List<ContactData>,
    unfavouiteContactListMap: Map<String,List<ContactData>>,
    navigateToContactDetails: (Int) -> Unit
    ) {

    if (favouriteContactList.isEmpty() && unfavouiteContactListMap.isEmpty()) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(233, 236, 239)),
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "No Data Found"
        )
    } else {
        // implementing new feature now
        LazyColumn(modifier = modifier) {
            if (favouriteContactList.isNotEmpty()){
                item{
                    FavouriteContactSection(modifier = Modifier, favouriteContactList, navigateToContactDetails = navigateToContactDetails)
                }
            }

            unfavouiteContactListMap.keys.forEach { category ->
                stickyHeader {
                    ContactHeader(modifier = Modifier, category = category)
                }
                items(unfavouiteContactListMap.getValue(category), key = {it.contactId}) { contact ->
                    UnfavouriteContactItem(contact = contact, navigateToContactDetails = navigateToContactDetails)
                }

            }


        }
    }
}

@Composable
private fun ContactHeader(modifier: Modifier.Companion, category: String) {
    Text(
        text = category,
        fontSize = 18.sp,
        color= Color(52,58,64),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(222, 226, 230))
            .padding(10.dp)
    )
}


@Composable
fun UnfavouriteContactItem(contact: ContactData, navigateToContactDetails: (Int) -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToContactDetails(contact.contactId) }
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .size(60.dp)
                .border(
                    width = 2.dp,
                    color = Color(52, 58, 64),
                    RoundedCornerShape(100.dp)
                )
                .clip(RoundedCornerShape(100.dp))
                .background(Color.LightGray)
                .padding(4.dp)
                .clip(RoundedCornerShape(100.dp)),
        ) {
            if (contact.photo == null) {
                Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Contact Profile", contentScale = ContentScale.Crop)
            } else {
                Image(painter = rememberImagePainter(data = File(context.filesDir,contact.photo)),
                    contentDescription = "Contact Profile" , contentScale = ContentScale.Crop)
            }
        }

        Spacer(modifier = Modifier.width(20.dp))
        Text(text = contact.name,
            fontSize = 20.sp,
            color = Color(79,79,79)
        )

    }
}

@Composable
private fun FavouriteContactSection(modifier:Modifier ,favouriteContactList:List<ContactData>,navigateToContactDetails: (Int) -> Unit) {
    Column(modifier = modifier) {

        ContactHeader(modifier = Modifier, category = "Favourite")
        //LazyRow

        LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item{
                    Spacer(modifier = Modifier.width(0.dp))
                }
                items(favouriteContactList, key = { it.contactId}) { contact ->
                    FavouriteContactItem(contact, navigateToContactDetails= navigateToContactDetails)
                }
                item{
                    Spacer(modifier = Modifier.width(0.dp))
                }
        }

    }
}

@Composable
private fun FavouriteContactItem(contact: ContactData,navigateToContactDetails: (Int) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .clickable { navigateToContactDetails(contact.contactId) }
    ) {
        val CARD_SIZE = 100.dp
        Card(
            modifier = Modifier
                .size(CARD_SIZE)
                .border(width = 2.dp, color = Color(52, 58, 64), RoundedCornerShape(100.dp))
                .clip(RoundedCornerShape(100.dp))
                .background(Color.LightGray)
                .padding(4.dp)
                .clip(RoundedCornerShape(100.dp)),
        ) {
            if (contact.photo == null) {
                Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Contact Profile", contentScale = ContentScale.Crop)
            } else {
                Image(painter = rememberImagePainter(data = File(context.filesDir,contact.photo)),
                    contentDescription = "Contact Profile" , contentScale = ContentScale.Crop)
            }
        }

        Spacer(modifier = Modifier
            .height(10.dp)
            .background(Color.Transparent))
        Text(text = contact.name,
            fontSize = 14.sp,
            color = Color(79,79,79),
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .background(Color.Transparent)
                .width(CARD_SIZE)
            )



    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private  fun ContactTopAppBar(
    modifier: Modifier,
    onSearchClick : () -> Unit,
    onMenuClick: () -> Unit,
    tagsList: List<Tags>,
    tagsDropDown: String,
    updateTagsDropDown: (String) -> Unit ,
    updateContactList: ()->Unit){

    TopAppBar(
        modifier = modifier,
        colors = topAppBarColors(
        containerColor = Color(222, 226, 230)
        ),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(modifier= Modifier
                    .size(35.dp, 35.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .padding(5.dp)
                    ,
                    painter = painterResource(id = R.drawable.menu)
                    , contentDescription = "Search",
                    tint = Color(52,58,64)
                )
            }
        },
        title = { Text(text = "")},
        actions = {
            ActionsForTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                onSearchClick = onSearchClick,
                tagsList = tagsList,
                tagsDropDown = tagsDropDown,
                updateTagsDropDown=updateTagsDropDown,
                updateContactList
            )
        }
        )
}

@Composable
private fun ActionsForTopAppBar(
    modifier: Modifier,
    onSearchClick : () -> Unit,
    tagsList: List<Tags>,
    tagsDropDown: String,
    updateTagsDropDown: (String) -> Unit,
    updateContactList: ()->Unit
) {
   Box(modifier = modifier)
   {
       Row(
           modifier = modifier,
           horizontalArrangement = Arrangement.Center,
           verticalAlignment = Alignment.CenterVertically
       ) {
           TagDropDownMenu(
               tagsList = tagsList,
               tagsDropDown = tagsDropDown,
               updateTagsDropDown = updateTagsDropDown,
               updateContactList
           )
       }

       Row(
           modifier = modifier,
           horizontalArrangement = Arrangement.End,
           verticalAlignment = Alignment.CenterVertically
       ) {

           IconButton(onClick = {onSearchClick()}) {
               Icon(modifier= Modifier
                   .size(35.dp, 35.dp)
                   .clip(RoundedCornerShape(100.dp))
                   .padding(5.dp)
                   ,
                   painter = painterResource(id = R.drawable.search)
                   , contentDescription = "Search",
                   tint = Color(52,58,64)
               )
           }


       }
   }
}

@Composable
private fun ContactFloatingActionButton(onFABClicked: () -> Unit) {
    FloatingActionButton(
        onClick = {
            Log.d("MainActivity", "FAB clicked")
                  onFABClicked()},
        modifier = Modifier
            .size(60.dp, 60.dp),
        shape = CircleShape,
        containerColor = Color(0xFF343A40)
        )
    {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Connection", tint = Color.White,
            modifier = Modifier
                .background(color = Color(0xFF343A40))
                .size(40.dp, 40.dp)
        )
    }
}


@Composable
private fun TagDropDownMenu(
    tagsList: List<Tags>,
    tagsDropDown: String,
    updateTagsDropDown: (String) -> Unit,
    updateContactList: ()->Unit
) {
    val menuItems = tagsList.toMutableList()
    menuItems.add(0, Tags(0, "All"))
    var expanded by remember { mutableStateOf(false) }

    //var selectedText by remember { mutableStateOf(menuItems[0].name) }
    Card(
        modifier = Modifier
            .border(width = 2.dp, color = Color(52, 58, 64), RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            .clickable { expanded = !expanded }
            .background(Color.Transparent)
            .padding(4.dp)
            .clip(RoundedCornerShape(50.dp))
            ,
        colors = CardDefaults.cardColors(containerColor = Color(52, 58, 64))
    ) {
        Row(modifier = Modifier.padding(
            start = 15.dp,
            end = 10.dp,
            top = 7.dp,
            bottom = 7.dp
        )
        ) {
            Text(
                text = tagsDropDown,
                color = Color(0xFFEBEBEB),
                //modifier = Modifier.width(IntrinsicSize.Min),
                maxLines = 1,
                )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.scale(scaleX = 1f,if (expanded) -1f else 1f))
            DropdownMenu(expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {

                menuItems.forEach { item ->
                    DropdownMenuItem(text = { Text(text = item.name)},
                        onClick = {
                            updateTagsDropDown(item.name)
                            updateContactList()
                            expanded = false

                        }
                    )
                }
            }
        }
    }

}

@Composable
fun NavigationDrawerContent(openGmailApp:(String,Context)->Unit,
                            openBrowser: (Context,String)-> Unit,
                            shareApp: (Context,String)-> Unit) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    ModalDrawerSheet(
        modifier = Modifier.requiredWidth(300.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
                .verticalScroll(scrollState,true),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .height(200.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = rememberImagePainter(
                    data = LocalContext.current.packageManager.getApplicationIcon("com.example.connect")),
                    contentDescription = "App Icon")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Connect", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "v1.0", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Text(text = "Check for Updates", fontSize = 16.sp, color = Color(0xFF2699F5),
                modifier = Modifier.clickable { openBrowser(context, "https://github.com/aniketmishr/connect-app/releases/latest") })

            Spacer(modifier = Modifier.height(10.dp))
            Divider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(label = { Text(text = "Developer")}, selected = false,
                onClick = {
                    openBrowser(context,"https://aniketmishra.co")
            }, icon = { Icon(
                painter = painterResource(id = R.drawable.programmer),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )})
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(label = { Text(text = "Report Bugs")}, selected = false, icon = { Icon(
                painter = painterResource(id = R.drawable.bug),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )},
                onClick = {
                openGmailApp("mailto:aniketmishra3476@gmail.com?subject=Connect: Report Bug",context)
            })
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(label = { Text(text = "Suggestion")}, selected = false,
                onClick = {
                openGmailApp("mailto:aniketmishra3476@gmail.com?subject=Connect: Suggestion",context)
                          }, icon = { Icon(
                painter = painterResource(id = R.drawable.suggestion),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )})
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(label = { Text(text = "Star Github Repo")}, selected = false,
                onClick = {
                          openBrowser(context,"https://github.com/aniketmishr/connect-app")
            }, icon = { Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )})
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(label = { Text(text = "Share App")}, selected = false,
                onClick = {
                          shareApp(context, "https://github.com/aniketmishr/connect-app?tab=readme-ov-file#installation")
                          }, icon = { Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )})
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun ScreenPreview() {
    ConnectTheme {
        ContactListScreenBody(
            modifier = Modifier,
            favouriteContactList = Faker().getFavouriteContactList(),
            unfavouiteContactListMap = Faker().getUnfavouriteContactListMap(),
            navigateToContactDetails = {}
        )
    }
}