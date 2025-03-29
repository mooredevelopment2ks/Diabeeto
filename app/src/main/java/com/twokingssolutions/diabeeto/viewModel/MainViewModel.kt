package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.model.Food
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class MainViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _foods = MutableStateFlow(myFoodList)
    val foods = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_foods) { text, foods ->
            if (text.isBlank()) {
                foods
            } else {
                foods.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _foods.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun updateFood(food: Food) {
        val index = _foods.value.indexOfFirst { it.id == food.id }
        if (index != -1) {
            _foods.value[index] = food
        }
    }

    fun deleteFood(food: Food) {
        _foods.value.remove(food)
    }
}

// Sample results data
val myFoodList = mutableListOf(
    Food(
        id = 1,
        foodItem = "1 Cup Of Rice Uncooked",
        carbAmount = "120g",
        notes = null,
        imageUri = null
    ),
    Food(
        id = 2,
        foodItem = "1 Cup Of Rice Cooked",
        carbAmount = "60g",
        notes = null,
        imageUri = null
    ),
    Food(
        id = 3,
        foodItem = "1 Cup Of Pasta Uncooked",
        carbAmount = "120g",
        notes = null,
        imageUri = null
    )
)