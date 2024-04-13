package com.example.connect.ui.presentation


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.connect.R
import com.example.connect.data.local.Tags
import com.example.connect.ui.AppViewModelProvider
import com.example.connect.ui.model.ContactModel
import com.example.connect.ui.navigation.NavigationDestination
import com.example.connect.ui.viewmodels.EditContactViewModel
import java.util.Date
import kotlinx.coroutines.launch
import java.io.File
import kotlin.reflect.KFunction2

object EditContactDestination: NavigationDestination {
    override val route: String = "edit_contact"
    val contactIdArg = "contactId"
    val routeWithArgs = "$route/{$contactIdArg}"

}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EditContactScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditContactViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Log.d("EditContactScreen","Edit Contact Screen Instantiated")
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ContactTopAppBar(
                modifier = Modifier,
                onNavigateBack = onNavigateBack,
               // topAppBarTitle = topAppBarTitle,
                onSaveBtnClicked = {
                    viewModel.saveImageToInputStorage(context = context)
                    coroutineScope.launch {

                        viewModel.saveContact()
                        onNavigateBack()
                    }
                                   },
                validateInput = viewModel.validateInput()

            )
        }

    )
    { contentPadding ->
        //Content
        Box( modifier = Modifier.padding(contentPadding)) {
            Log.d("EditContactScreen","EditScreenBody Instantiated")
            ContactEditScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(233, 236, 239)),
                tagsList = viewModel.tagsList.collectAsState().value,
                _uiState = viewModel.editContactUiState,
                onContactDetailValueChange = viewModel::updateUiState,
                contactPhotoUri = viewModel.contactPhotoUri,
                dateTostring = viewModel::dateToString,
                onPhotoPickerSelectionChanged = viewModel::onPhotoPickerSelectionChanged
            )
            Log.d("EditContactScreen","EditScreenBody process ended")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEditScreenBody(
    modifier: Modifier,
    tagsList: List<Tags>,
    _uiState: ContactModel,
    onContactDetailValueChange: (ContactModel) -> Unit,
    contactPhotoUri:Uri?,
    dateTostring: (Date) -> String,
    onPhotoPickerSelectionChanged: (Uri?,Context)->Unit
) {
    val context = LocalContext.current
    val CARD_SIZE = 180.dp
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val lazyListState = rememberLazyListState()
    val textfieldColor = TextFieldDefaults.textFieldColors(
        containerColor = Color(217,217,217),
        focusedTextColor = Color.DarkGray,
        unfocusedTextColor = Color.DarkGray,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    Log.d("EditContactScreen","PhotoPickerLauncher started")
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            onPhotoPickerSelectionChanged(uri, context)
        }
    )
    Log.d("EditContactScreen","PhotoPickerLauncher ended")
    LazyColumn(
        modifier = modifier
            .padding(start = 15.dp, end = 15.dp),
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
        item{
            Spacer(modifier = Modifier.height(10.dp))
        }
        item(key=1){
            Box {
                Card(
                    modifier = Modifier
                        .size(CARD_SIZE)
                        .border(width = 4.dp, color = Color.DarkGray, RoundedCornerShape(100.dp))
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.Transparent)
                        .padding(7.dp)
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(100.dp)),
                ) {
                    if ((_uiState.photo == null) && (contactPhotoUri == null)) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Contact Profile"
                        )
                    } else if (contactPhotoUri != null){
                        Image(
                            painter = rememberImagePainter(data = contactPhotoUri),
                            contentDescription = "Contact Profile", contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberImagePainter(data = _uiState.photo?.let {
                                File(context.filesDir,
                                    it
                                )
                            }),
                            contentDescription = "Contact Profile", contentScale = ContentScale.Crop
                        )
                    }
                }

                IconButton(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                    modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(
                        imageVector = if (_uiState.photo == null) {Icons.Rounded.Add} else {Icons.Rounded.Edit},
                        contentDescription = "Add Photo",
                        modifier = Modifier
                            .size(100.dp, 100.dp)
                            .background(color = Color.DarkGray)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(color = Color.DarkGray)
                        ,
                        tint = Color.White
                    )
                }
            }
        }


        Log.d("EditContactScreen","Input Columns started")
        //item2
        item(key=2) {
            TextField(
                value = _uiState.name,
                onValueChange = { onContactDetailValueChange(_uiState.copy(name = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.user_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Name",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item3
        item(key=3) {
            TextField(
                value = _uiState.highlight,
                onValueChange = { onContactDetailValueChange(_uiState.copy(highlight = it)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Info, contentDescription = null, tint = Color.DarkGray) },
                label = { Text(text = "Highlight", color = Color(0, 128, 255), fontWeight = FontWeight.SemiBold) },
                singleLine = false,
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item4
        item(key=4) {
            TagExposedDropDownMenuBox(
                modifier = Modifier,
                _uiState = _uiState,
                tagsList = tagsList,
                onMenuItemValueChange = onContactDetailValueChange
            )
        }

        // datepickerdialog
        item(key=5) {
            DobDateTextField(
                modifier = Modifier,
                _uiState = _uiState,
                onDateValueChange = onContactDetailValueChange,
                dateToString = dateTostring
            )
        }
        //item6
        item(key=6) {
            TextField(
                value = _uiState.mobile,
                onValueChange = { onContactDetailValueChange(_uiState.copy(mobile = it)) },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Phone, contentDescription = null, tint = Color.DarkGray) },
                label = { Text(text = "Mobile Number", color = Color(0, 128, 255), fontWeight = FontWeight.SemiBold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }


        //item8
        item(key = 8 ) {
            TextField(
                value = _uiState.email,
                onValueChange = { onContactDetailValueChange(_uiState.copy(email = it)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = null,
                        tint = Color.DarkGray
                    )
                },
                label = {
                    Text(
                        text = "Email",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item9
        item(key = 9) {
            TextField(
                value = _uiState.college,
                onValueChange = { onContactDetailValueChange(_uiState.copy(college = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.college_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "College",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item10
        item(key = 10) {
            TextField(
                value = _uiState.address,
                onValueChange = { onContactDetailValueChange(_uiState.copy(address = it)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = null,
                        tint = Color.DarkGray
                    )
                },
                label = {
                    Text(
                        text = "Home Address",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item11
        item(key =11) {
            TextField(
                value = _uiState.workplace,
                onValueChange = { onContactDetailValueChange(_uiState.copy(workplace = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.company_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Workplace",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }


        //item13
        item(key =13) {
            TextField(
                value = _uiState.linkedIn,
                onValueChange = { onContactDetailValueChange(_uiState.copy(linkedIn = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.linkedin_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "LinkedIn ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item14
        item(key = 14) {
            TextField(
                value = _uiState.facebook,
                onValueChange = { onContactDetailValueChange(_uiState.copy(facebook = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Facebook ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item15
        item(key = 15) {
            TextField(
                value = _uiState.youtube,
                onValueChange = { onContactDetailValueChange(_uiState.copy(youtube = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.youtube_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Youtube ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item16
        item(key = 16) {
            TextField(
                value = _uiState.twitter,
                onValueChange = { onContactDetailValueChange(_uiState.copy(twitter = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.twitter_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Twitter ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item17
        item(key =17) {
            TextField(
                value = _uiState.instagram,
                onValueChange = { onContactDetailValueChange(_uiState.copy(instagram = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Instagram ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }



        //item20
        item(key = 20) {
            TextField(
                value = _uiState.github,
                onValueChange = { onContactDetailValueChange(_uiState.copy(github = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.github_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Github ID",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        //item21
        item(key = 21) {
            TextField(
                value = _uiState.hyperlink,
                onValueChange = { onContactDetailValueChange(_uiState.copy(hyperlink = it)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.website_bw),
                        contentDescription = null, tint = Color.DarkGray,
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Website Link",
                        color = Color(0, 128, 255),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = textfieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        Log.d("EditContactScreen","Input Columns ended")

    }

}

@Composable
fun textFiledIcon(){
    TODO("Remove reduntant code from textfiled above")
}

@Composable
fun textFiledLabel(){
    TODO("Remove reduntant code from textfiled above")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobDateTextField(
    modifier: Modifier,
    _uiState: ContactModel,
    onDateValueChange: (ContactModel) -> Unit,
    dateToString: (Date) -> String
) {
    var datePickerOpenDialog by remember{ mutableStateOf(false) }
    TextField(
        value = _uiState.dob,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .clickable { datePickerOpenDialog = true },
        readOnly = true,
        onValueChange = { },
        trailingIcon = {
            if (_uiState.dob != ""){
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "Clear Date",
                    modifier = Modifier.clickable { onDateValueChange(_uiState.copy(dob  = "")) },
                    tint = Color.DarkGray
                )
            }

        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier.clickable {
                    datePickerOpenDialog = true
                },
                tint = Color.DarkGray
            )
        },
        label = { Text(text = "Date of Birth", color = Color(0,128,255), fontWeight = FontWeight.SemiBold,modifier = Modifier.clickable {
            datePickerOpenDialog = true
        } )},
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(217,217,217),
            focusedTextColor = Color.DarkGray,
            unfocusedTextColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

    )


    if (datePickerOpenDialog){
        DobDatePicker(
            openDialog = datePickerOpenDialog,
            onOpenDialogChange = {datePickerOpenDialog = it},
            modifier = Modifier,
            _uiState = _uiState,
            onDateValueChange = onDateValueChange,
            dateToString = dateToString
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobDatePicker(
    modifier: Modifier,
    _uiState: ContactModel,
    onDateValueChange: (ContactModel) -> Unit,
    dateToString: (Date) -> String,
    openDialog: Boolean,
    onOpenDialogChange: (Boolean) -> Unit
) {

    val state =  rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = Date().time
    )
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenDialogChange(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateValueChange(_uiState.copy(dob = if (state.selectedDateMillis == null) {""} else {dateToString(Date(
                            state.selectedDateMillis!!))} ))
                        onOpenDialogChange(false)
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { onOpenDialogChange(false) }) {
                    Text(text = "Cancel")
                }
            }
        )
        {
           DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagExposedDropDownMenuBox(
    modifier: Modifier,
    _uiState: ContactModel,
    tagsList: List<Tags>,
    onMenuItemValueChange: (ContactModel) -> Unit
) {
    //val placeHolderText = "No Tag"
    val tagsList = tagsList
    var expanded by remember{ mutableStateOf(false) }
    var  selectedTag by remember{ mutableStateOf(_uiState.tag) }
    Box{
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
            TextField(
                value =selectedTag,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Select Tag", color = Color(0,128,255), fontWeight = FontWeight.SemiBold )},
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.label_bw),
                    contentDescription = null, tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp,24.dp))},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.scale(scaleX = 1f,if (expanded) -1f else 1f),
                        tint = Color.DarkGray
                    )
                               },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(217,217,217),
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false},
                modifier = Modifier.background(Color(217,217,217))) {
                tagsList.forEach{tag->
                    DropdownMenuItem(
                        text = { Text(text = tag.name , color = Color.Black) },
                        onClick = {
                            selectedTag = tag.name
                            expanded = false
                            onMenuItemValueChange(_uiState.copy(tag = selectedTag))
                            Log.d("Tag", selectedTag.toString())
                        },
                        modifier = Modifier.background(Color(217,217,217))
                    )

                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private  fun ContactTopAppBar(
    modifier: Modifier,
    onNavigateBack : () -> Unit,
    onSaveBtnClicked : () -> Unit,
    validateInput: Boolean
){

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(222, 226, 230)
        ),
        title = { Text(text = "", color = Color.DarkGray) },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack()}) {
                Image(modifier= Modifier
                    .size(50.dp, 50.dp),
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Navigate Back"
                )
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onSaveBtnClicked()
                              },
                    enabled = validateInput,
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}

