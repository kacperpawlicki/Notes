package com.example.notes.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.ui.components.ThemePickerShape


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenUi(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ustawienia",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }

    ) { innerPadding ->
        //val fontSizelist: List<Int> = listOf(12,14,16,18,20,22)

        val context = LocalContext.current

        val currentTheme by viewModel.getThemeFlow(context).collectAsState(initial = "system")

        var selectedOption by remember(currentTheme) { mutableStateOf(currentTheme) }

        val radioOptions = listOf("system", "light", "dark")

        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            HorizontalDivider()

//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceAround,
//                modifier = Modifier.padding(top = 30.dp, start = 20.dp)
//            ) {
//                Text(
//                    text = "Rozmiar czcionki:",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                NumberSelector(
//                    selectedNumber = 16,
//                    numbers = fontSizelist,
//                    onNumberSelected = {},
//                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
//                )
//            }
//
            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = "Motyw",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
                fontWeight = FontWeight.SemiBold
            )



            Column(
                modifier = Modifier.padding(start = 30.dp)
            ) {
                radioOptions.forEach() { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedOption = option
                                viewModel.setTheme(context = context, theme = option)
                            }
                            .padding(2.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = {
                                selectedOption = option
                                viewModel.setTheme(context = context, theme = option)
                            }
                        )
                        Text(
                            text = when (option) {
                                "system" -> "Zgodny z motywem urządzenia"
                                "light" -> "Jasny"
                                "dark" -> "Ciemny"
                                else -> ""
                            },
                            fontSize = 16.sp
                        )
                    }
                }

                Text(
                    text = "Wybierz inny motyw",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .fillMaxWidth()
                ) {
                    listOf("theme1", "theme2", "theme3", "theme4").forEach { theme ->
                        ThemePickerShape(
                            size = 60.dp,
                            cornerRadius = 10.dp,
                            color1 = when (theme) {
                                "theme1" -> Color(0xFFC8D9E6)
                                "theme2" -> Color(0xFF8EA58C)
                                "theme3" -> Color(0xFF8E4949)
                                "theme4" -> Color(0xFFBE9EC4)
                                else ->  Color(0xFFFFFFFF)
                            },
                            color2 = when (theme) {
                                "theme1" -> Color(0xFFFFFFFF)
                                "theme2" -> Color(0xFFBFCFBB)
                                "theme3" -> Color(0xFF997070)
                                "theme4" -> Color(0xFFE6CCEE)
                                else ->  Color(0xFFFFFFFF)
                            },
                            borderColor = when (theme) {
                                "theme1" -> Color(0xFFD7E3EE)
                                "theme2" -> Color(0xFF879A84)
                                "theme3" -> Color(0xFFBB7C7C)
                                "theme4" -> Color(0xFFB793BB)
                                else ->  Color(0xFFFFFFFF)
                            },
                            borderWidth = 3.dp,
                            selected = selectedOption == theme,
                            modifier = Modifier.clickable {
                                selectedOption = theme
                                viewModel.setTheme(context = context, theme = theme)
                            }
                        )
                    }
                }
            }
        }
    }
}