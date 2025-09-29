package com.example.notes.ui.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NoteDetailViewModel(
    private val noteId: Int?,
    private val dao: NoteDao,
    private val onNavigateBack: () -> Unit,
    private val from: String
): ViewModel() {

    private val _note = MutableStateFlow(Note.empty())

    val note: StateFlow<Note> = _note.asStateFlow()

    init {
        if (noteId != null) {
            viewModelScope.launch {
                val loadedNote = dao.getNoteById(noteId).first()
                _note.value = loadedNote
            }
        }
    }

    suspend fun saveNote() {
        updateModificationDate()
        dao.upsertNote(_note.value)
    }

    fun navigateBack() {
        when (from) {
            "NotesListScreen" -> onNavigateBack()
            "SearchResultScreen" -> {
                onNavigateBack()
                onNavigateBack()
            }
        }
    }

    var noteStarted = false
    fun saveAndNavigateBack() {
        if (noteStarted) {
            viewModelScope.launch {
                saveNote()
                navigateBack()
            }
        } else {
            navigateBack()
        }
    }

    fun updateNoteContent(newContent: String) {
        _note.value = _note.value.copy(content = newContent)
        noteStarted = true

        if (noteId != null) {
            viewModelScope.launch {
                dao.upsertNote(_note.value)
            }
        }
    }

    fun updateNoteTitle(newTitle: String) {
        _note.value = _note.value.copy(title = newTitle)
        noteStarted = true

        if (noteId != null) {
            viewModelScope.launch {
                dao.upsertNote(_note.value)
            }
        }
    }

    fun updateModificationDate() {
        val now = LocalDateTime.now()
        _note.value = _note.value.copy(modificationDate = now)
    }

    fun isNoteNew(): Boolean {
        return noteId == null
    }
}