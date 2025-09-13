package com.example.notes.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.notes.data.themeFlow

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB6224), //checkboxy i kursor
    background = Color(0xFF1C1B1B), // tlo
    surface = Color(0xFF1C1B1B), //tlo tego u gory
    surfaceContainer = Color(0xFF131213), //tlo tego u gory po scrollu
    onSurface = Color(0xFFD2D0CE), //tekst
    onBackground = Color(0xFFE8E6E6), //tekst tytulu
    primaryContainer = Color(0xFFBD7C4E), //dodawanie
    surfaceVariant = Color(0xFF2F2E2E), //scrollup
    surfaceContainerHighest = Color(0xFF424040), //tlo card
    surfaceContainerHigh = Color(0xFF424040), //tlo dialogu
    onPrimaryContainer = Color(0xFFD2D0CE) // kolor ikony plusa
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF667966), //checkboxy i kursor
    background = Color(0xFFEDE6D9), // tlo
    surface = Color(0xFFEDE6D9), //tlo tego u gory
    surfaceContainer = Color(0xFFCEC7BB), //tlo tego u gory po scrollu
    onSurface = Color(0xFF1A1A1A), //tekst
    onBackground = Color(0xFF111111), //tekst tytulu
    primaryContainer = Color(0xFFCEC7BB), //dodawanie
    surfaceVariant = Color(0xFF807B7B), //scrollup
    surfaceContainerHighest = Color(0xFFFFFDE9), //tlo card
    surfaceContainerHigh = Color(0xFF807B7B), //tlo dialogu
    onPrimaryContainer = Color(0xFF1C1B1B) // kolor ikony plusa
)

private val Theme1ColorScheme = lightColorScheme(
    primary = Color(0xFFCBBCB2), //checkboxy i kursor
    background = Color(0xFFC8D9E6), // tlo
    surface = Color(0xFFC8D9E6), //tlo tego u gory
    surfaceContainer = Color(0xFFA1BBC6), //tlo tego u gory po scrollu
    onSurface = Color(0xFF343434), //tekst
    onBackground = Color(0xFF494949), //tekst tytulu
    primaryContainer = Color(0xFF567C8D), //dodawanie
    surfaceVariant = Color(0xFF6D95A5), //scrollup
    surfaceContainerHighest = Color(0xFFFFFFFF), //tlo card
    surfaceContainerHigh = Color(0xFFD7D3D3), //tlo dialogu
    onPrimaryContainer = Color(0xFFD2D0CE) // kolor ikony plusa
)

private val Theme2ColorScheme = lightColorScheme(
    primary = Color(0xFF788270), //checkboxy i kursor
    background = Color(0xFF8EA58C), // tlo
    surface = Color(0xFF8EA58C), //tlo tego u gory
    surfaceContainer = Color(0xFF738A6E), //tlo tego u gory po scrollu
    onSurface = Color(0xFF2B2525), //tekst
    onBackground = Color(0xFFE8E6E6), //tekst tytulu
    primaryContainer = Color(0xFF344C3D), //dodawanie
    surfaceVariant = Color(0xFF344C3D), //scrollup
    surfaceContainerHighest = Color(0xFFBFCFBB), //tlo card
    surfaceContainerHigh = Color(0xFFD0CFCF), //tlo dialogu
    onPrimaryContainer = Color(0xFFA5A5A2) // kolor ikony plusa
)

private val Theme3ColorScheme = darkColorScheme(
    primary = Color(0xFFC98982), //checkboxy i kursor
    background = Color(0xFF8E4949), // tlo
    surface = Color(0xFF8E4949), //tlo tego u gory
    surfaceContainer = Color(0xFF6D3A3A), //tlo tego u gory po scrollu
    onSurface = Color(0xFFD2D0CE), //tekst
    onBackground = Color(0xFFE8E6E6), //tekst tytulu
    primaryContainer = Color(0xFF6F3232), //dodawanie
    surfaceVariant = Color(0xFF6F3232), //scrollup
    surfaceContainerHighest = Color(0xFF997070), //tlo card
    surfaceContainerHigh = Color(0xFF979595), //tlo dialogu
    onPrimaryContainer = Color(0xFFD2D0CE) // kolor ikony plusa
)

private val Theme4ColorScheme = lightColorScheme(
    primary = Color(0xFFD173E3), //checkboxy i kursor
    background = Color(0xFFBE9EC4), // tlo
    surface = Color(0xFFBE9EC4), //tlo tego u gory
    surfaceContainer = Color(0xFF9E7EA5), //tlo tego u gory po scrollu
    onSurface = Color(0xFF525151), //tekst
    onBackground = Color(0xFFE8E6E6), //tekst tytulu
    primaryContainer = Color(0xFF906B97), //dodawanie
    surfaceVariant = Color(0xFF906B97), //scrollup
    surfaceContainerHighest = Color(0xFFE6CCEE), //tlo card
    surfaceContainerHigh = Color(0xFFC9C9C9), //tlo dialogu
    onPrimaryContainer = Color(0xFFD2D0CE) // kolor ikony plusa
)

@Composable
fun NotesTheme(
    context: Context = LocalContext.current,
    systemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val themeName by context.themeFlow.collectAsState(initial = "system")

    val colorScheme = when(themeName) {
        "system" -> {
            if (systemInDarkTheme) DarkColorScheme
            else LightColorScheme
        }
        "light" -> LightColorScheme
        "dark" -> DarkColorScheme
        "theme1" -> Theme1ColorScheme
        "theme2" -> Theme2ColorScheme
        "theme3" -> Theme3ColorScheme
        "theme4" -> Theme4ColorScheme

        else -> DarkColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}