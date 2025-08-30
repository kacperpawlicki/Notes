package com.example.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity
data class Note(
    val title: String,
    val content: String,
    val creationDate: LocalDateTime,
    val modificationDate: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
) {
    companion object {
        fun empty() = Note(
            title = "",
            content = "",
            creationDate = LocalDateTime.now(),
            modificationDate = LocalDateTime.now(),
            id = null
        )
    }
}
