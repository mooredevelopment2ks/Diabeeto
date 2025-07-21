package com.twokingssolutions.diabeeto.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private val CARBS_PER_UNIT_KEY = doublePreferencesKey("carbs_per_unit")

    val carbsPerUnit: Flow<Double> = context.dataStore.data
        .map { preferences ->
            val key = "carbs_per_unit"
            val value = preferences.asMap().entries.find { it.key.name == key }?.value
            when (value) {
                is Double -> value
                is Int -> value.toDouble()
                else -> 10.0
            }
        }

    suspend fun setCarbsPerUnit(value: Double) {
        context.dataStore.edit { preferences ->
            preferences[CARBS_PER_UNIT_KEY] = value
        }
    }
}