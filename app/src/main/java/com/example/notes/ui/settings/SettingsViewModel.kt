package com.example.notes.ui.settings

import androidx.lifecycle.ViewModel

class SettingsViewModel(
    private val onNavigateBack: () -> Unit
): ViewModel() {

    fun navigateBack() {
        onNavigateBack()
    }
}