package com.example.notes.ui.noteslist

import androidx.lifecycle.ViewModel
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotesListViewModel(
    val dao: NoteDao
): ViewModel() {

    val notes = dao.getAllNotesOrderedByModDate()




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