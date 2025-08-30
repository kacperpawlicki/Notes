package com.example.notes.ui.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val noteId: Int?,
    private val dao: NoteDao
): ViewModel() {

    private val _note = MutableStateFlow(Note.empty())

    val note: StateFlow<Note> = _note.asStateFlow()

    init {
        if (noteId != null) {
            viewModelScope.launch {
                dao.getNoteById(noteId).collect { loadedNote ->
                    _note.value = loadedNote
                }
            }
        }
    }

    suspend fun saveNote() {
        dao.upsertNote(_note.value)
    }

    //chwilowo
    suspend fun deleteNote() {
        dao.deleteNote(_note.value)
    }

    fun updateNoteContent(newContent: String) {
        _note.value = _note.value.copy(content = newContent)
    }

    fun updateNoteTitle(newTitle: String) {
        _note.value = _note.value.copy(title = newTitle)
    }
}