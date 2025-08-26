package com.example.notes.ui.notedetail

import androidx.lifecycle.ViewModel
import com.example.notes.data.sampleNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteDetailViewModel(
    private val noteId: Int
): ViewModel() {
    val _noteState = MutableStateFlow(
        sampleNotes.first{ it.id == noteId }
    )
    val noteState = _noteState.asStateFlow()
}