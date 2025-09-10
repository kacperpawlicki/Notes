package com.example.notes.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.setTheme
import com.example.notes.data.themeFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val onNavigateBack: () -> Unit,
): ViewModel() {

    fun setTheme(context: Context, theme: String) {
        viewModelScope.launch {
            context.setTheme(theme)
        }
    }

    fun getThemeFlow(context: Context) = context.themeFlow

    fun navigateBack() {
        onNavigateBack()
    }
}