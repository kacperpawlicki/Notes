package com.example.notes.ui.searching

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.notes.ui.components.HighlightedText
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingScreenUi(
    modifier: Modifier = Modifier,
    viewModel: SearchingViewModel,
    onNoteClick: (Int, String) -> Unit,
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val listState = rememberLazyGridState()

    val scope = rememberCoroutineScope()

    val scrollUpFabVisible by remember {
        derivedStateOf {
            !(listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0)
        }
    }

    var isNavigating by remember { mutableStateOf(false) }

    val textFieldValueSaver = Saver<TextFieldValue, String>(
        save = { it.text },
        restore = { restoredText ->
            TextFieldValue(
                text = restoredText,
                selection = TextRange(restoredText.length)
            )
        }
    )

    var query by rememberSaveable(stateSaver = textFieldValueSaver) {
        mutableStateOf(TextFieldValue(""))
    }

    val focusRequester = remember { FocusRequester() }


    val lifecycleOwner = LocalLifecycleOwner.current
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            refreshTrigger++
        }
    }

    LaunchedEffect(refreshTrigger) {
        viewModel.searchNotes(query.text)
        focusRequester.requestFocus()
    }


    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.searchNotes(query.text)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontSize = 21.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (query.text.isEmpty()) {
                                    Text(
                                        "Wyszukaj...",
                                        style = TextStyle(
                                            fontSize = 21.sp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                        ),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.navigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Wróć",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Więcej opcji",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
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
                                    .clickable(
                                        onClick = {
                                            if (!isNavigating) {
                                                note.id?.let { onNoteClick(it, query.text) }
                                                isNavigating = true
                                            }
                                        }
                                    )
                            ) {
                                HighlightedText(
                                    fullText = note.content,
                                    query = query.text,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Left,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                        }

                        if (note.title != "") {
                            HighlightedText(
                                fullText = viewModel.getTitleText(note),
                                query = query.text,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Text(
                                text =  viewModel.getTitleText(note),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = viewModel.getModificationTimeText(note),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = scrollUpFabVisible,
                enter = fadeIn(),
                exit = fadeOut(),
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

