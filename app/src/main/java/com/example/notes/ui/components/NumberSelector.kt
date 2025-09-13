package com.example.notes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun NumberSelector(
    selectedNumber: Int,
    numbers: List<Int>,
    onNumberSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .height(35.dp)
                .width(60.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(
                text = selectedNumber.toString(),
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector =
                    if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            numbers.forEach { number ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = number.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                           },
                    onClick = {
                        onNumberSelected(number)
                        expanded = false
                    }
                )
            }
        }
    }
}