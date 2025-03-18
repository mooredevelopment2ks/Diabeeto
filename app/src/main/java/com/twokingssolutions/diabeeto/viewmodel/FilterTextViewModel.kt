package com.twokingssolutions.diabeeto.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterTextViewModel : ViewModel() {
    private val items = listOf(
        "Apple",
        "Banana",
        "Cherry",
        "Date",
        "Elderberry",
        "Fig",
        "Potato",
    )

    private val _filteredItems = MutableStateFlow(items)
    var filteredItems: StateFlow<List<String>> = _filteredItems

    fun filterText(input: String) {
        _filteredItems.value = if (input.isEmpty()) {
            emptyList()
        } else {
            items.filter { it.contains(input, ignoreCase = true) }
        }
    }
}