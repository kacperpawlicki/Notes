package com.example.notes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.notes.ui.notedetail.NoteDetailScreenUi
import com.example.notes.ui.notedetail.NoteDetailViewModel
import com.example.notes.ui.noteslist.NotesListScreenUi
import com.example.notes.ui.noteslist.NotesListViewModel
import kotlinx.serialization.Serializable


@Serializable
data object NotesListScreen: NavKey

@Serializable
data class NoteDetailScreen(val id: Int): NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
){
    val backStack = rememberNavBackStack(NotesListScreen)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is NotesListScreen -> {
                    NavEntry(key = key){
                        NotesListScreenUi(
                            viewModel = NotesListViewModel(),
                            onNoteClick = { noteId ->
                                backStack.add(NoteDetailScreen(noteId))
                            }
                        )
                    }
                }
                is NoteDetailScreen -> {
                    NavEntry(key = key){
                        NoteDetailScreenUi(
                            viewModel = NoteDetailViewModel(key.id)
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}