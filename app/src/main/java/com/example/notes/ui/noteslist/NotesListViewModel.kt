package com.example.notes.ui.noteslist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotesListViewModel(
    private val dao: NoteDao,
    private val onNavigateBack: () -> Unit
): ViewModel() {

    val notes = dao.getAllNotesOrderedByModDate()

    private var _selectedNotes = mutableStateListOf<Note>()
    val selectedNotes: List<Note> get() = _selectedNotes

    fun toggleSelection(note: Note) {
        if (_selectedNotes.contains(note)) {
            _selectedNotes.remove(note)
        } else {
            _selectedNotes.add(note)
        }
    }

    fun clearSelected() {
        _selectedNotes.clear()
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            dao.deleteNotes(_selectedNotes.toList())
        }
        clearSelected()
    }

    fun getSelectedNotesCount(): Int {
        return _selectedNotes.count()
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