package com.example.notes.ui.noteslist

import androidx.lifecycle.ViewModel
import com.example.notes.data.sampleNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesListViewModel(

): ViewModel() {
    val _notes = MutableStateFlow(sampleNotes)
    val notes = _notes.asStateFlow()
}