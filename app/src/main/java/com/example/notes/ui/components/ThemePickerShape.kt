package com.example.notes.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ThemePickerShape(
    size: Dp,
    cornerRadius: Dp,
    color1: Color,
    color2: Color,
    borderColor: Color,
    borderWidth: Dp,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val scale = animateFloatAsState(targetValue = if (selected) 1.2f else 1f)

    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        val rect = Rect(0f, 0f, size.toPx(), size.toPx())
        val corner = cornerRadius.toPx()
        val stroke = if (selected) borderWidth.toPx() * 1.5f else borderWidth.toPx()

        val roundRect = RoundRect(rect, corner, corner)

        clipPath(Path().apply { addRoundRect(roundRect) }) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.toPx(), 0f)
                    lineTo(0f, size.toPx())
                    close()
                },
                color = color1
            )

            drawPath(
                path = Path().apply {
                    moveTo(size.toPx(), 0f)
                    lineTo(size.toPx(), size.toPx())
                    lineTo(0f, size.toPx())
                    close()
                },
                color = color2
            )

            drawLine(
                color = borderColor,
                start = Offset(0f, size.toPx()),
                end = Offset(size.toPx(), 0f),
                strokeWidth = stroke
            )
        }

        drawRoundRect(
            color = borderColor,
            topLeft = rect.topLeft,
            size = rect.size,
            cornerRadius = CornerRadius(corner, corner),
            style = Stroke(width = stroke)
        )
    }
}