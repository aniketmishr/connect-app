package com.example.connect.ui.presentation


import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.connect.R
import com.example.connect.data.local.Contact
import com.example.connect.data.local.Note
import com.example.connect.ui.AppViewModelProvider
import com.example.connect.ui.navigation.NavigationDestination
import com.example.connect.ui.viewmodels.ContactDetailsViewModel
import java.io.File

object ContactDetailsDestination: NavigationDestination {
    override val route = "contact_details"
    val contactIdArg = "contactId"
    val routeWithArgs = "${ContactDetailsDestination.route}/{$contactIdArg}"
}

@Composable
fun ContactDetailsScreen(
    onNavigateBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: ContactDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNewNoteClick: (Int)-> Unit,
    onNoteClicked:(Int,Int)-> Unit
) {
    val contactDetails = viewModel.contactDetails.collectAsState().value
    val notes = viewModel.notes.collectAsState().value
    var tabId by remember { mutableStateOf(1) } //TODO("move this to viewmodel")


    Scaffold(
        topBar = {
            ContactTopAppBar(
                modifier = Modifier,
                onNavigateBackClick = onNavigateBackClick,
                onHeartClick = {
                    viewModel.makeContactFavourite(
                        contactDetails,
                        !contactDetails.favourite
                    )
                },
                onDeleteClick = viewModel::deleteContact,
                onEditClick = { onEditClick(contactDetails.contactId) },
                contactDetails = contactDetails
            )
        }
    )
    { contentPadding ->
        //Content
        Box(modifier = Modifier.padding(contentPadding)) {
            ContactDetailsScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(233, 236, 239)),
                contactDetails = contactDetails,
                notes = notes,
                viewModel,
                openNotesTab = { tabId = 2 },
                openDetailsTab = { tabId = 1 },
                tabId = tabId,
                onNewNoteClick = onNewNoteClick,
                onNoteClicked = onNoteClicked
            )
        }
    }
}

@Composable
fun ContactDetailsScreenBody(modifier: Modifier,
                             contactDetails: Contact,
                             notes: List<Note>,viewmodel: ContactDetailsViewModel,
                             openNotesTab : () -> Unit, // TODO("Remove aferr moving tabId to viewmodel")
                             openDetailsTab : () -> Unit, // TODO("Remove aferr moving tabId to viewmodel")
                             tabId : Int,// TODO("Remove after moving tabId to viewmodel")
                             onNewNoteClick: (Int) -> Unit,
                             onNoteClicked:(Int,Int)-> Unit
                             ) {
    val socialMediaLinks = viewmodel.getContactSocialMediaLinks(contactDetails)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Spacer(modifier = Modifier.height(30.dp))
        ProfileView(contact = contactDetails)
        ContactActions(
            phoneNumber = contactDetails.mobile,
            enableCall = viewmodel::enableCall,
            enableVideoCall = viewmodel::enableVideoCall,
            sendMessage = viewmodel::sendMessage
            )
        if (socialMediaLinks.isNotEmpty()){
            Spacer(modifier = Modifier.height(30.dp))
            ShowSocialMediaLinks(socialMediaLinks,viewmodel::openBrowser)
        }
        Spacer(modifier = Modifier.height(30.dp))
        TabDetailsAndNotes(modifier = Modifier, contactDetails = contactDetails, notes = notes, openNotesTab = openNotesTab, openDetailsTab = openDetailsTab, tabId = tabId)

        if (tabId == 1) {
            viewmodel.getContactDetailsForDetailsTab(contactDetails).forEach{ detail ->
                DetailCard(modifier = Modifier, title = detail.key, body = detail.value)
            }
        } else if ((tabId == 2)) {
            Button(onClick = {onNewNoteClick(viewmodel.contactId)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors( Color(0xFF343A40)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Add Note")
            }
            notes.forEach{ note ->
                NoteCard(note = note, viewmodel = viewmodel,modifier = Modifier, onNoteClicked = onNoteClicked)
            }
        }
    }
}

@Composable
fun ContactActions(
    phoneNumber: Long?,
    enableCall:(Context,String)->Unit,
    enableVideoCall:(Context,String)->Unit,
    sendMessage:(Context,String)->Unit
    ) {
    val context = LocalContext.current
    if (phoneNumber != null){
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ContactActionItem(icon = R.drawable.call, text = "Call", onClick = {enableCall(context,phoneNumber.toString())})
            ContactActionItem(icon = R.drawable.text, text = "Text", onClick = {sendMessage(context,phoneNumber.toString())})
            ContactActionItem(icon = R.drawable.video, text = "Video", onClick = {enableVideoCall(context,phoneNumber.toString())})
        }
    }
}

@Composable
fun ContactActionItem(icon: Int, text: String, onClick:()->Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick,
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(100)),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(222, 226, 230))) {
            Icon(painter = painterResource(id = icon), contentDescription = text, tint = Color.Unspecified, modifier = Modifier.size(32.dp))
        }
        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun NoteCard(note: Note, viewmodel: ContactDetailsViewModel,onNoteClicked:(Int,Int)-> Unit,modifier: Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = viewmodel.getNoteCardRandomColor()
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onNoteClicked(viewmodel.contactId, note.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Text(text = note.title.toString(), color = Color(0, 0, 0, 255), fontSize = 24.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = note.body.toString(), color = Color(56, 56, 56, 255), fontSize = 18.sp)
        }
    }
}

@Composable
fun DetailCard(modifier: Modifier,title: String, body: String?) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(217, 217, 217)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                clipboardManager.setText(AnnotatedString(body.toString()))
                Toast
                    .makeText(context, "Text Copied!!", Toast.LENGTH_SHORT)
                    .show()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Text(text = title, color = Color(0,128,255), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = body.toString(), color = Color(90,90,90), fontSize = 16.sp)
        }
    }
}

@Composable
fun TabDetailsAndNotes(
    modifier: Modifier.Companion, contactDetails: Contact, notes: List<Note>,
                       openNotesTab : () -> Unit,
                       openDetailsTab : () -> Unit,
    tabId: Int
) {
    val ROW_HEIGHT = 50.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ROW_HEIGHT)
            .background(Color(222, 226, 230)),
        verticalAlignment = Alignment.CenterVertically)
    {
        Box(
            modifier = Modifier
                .background(
                    color = if (tabId == 1) {
                        Color(198, 198, 198)
                    } else {
                        Color(222, 226, 230)
                    }
                )
                .weight(0.5f)
                .height(ROW_HEIGHT)
                .clickable { openDetailsTab() }) {
            Text(text = "Details",  //tabId = 1
                fontSize = 20.sp,
                color = Color(52,58,64),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)

            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (tabId == 2) {
                        Color(198, 198, 198)
                    } else {
                        Color(222, 226, 230)
                    }
                )
                .weight(0.5f)
                .height(ROW_HEIGHT)
                .clickable { openNotesTab() }) {
            Text(text = "Notes",  //tabId = 1
                fontSize = 20.sp,
                color = Color(52,58,64),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)

            )
        }
    }

}

@Composable
fun ShowSocialMediaLinks(socialMediaLinks: Map<Int, String?>, openBrowser:(Context,String)->Unit) {
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        socialMediaLinks.forEach{ (resourceId, url) ->
            if (url != null){
                item {
                    Image(painter = painterResource(id = resourceId),
                        contentScale = ContentScale.Inside,
                        contentDescription = "Social Link",
                        modifier = Modifier
                            .width(80.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .clickable {
                                openBrowser(context, url)
                            })
                }
            }
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun ProfileView(contact: Contact) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
        val CARD_SIZE = 180.dp
        Card(
            modifier = Modifier
                .size(CARD_SIZE)
                .border(width = 4.dp, color = Color(52, 58, 64), RoundedCornerShape(100.dp))
                .clip(RoundedCornerShape(100.dp))
                .background(Color.Transparent)
                .padding(7.dp)
                .background(Color.Transparent)
                .clip(RoundedCornerShape(100.dp)),
        ) {
            if (contact.photo == null) {
                Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Contact Profile")
            } else {
                Image(painter = rememberImagePainter(data = File(context.filesDir,contact.photo)),
                    contentDescription = "Contact Profile" , contentScale = ContentScale.Crop)
            }
        }

        // name
        Text(text = contact.name,
            fontSize = 30.sp,
            color = Color(52,58,64),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Transparent)
                .width(CARD_SIZE)
                .padding(top = 10.dp)
        )
        // tag
        if (contact.tag != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = contact.tag,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(217, 20, 207),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(241, 188, 239, 255))
                    .padding(5.dp)


                )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private  fun ContactTopAppBar(
    modifier: Modifier,
    onNavigateBackClick: () -> Unit,
    onHeartClick : () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    contactDetails: Contact){

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(222, 226, 230)
        ),
        title = { Text(text = "")},
        navigationIcon = {
                         IconButton(onClick = { onNavigateBackClick() }) {
                             Image(modifier= Modifier
                                 .size(50.dp, 50.dp),
                                 imageVector = Icons.Rounded.KeyboardArrowLeft,
                                 contentDescription = "Navigate Back"
                             )
                         }
            },
        actions = {
            ActionsForTopAppBar(
                modifier = Modifier,
                onHeartClick = onHeartClick,
                contactDetails = contactDetails,
                onEditClick = onEditClick,
                onNavigateBackClick= onNavigateBackClick,
                onDeleteClick = onDeleteClick
            )
        }
    )
}

@Composable
private fun ActionsForTopAppBar(
    modifier: Modifier,
    onHeartClick : () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigateBackClick: () -> Unit,
    contactDetails: Contact
) {

    var expanded by remember{ mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHeartClick) { //TODO("Implement click feature of heart icon")
            Image(modifier= Modifier
                .size(30.dp, 30.dp),
                painter = painterResource(id =  if (contactDetails.favourite){ R.drawable.heart_filled} else {R.drawable.heart_outlined})
                , contentDescription = "Search"
            )
        }

        Box {
            IconButton(onClick = { expanded = !expanded} ) {  //TODO("Implement click feature of menu icon")
                Image(
                    modifier = Modifier
                        .size(40.dp, 40.dp),
                    imageVector = Icons.Rounded.MoreVert, contentDescription = "Search"
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                DropdownMenuItem(text = { Text(text = "Edit")},
                    onClick = {
                        onEditClick()
                        expanded = false
                })
                DropdownMenuItem(text = { Text(text = "Delete")}, onClick = {
                    onDeleteClick()
                    onNavigateBackClick()
                    expanded = false
                })
            }
        }
    }
}



