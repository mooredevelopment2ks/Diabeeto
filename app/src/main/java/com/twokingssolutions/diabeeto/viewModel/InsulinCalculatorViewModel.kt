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

    private val _calculatedInsulinAmount = MutableStateFlow(0)
    val calculatedInsulinAmount: StateFlow<Int> = _calculatedInsulinAmount

    private val _selectedFoods = MutableStateFlow<List<Food>>(emptyList())
    val selectedFoods: StateFlow<List<Food>> = _selectedFoods

    private val _foodQuantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val foodQuantities: StateFlow<Map<Int, Int>> = _foodQuantities

    private val _totalCarbAmount = MutableStateFlow(0)
    val totalCarbAmount: StateFlow<Int> = _totalCarbAmount

    init {
        viewModelScope.launch {
            combine(
                _selectedFoods,
                _foodQuantities,
                settingsRepository.carbsPerUnit
            ) { foods, quantities, carbsPerUnit ->
                Triple(foods, quantities, carbsPerUnit)
            }.collect { (foods, quantities, carbsPerUnit) ->
                _currentCarbsPerUnit.value = carbsPerUnit
                val totalCarbs = calculateTotalCarbs(foods, quantities)
                _totalCarbAmount.value = totalCarbs

                if (carbsPerUnit <= 0) {
                    _calculatedInsulinAmount.value = 0
                } else {
                    val exactAmount = totalCarbs.toDouble() / carbsPerUnit.toDouble()
                    _calculatedInsulinAmount.value = kotlin.math.round(exactAmount).toInt()
                }
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

    private fun updateInsulinCalculation(totalCarbs: Int = _totalCarbAmount.value,
                                         carbsPerUnit: Int = _currentCarbsPerUnit.value) {
        if (carbsPerUnit <= 0) {
            _calculatedInsulinAmount.value = 0
            return
        }

        val exactAmount = totalCarbs.toDouble() / carbsPerUnit.toDouble()
        _calculatedInsulinAmount.value = kotlin.math.round(exactAmount).toInt()
    }

    fun refreshCalculations() {
        val foods = _selectedFoods.value
        val quantities = _foodQuantities.value
        _totalCarbAmount.value = calculateTotalCarbs(foods, quantities)
        updateInsulinCalculation()
    }

    fun updateFoodInList(updatedFood: Food) {
        val currentFoods = _selectedFoods.value.toMutableList()
        val index = currentFoods.indexOfFirst { it.id == updatedFood.id }
        if (index != -1) {
            currentFoods[index] = updatedFood
            _selectedFoods.value = currentFoods
        }
    }

    fun addFoodAtPosition(food: Food, position: Int) {
        val currentFoods = _selectedFoods.value.toMutableList()
        val validPosition = position.coerceIn(0, currentFoods.size)

        if (!currentFoods.contains(food)) {
            currentFoods.add(validPosition, food)
            _selectedFoods.value = currentFoods

            val currentQuantities = _foodQuantities.value.toMutableMap()
            currentQuantities[food.id] = 1  // Reset to 1 or use stored quantity
            _foodQuantities.value = currentQuantities
        }
    }
}