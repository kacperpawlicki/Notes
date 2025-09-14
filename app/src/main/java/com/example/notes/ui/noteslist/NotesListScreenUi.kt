package com.example.notes.ui.noteslist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreenUi(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel,
    onNoteClick: (Int) -> Unit,
    onNoteAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val listState = rememberLazyGridState()

    val scope = rememberCoroutineScope()

    val addingFabVisible by remember {
        derivedStateOf {
            !listState.isScrollInProgress
        }
    }

    val scrollUpFabVisible by remember {
        derivedStateOf {
            !(listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) && listState.isScrollInProgress
        }
    }

    var isNavigating by remember { mutableStateOf(false) }

    var isDeleting by remember { mutableStateOf(false) }

    var isDeletingConfirmationDialogVisible by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var dropDownMenuExpanded by remember { mutableStateOf(false) }



    BackHandler {
        if (isDeleting) {
            isDeleting = false
        } else {
            viewModel.navigateBack()
        }
    }

    if (isDeletingConfirmationDialogVisible) {
        DeleteConfirmationDialog(
            onDismiss = { isDeletingConfirmationDialogVisible = false },
            onConfirm = {
                viewModel.deleteSelectedNotes()
                isDeleting = false
                isDeletingConfirmationDialogVisible = false
            },
            amount = viewModel.getSelectedNotesCount()
        )
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Moje notatki",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSearchClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Wyszukaj notatkę",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Box{
                        IconButton(
                            onClick = {
                                dropDownMenuExpanded = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Więcej opcji",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                            expanded = dropDownMenuExpanded,
                            onDismissRequest = { dropDownMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                                text = {
                                    Text(
                                        text = "Ustawienia",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onSettingsClick()
                                }
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (!isDeleting) {
                AnimatedVisibility(
                    visible = addingFabVisible,
                    enter = fadeIn(
                        animationSpec = tween(200, 800)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(200)
                    )
                ) {
                    FloatingActionButton(
                        onClick = {
                            onNoteAddClick()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Utwórz nową notatkę"
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (isDeleting) {
                BottomAppBar(
                    content = {
                        IconButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                isDeletingConfirmationDialogVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Usuń wybrane notatki"
                            )
                        }
                    },
                    modifier = Modifier
                        .height(120.dp)
                )
            }

        }
    ) { innerPadding ->
        Box {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp, 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState
            ) {
                items(notes) { note ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {


                        Box {
                            Card(
                                modifier = Modifier

                                    .fillMaxWidth()
                                    .height(225.dp)
                                    .padding(vertical = 3.dp)
                                    .combinedClickable(
                                        onClick = {
                                            if (isDeleting) {
                                                viewModel.toggleSelection(note)
                                            } else if (!isNavigating) {
                                                isNavigating = true
                                                note.id?.let { onNoteClick(it) }
                                            }
                                        },
                                        onLongClick = {
                                            if (!isDeleting) {
                                                isDeleting = true
                                                viewModel.toggleSelection(note)
                                            } else {
                                                viewModel.clearSelected()
                                                isDeleting = false
                                            }
                                        }
                                    )
                            ) {
                                Text(
                                    text = note.content,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (isDeleting) {
                                Checkbox(
                                    checked = note in viewModel.selectedNotes,
                                    onCheckedChange = {
                                        viewModel.toggleSelection(note)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                )
                            }

                        }

                        Text(
                            text = viewModel.getTitleText(note),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = viewModel.getModificationTimeText(note),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = scrollUpFabVisible,
                enter = fadeIn(
                    animationSpec = tween(200)
                ),
                exit = fadeOut(
                    animationSpec = tween(200, 1800)
                ),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .size(40.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Podjedź do góry",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                }
            }
        }
    }
}


@Composable
fun DeleteConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, amount: Int){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Potwierdzenie")
        },
        text = {
            Text("Czy na pewno chcesz usunąć notatki? ($amount)")
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = "Usuń",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "Anuluj",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

