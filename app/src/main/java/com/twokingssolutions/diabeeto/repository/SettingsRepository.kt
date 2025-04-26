package com.twokingssolutions.diabeeto.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private val CARBS_PER_UNIT_KEY = intPreferencesKey("carbs_per_unit")

    val carbsPerUnit: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CARBS_PER_UNIT_KEY] ?: 10
        }

    suspend fun setCarbsPerUnit(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[CARBS_PER_UNIT_KEY] = value
        }
    }
}