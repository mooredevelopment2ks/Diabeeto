package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.repository.FoodRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FoodDatabaseViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    private val _foodList = MutableStateFlow<List<Food>>(emptyList())
    val foodList: Flow<List<Food>>
        get() = _foodList

    private val _searchText = MutableStateFlow("")
    val searchText: Flow<String>
        get() = _searchText

    private val _isSearching = MutableStateFlow(false)
    val isSearching: Flow<Boolean>
        get() = _isSearching

    private val _favouriteFoodList = MutableStateFlow<List<Food>>(emptyList())
    val favouriteFoodList: Flow<List<Food>>
        get() = _favouriteFoodList

    init {
        viewModelScope.launch {
            _searchText
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _isSearching.value = true
                    foodRepository.searchFoods(query).also {
                        _isSearching.value = false
                    }
                }
                .collect { foods ->
                    _foodList.value = foods
                }
        }

        viewModelScope.launch {
            foodRepository.getAllFoodsOrderByFoodItem().collect { foods ->
                _foodList.value = foods
            }
        }

        viewModelScope.launch {
            foodRepository.getAllFavourites().collect { foods ->
                _favouriteFoodList.value = foods
            }
        }
    }

    fun onSearchTextChange(newSearchText: String) {
        _searchText.value = newSearchText
        searchFoods(newSearchText)
    }

    fun insertFood(food: Food) {
        viewModelScope.launch {
            foodRepository.insertFood(food)
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            foodRepository.updateFood(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            foodRepository.deleteFood(food)
        }
    }

    private fun searchFoods(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            foodRepository.searchFoods(query).collect { foods ->
                _foodList.value = foods
                _isSearching.value = false
            }
        }
    }
}