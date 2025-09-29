package com.example.notes.ui.notedetail

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreenUi(
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel,
    onSettingsClick: () -> Unit
) {

    val note by viewModel.note.collectAsState()

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val localView = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    val lineHeight = 24.sp
    val lineHeightPx = with(density) { lineHeight.toPx() }


    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    if (!viewModel.isNoteNew()) {
        textFieldValue = textFieldValue.copy(text = note.content)
    }

    val insets = WindowInsets.ime
    val keyboardHeight by remember {
        derivedStateOf {
            insets.getBottom(density)
        }
    }

    val visibleLinesAboveKeyboard = if (keyboardHeight > 0) {
        val screenHeightPx = with(density) {
            LocalConfiguration.current.screenHeightDp.dp.toPx()
        }
        val topBarAndPaddingPx = with(density) { 120.dp.toPx() }
        val availableHeightAboveKeyboard = screenHeightPx - keyboardHeight - topBarAndPaddingPx

        (availableHeightAboveKeyboard / lineHeightPx).toInt() - 3 // -3 linie na margines
    } else {
        0
    }

    LaunchedEffect(textFieldValue.selection.start, keyboardHeight) {
        if (keyboardHeight > 0 && textFieldValue.selection.start >= 0) {

            val cursorPosition = textFieldValue.selection.start
            val lines = textFieldValue.text.substring(0, cursorPosition).count { it == '\n' }
            val cursorYPosition = lines * lineHeightPx

            val minVisiblePosition = cursorYPosition - lineHeightPx * visibleLinesAboveKeyboard

            if (minVisiblePosition > scrollState.value) {
                scrollState.animateScrollTo(minVisiblePosition.toInt().coerceAtLeast(0))
            }
        }
    }

    if (viewModel.isNoteNew() && textFieldValue.text.isEmpty()) focusRequester.requestFocus()


    BackHandler {
        viewModel.viewModelScope.launch {
            viewModel.saveAndNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = note.title,
                        onValueChange = { viewModel.updateNoteTitle(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 21.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (note.title.isEmpty()) {
                                    Text(
                                        "Tytuł",
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
                                viewModel.saveAndNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
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
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            HorizontalDivider()

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->

                            val clickedLine = (offset.y / lineHeight.toPx()).toInt()
                            val lines = textFieldValue.text.split('\n')

                            if (clickedLine >= lines.size) {
                                val newLines = lines.toMutableList()
                                repeat(clickedLine - lines.size) {
                                    newLines.add("")
                                }
                                val newText = newLines.joinToString("\n")

                                val cursorPosition = if (clickedLine < newLines.size) {
                                    newLines.take(clickedLine + 1)
                                        .sumOf { it.length + 1 } - 1
                                } else {
                                    newText.length
                                }

                                textFieldValue = TextFieldValue(
                                    text = newText,
                                    selection = TextRange(cursorPosition.coerceAtMost(newText.length))
                                )
                                viewModel.updateNoteContent(newText)

                            } else {
                                val cursorPosition = lines.take(clickedLine + 1)
                                        .sumOf { it.length + 1 } - 1

                                textFieldValue = textFieldValue.copy(
                                    selection = TextRange(cursorPosition.coerceAtMost(textFieldValue.text.length))
                                )
                            }

                            focusRequester.requestFocus()
                            keyboardController?.show()

                        }
                    }
            ) {
                Spacer(modifier = modifier.height(16.dp))

                BasicTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .imePadding()
                        .focusRequester(focusRequester)
                        .onPreviewKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.restartInput(localView)

                                coroutineScope.launch {
                                    delay(10)
                                    val currentText = textFieldValue.text
                                    val cursorPosition = textFieldValue.selection.start

                                    val newText = currentText.substring(0, cursorPosition) + "\n" +
                                            currentText.substring(cursorPosition)

                                    textFieldValue = TextFieldValue(
                                        text = newText,
                                        selection = TextRange(cursorPosition + 1)
                                    )
                                    viewModel.updateNoteContent(newText)
                                }
                                true
                            } else {
                                false
                            }
                        },
                    value = textFieldValue,
                    onValueChange = { newContent ->
                        textFieldValue = newContent
                        viewModel.updateNoteContent(newContent.text)
                    },
                    singleLine = false,
                    maxLines = Int.MAX_VALUE,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )

                Spacer(modifier = modifier.height(500.dp))
            }
        }
    }
}