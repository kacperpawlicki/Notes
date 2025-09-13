package com.example.notes.ui.notedetail


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreenUi(
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel,
) {

    val note by viewModel.note.collectAsState()

    BackHandler {
        viewModel.viewModelScope.launch {
            viewModel.saveAndNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = note.title,
                        onValueChange = { viewModel.updateNoteTitle(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 21.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (note.title.isEmpty()) {
                                    Text(
                                        "Tytuł",
                                        style = TextStyle(
                                            fontSize = 21.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                        ),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.saveAndNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Więcej opcji",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            HorizontalDivider()

            val scrollState = rememberScrollState()
            val focusRequester = remember { FocusRequester() }

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        focusRequester.requestFocus()
                    }
            ) {
                Spacer(modifier = modifier.height(16.dp))

                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .imePadding()
                        .focusRequester(focusRequester),
                    value = note.content,
                    onValueChange = { newContent ->
                        viewModel.updateNoteContent(newContent)
                    },
                    singleLine = false,
                    maxLines = Int.MAX_VALUE,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
            }
        }
    }
}