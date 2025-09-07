package com.example.notes.ui.searching

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingScreenUi(
    modifier: Modifier = Modifier,
    viewModel: SearchingViewModel,
    onNoteClick: (Int) -> Unit,
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val listState = rememberLazyGridState()

    var isNavigating by remember { mutableStateOf(false) }

    val query = viewModel.query

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = query,
                        onValueChange = {
                            viewModel.onQueryChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 21.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (query.isEmpty()) {
                                    Text(
                                        "Wyszukaj...",
                                        style = TextStyle(
                                            fontSize = 21.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                            contentDescription = "Wróć"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Więcej opcji"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

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
                Column (
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
                                            note.id?.let { onNoteClick(it) }
                                            isNavigating = true
                                        }
                                    }
                                )
                        ) {
                            HighlightedText(
                                fullText = note.content,
                                query = query,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                    }

                    HighlightedText(
                        fullText = viewModel.getTitleText(note),
                        query = query,
                        modifier = modifier.align(Alignment.CenterHorizontally)
                    )

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
    }
}

@Composable
fun HighlightedText(fullText: String, query: String, modifier: Modifier) {
    if (query.isEmpty()) {
        Text(
            text = fullText,
            modifier = modifier
        )
        return
    }

    val annotatedText = buildAnnotatedString {
        val lowerFull = fullText.lowercase()
        val lowerQuery = query.lowercase()

        var startIndex = 0
        var index = lowerFull.indexOf(lowerQuery, startIndex)

        while (index >= 0) {
            append(fullText.substring(startIndex, index))

            withStyle(
                SpanStyle(
                    background = Color.Red
                )
            ) {
                append(fullText.substring(index, index + query.length))
            }

            startIndex = index + query.length
            index = lowerFull.indexOf(lowerQuery, startIndex)
        }

        if (startIndex < fullText.length) {
            append(fullText.substring(startIndex))
        }
    }

    Text(
        text = annotatedText,
        modifier = modifier
    )
}