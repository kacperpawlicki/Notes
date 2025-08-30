package com.example.notes.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.notes.data.NoteDatabaseProvider
import com.example.notes.ui.notedetail.NoteDetailScreenUi
import com.example.notes.ui.notedetail.NoteDetailViewModel
import com.example.notes.ui.noteslist.NotesListScreenUi
import com.example.notes.ui.noteslist.NotesListViewModel
import kotlinx.serialization.Serializable


@Serializable
data object NotesListScreen: NavKey

@Serializable
data class NoteDetailScreen(val id: Int?): NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
){
    val backStack = rememberNavBackStack(NotesListScreen)

    val context = LocalContext.current.applicationContext
    val db = NoteDatabaseProvider.get(context)
    val dao = db.noteDao()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(500)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            )
        },
        entryProvider = { key ->
            when(key) {
                is NotesListScreen -> {
                    NavEntry(key = key){
                        NotesListScreenUi(
                            viewModel = NotesListViewModel(
                                dao = dao
                            ),
                            onNoteAddClick = {
                                backStack.add(NoteDetailScreen(null))
                            },
                            onNoteClick = { noteId ->
                                backStack.add(NoteDetailScreen(noteId))
                            }
                        )
                    }
                }
                is NoteDetailScreen -> {
                    NavEntry(key = key){
                        NoteDetailScreenUi(
                            viewModel = NoteDetailViewModel(
                                dao = dao,
                                noteId = key.id
                            )
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}