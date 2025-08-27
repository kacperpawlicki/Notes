package com.example.notes.ui.noteslist

import androidx.lifecycle.ViewModel
import com.example.notes.data.Note
import com.example.notes.data.sampleNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotesListViewModel(

): ViewModel() {
    val _notes = MutableStateFlow(sampleNotes)
    val notes = _notes.asStateFlow()


    val monthsPolish = listOf(
        "sty", "lut", "mar", "kwi", "maj", "cze",
        "lip", "sie", "wrz", "paź", "lis", "gru"
    )

    fun getTitleText(note: Note): String {
        if (note.title == "") {
            return "Notatka tekstowa z dnia ${note.creationDate.dayOfMonth}." +
                    "${note.creationDate.format(DateTimeFormatter.ofPattern("MM"))}"
        }
        return note.title
    }

    fun getModificationTimeText(note: Note): String {
        if (note.modificationDate.toLocalDate().equals(LocalDate.now())) {
            return "${note.modificationDate.hour}:${note.modificationDate.minute}"
        }
        return "${note.modificationDate.dayOfMonth} ${monthsPolish.getOrNull(note.modificationDate.monthValue-1)}"
    }
}