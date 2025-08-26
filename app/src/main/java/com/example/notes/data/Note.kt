package com.example.notes.data

import java.time.LocalDate

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val creationDate: LocalDate,
    val modificationDate: LocalDate
)

val sampleNotes = List(100) {
    Note(
        id = it,
        title = "Title $it",
        content = "Content $it",
        creationDate = LocalDate.now(),
        modificationDate = LocalDate.now()
    )
}