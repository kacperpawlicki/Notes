package com.example.notes.ui.searchresultnotedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.data.Note
import com.example.notes.ui.components.HighlightedText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultNoteDetailScreenUi(
    modifier: Modifier = Modifier,
    viewModel: SearchResultNoteDetailViewModel,
    query: String,
    onNoteEditClick: () -> Unit
){

    val note by viewModel.note.collectAsState(initial = Note.empty())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (note.title == "") {
                        Text(
                            text = "Brak tytułu"
                        )
                    } else {
                        HighlightedText(
                            fullText = note.title,
                            query = query,
                            textAlign = TextAlign.Left,
                            fontSize = 21.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Wróć"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onNoteEditClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edytuj notatke"
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        onNoteEditClick()
                    }
            ) {
                Spacer(modifier = modifier.height(16.dp))

                HighlightedText(
                    fullText = note.content,
                    query = query,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}