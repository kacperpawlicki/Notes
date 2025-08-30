package com.example.notes.data

import android.content.Context
import androidx.room.Room

object NoteDatabaseProvider {
    var instance: NoteDatabase? = null

    fun get(context: Context): NoteDatabase {
        return instance ?: Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "note.db"
        ).build().also { instance = it }
    }
}