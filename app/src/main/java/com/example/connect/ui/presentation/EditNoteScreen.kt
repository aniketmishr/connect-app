package com.example.connect.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.connect.ui.AppViewModelProvider
import com.example.connect.ui.navigation.NavigationDestination
import com.example.connect.ui.viewmodels.EditNoteUiState
import com.example.connect.ui.viewmodels.EditNoteViewModel
import kotlinx.coroutines.launch


object EditNoteDestination: NavigationDestination {
    override val route: String = "edit_note"
    val noteIdArg = "noteId"
    val contactIdArg = "contactId"
    val routeWithArgs = "$route/{$contactIdArg}/{$noteIdArg}"

}

@Composable
fun EditNoteScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditNoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ContactTopAppBar(
                modifier = Modifier,
                onNavigateBack = onNavigateBack,
                enableDelete = if (viewModel.noteId == 0) false else true, // noteId = 0 for creating new note
                onDeleteNote = viewModel::onDeleteNote,
                onSaveBtnClicked = {
                    coroutineScope.launch {
                        viewModel.saveNote()
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
            EditNoteScreenBody(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(233, 236, 239)),
                _uiState = viewModel.uiState,
                updateUiState = viewModel::updateUiState
            )
        }
    }
}

@Composable
fun EditNoteScreenBody(
    modifier: Modifier,
    _uiState: EditNoteUiState,
    updateUiState: (EditNoteUiState)-> Unit
){
    Column(modifier = modifier.padding(10.dp)) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            textStyle = TextStyle(color = Color.DarkGray, fontSize = 36.sp, fontWeight = FontWeight.SemiBold),
            value = _uiState.title,
            onValueChange = {updateUiState(_uiState.copy(title = it))}
            )
        Spacer(modifier = Modifier.height(20.dp))
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            textStyle = TextStyle(color = Color.DarkGray, fontSize = 24.sp),
            value = _uiState.body,
            onValueChange = {updateUiState(_uiState.copy(body = it))}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private  fun ContactTopAppBar(
    modifier: Modifier,
    enableDelete:Boolean,
    onNavigateBack : () -> Unit,
    onSaveBtnClicked : () -> Unit,
    onDeleteNote: () -> Unit,
    validateInput: Boolean
){
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(222, 226, 230)
        ),
        title = { Text(text = "Add Note", color = Color.DarkGray) },
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
                if (enableDelete){
                    IconButton(onClick = {
                        onDeleteNote()
                        onNavigateBack()
                    }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null, tint = Color(
                            0xFFFD4F4F
                        )
                        )
                    }
                }

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

