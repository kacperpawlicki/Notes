package com.example.notes.data

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val creationDate: LocalDate,
    val modificationDate: LocalDateTime
)

val sampleNotes = List(100) {
    Note(
        id = it,
        title = test(),
        content = "Content $it",
        creationDate = LocalDate.now(),
        modificationDate = test2()
    )
}

fun test(): String {
    val a = Random.nextInt(2)
    if(a == 1) {
        return "Tytuł test"
    }
    return ""
}

fun test2(): LocalDateTime {
    val a = Random.nextInt(2)
    if(a == 1) {
        return LocalDateTime.now()
    }
    return LocalDateTime.now().minusDays(2)
}