package com.example.notes.ui.searching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchingViewModel(
    private val dao: NoteDao,
    private val onNavigateBack: () -> Unit
): ViewModel(){

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        searchNotes("")
    }
    fun searchNotes(query: String) {
        viewModelScope.launch {
            dao.searchNotes(query).collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun navigateBack() {
        onNavigateBack()
    }

    val monthsPolish = listOf(
        "sty", "lut", "mar", "kwi", "maj", "cze",
        "lip", "sie", "wrz", "paź", "lis", "gru"
    )

    fun getTitleText(note: Note): String {
        if (note.title == "") {
            return "Notatka tekstowa z dnia " +
                    "${note.creationDate.format(DateTimeFormatter.ofPattern("dd.MM"))}"
        }
        return note.title
    }

    fun getModificationTimeText(note: Note): String {
        if (note.modificationDate.toLocalDate().equals(LocalDate.now())) {
            return "${note.modificationDate.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        }
        return "${note.modificationDate.dayOfMonth} ${monthsPolish.getOrNull(note.modificationDate.monthValue-1)}"
    }
}