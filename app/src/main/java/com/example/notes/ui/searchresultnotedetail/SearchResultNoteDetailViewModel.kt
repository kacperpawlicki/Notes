package com.example.notes.ui.searchresultnotedetail

import com.example.notes.data.NoteDao

class SearchResultNoteDetailViewModel(
    private val noteId: Int,
    private val dao: NoteDao,
    private val onNavigateBack: () -> Unit
) {

    val note = dao.getNoteById(noteId)

    fun navigateBack() {
        onNavigateBack()
    }
}