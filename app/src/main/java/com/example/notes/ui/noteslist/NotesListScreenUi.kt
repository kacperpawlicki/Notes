package com.example.notes.ui.noteslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NotesListScreenUi(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel,
    onNoteClick: (Int) -> Unit
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
    ){
        items(notes) { note ->
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = { onNoteClick(note.id) }
                    )
            ) {
                Text(
                    text = note.title
                )
            }
        }
    }
}