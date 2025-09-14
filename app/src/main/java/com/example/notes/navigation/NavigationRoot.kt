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
import com.example.notes.ui.searching.SearchingScreenUi
import com.example.notes.ui.searching.SearchingViewModel
import com.example.notes.ui.searchresultnotedetail.SearchResultNoteDetailScreenUi
import com.example.notes.ui.searchresultnotedetail.SearchResultNoteDetailViewModel
import com.example.notes.ui.settings.SettingsScreenUi
import com.example.notes.ui.settings.SettingsViewModel
import kotlinx.serialization.Serializable


@Serializable
data object NotesListScreen: NavKey

@Serializable
data class NoteDetailScreen(val id: Int?, val from: String): NavKey

@Serializable
data class SearchResultNoteDetailScreen(val id: Int, val query: String): NavKey
@Serializable
data object SearchingScreen: NavKey

@Serializable
data object SettingsScreen: NavKey

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
                                dao = dao,
                                onNavigateBack = {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            ),
                            onNoteAddClick = {
                                backStack.add(NoteDetailScreen(id = null, from = "NotesListScreen"))
                            },
                            onNoteClick = { noteId ->
                                backStack.add(NoteDetailScreen(id = noteId, from = "NotesListScreen"))
                            },
                            onSearchClick = {
                                backStack.add(SearchingScreen)
                            },
                            onSettingsClick = {
                                backStack.add(SettingsScreen)
                            }
                        )
                    }
                }
                is NoteDetailScreen -> {
                    NavEntry(key = key){
                        NoteDetailScreenUi(
                            viewModel = NoteDetailViewModel(
                                dao = dao,
                                noteId = key.id,
                                onNavigateBack = {
                                    backStack.removeAt(backStack.lastIndex)
                                },
                                from = key.from
                            ),
                            onSettingsClick = {
                                backStack.add(SettingsScreen)
                            }
                        )
                    }
                }
                is SearchingScreen -> {
                    NavEntry(key = key){
                        SearchingScreenUi(
                            viewModel = SearchingViewModel(
                                dao = dao,
                                onNavigateBack = {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            ),
                            onNoteClick = { noteId, query ->
                                backStack.add(SearchResultNoteDetailScreen(noteId, query))
                            },
                            onSettingsClick = {
                                backStack.add(SettingsScreen)
                            }
                        )
                    }
                }
                is SearchResultNoteDetailScreen -> {
                    NavEntry(key = key) {
                        SearchResultNoteDetailScreenUi(
                            viewModel = SearchResultNoteDetailViewModel(
                                dao = dao,
                                noteId = key.id,
                                onNavigateBack = {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            ),
                            query = key.query,
                            onNoteEditClick = {
                                backStack.add(NoteDetailScreen(id = key.id, from = "SearchResultScreen"))
                            }
                        )
                    }
                }
                is SettingsScreen -> {
                    NavEntry(key = key) {
                        SettingsScreenUi(
                            viewModel = SettingsViewModel(
                                onNavigateBack = {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            )
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}