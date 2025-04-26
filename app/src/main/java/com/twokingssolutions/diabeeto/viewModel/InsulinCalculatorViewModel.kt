package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class InsulinCalculatorViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _currentCarbsPerUnit = MutableStateFlow(0)

    private val _selectedFoods = MutableStateFlow<List<Food>>(emptyList())
    val selectedFoods: StateFlow<List<Food>> = _selectedFoods

    private val _foodQuantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val foodQuantities: StateFlow<Map<Int, Int>> = _foodQuantities

    private val _totalCarbAmount = MutableStateFlow(0)
    val totalCarbAmount: StateFlow<Int> = _totalCarbAmount

    init {
        viewModelScope.launch {
            combine(_selectedFoods, _foodQuantities) { foods, quantities ->
                calculateTotalCarbs(foods, quantities)
            }.collect { totalCarbs ->
                _totalCarbAmount.value = totalCarbs
            }
        }

        viewModelScope.launch {
            settingsRepository.carbsPerUnit.collect { value ->
                _currentCarbsPerUnit.value = value
            }
        }
    }

    private fun calculateTotalCarbs(foods: List<Food>, quantities: Map<Int, Int>): Int {
        return foods.sumOf { food ->
            val quantity = quantities[food.id] ?: 1
            food.carbAmount * quantity
        }
    }

    fun addFood(food: Food) {
        val currentFoods = _selectedFoods.value.toMutableList()
        if (!currentFoods.contains(food)) {
            currentFoods.add(food)
            _selectedFoods.value = currentFoods

            val currentQuantities = _foodQuantities.value.toMutableMap()
            currentQuantities[food.id] = 1
            _foodQuantities.value = currentQuantities
        }
    }

    fun updateFoodQuantity(foodId: Int, quantity: Int) {
        if (quantity > 0) {
            val currentQuantities = _foodQuantities.value.toMutableMap()
            currentQuantities[foodId] = quantity
            _foodQuantities.value = currentQuantities
        }
    }

    fun removeFood(food: Food) {
        val currentFoods = _selectedFoods.value.toMutableList()
        currentFoods.remove(food)
        _selectedFoods.value = currentFoods

        val currentQuantities = _foodQuantities.value.toMutableMap()
        currentQuantities.remove(food.id)
        _foodQuantities.value = currentQuantities
    }

    fun clearSelectedFoods() {
        _selectedFoods.value = emptyList()
        _foodQuantities.value = emptyMap()
    }

    fun calculateInsulinAmount(): Int {
        val carbsPerUnitValue = _currentCarbsPerUnit.value
        if (carbsPerUnitValue <= 0) return 0

        val exactAmount = _totalCarbAmount.value.toDouble() / carbsPerUnitValue.toDouble()
        return kotlin.math.round(exactAmount).toInt()
    }
}