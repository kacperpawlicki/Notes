package com.example.notes.ui.notedetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.ui.noteslist.NotesListViewModel

@Composable
fun NoteDetailScreenUi(
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel,
) {
    val noteState by viewModel.noteState.collectAsStateWithLifecycle()

    Text(noteState.content)
}