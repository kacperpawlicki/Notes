package com.example.notes.ui.searching

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchingViewModel(
    private val dao: NoteDao,
    private val onNavigateBack: () -> Unit
): ViewModel() {

    var notes: Flow<List<Note>> = dao.searchNotes("")

    var query by mutableStateOf("")
        private set

    fun onQueryChange(newQuery: String) {
        query = newQuery
        getSearchResults(newQuery)
    }
    fun getSearchResults(query: String) {
        notes = dao.searchNotes(query)
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