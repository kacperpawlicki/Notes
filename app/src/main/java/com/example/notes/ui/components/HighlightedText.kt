package com.example.notes.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightedText(fullText: String, query: String, modifier: Modifier = Modifier) {
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