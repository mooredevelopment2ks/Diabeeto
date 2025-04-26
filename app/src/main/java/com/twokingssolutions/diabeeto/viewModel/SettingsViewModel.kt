package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val carbsPerUnit = repository.carbsPerUnit
        .stateIn(viewModelScope, SharingStarted.Eagerly, 10)

    fun setCarbsPerUnit(value: Int) {
        viewModelScope.launch {
            repository.setCarbsPerUnit(value)
        }
    }
}