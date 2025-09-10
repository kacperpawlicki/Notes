package com.example.notes.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_preferences")

val THEME_KEY = stringPreferencesKey("theme_name")

val Context.themeFlow: Flow<String>
    get() = dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "system"
    }

suspend fun Context.setTheme(themeName: String) {
    dataStore.edit { preferences ->
        preferences[THEME_KEY] = themeName
    }
}