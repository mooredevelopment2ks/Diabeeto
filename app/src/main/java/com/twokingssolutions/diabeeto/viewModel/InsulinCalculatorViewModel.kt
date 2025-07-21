package com.twokingssolutions.diabeeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twokingssolutions.diabeeto.model.CarbCalculationMode
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.model.ProductCalculationState
import com.twokingssolutions.diabeeto.repository.SettingsRepository
import com.twokingssolutions.diabeeto.util.getCarbsValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class InsulinCalculatorViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _selectedProducts = MutableStateFlow<List<FullProductDetails>>(emptyList())
    val selectedProducts: StateFlow<List<FullProductDetails>> = _selectedProducts.asStateFlow()

    private val _productCalculationStates = MutableStateFlow<Map<String, ProductCalculationState>>(emptyMap())
    val productCalculationStates: StateFlow<Map<String, ProductCalculationState>> = _productCalculationStates.asStateFlow()

    val totalCarbAmount: StateFlow<Double> = combine(
        _selectedProducts,
        _productCalculationStates
    ) { products, states ->
        products.sumOf { product ->
            val state = states[product.product.productId]
            val carbsPerServing = state?.carbsPerServing ?: getCarbsPerServing(product)
            val carbsPer100g100ml = state?.carbsPer100g100ml ?: getCarbsPer100g100ml(product)
            val quantity = state?.quantity ?: 1.0
            val mode = state?.calculationMode ?: CarbCalculationMode.PER_SERVING
            when (mode) {
                CarbCalculationMode.PER_SERVING -> carbsPerServing * quantity
                CarbCalculationMode.PER_100G_100ML -> carbsPer100g100ml * quantity
                CarbCalculationMode.CUSTOM_AMOUNT -> (carbsPer100g100ml / 100.0) * quantity
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    val calculatedInsulinAmount: StateFlow<Double> = combine(
        totalCarbAmount,
        settingsRepository.carbsPerUnit
    ) { totalCarbs, carbsPerUnit ->
        if (carbsPerUnit > 0) totalCarbs / carbsPerUnit else 0.0
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    fun addProduct(fullProductDetails: FullProductDetails) {
        val productId = fullProductDetails.product.productId

        if (_selectedProducts.value.any { it.product.productId == productId }) {
            return
        }

        _selectedProducts.value = _selectedProducts.value + fullProductDetails
        val carbsPerServing = getCarbsPerServing(fullProductDetails)
        val carbsPer100g100ml = getCarbsPer100g100ml(fullProductDetails)
        val calculationState = ProductCalculationState(
            quantity = 1.0,
            carbsPerServing = carbsPerServing,
            carbsPer100g100ml = carbsPer100g100ml,
            calculationMode = CarbCalculationMode.PER_SERVING
        )

        val currentStates = _productCalculationStates.value.toMutableMap()
        currentStates[productId] = calculationState
        _productCalculationStates.value = currentStates
    }

    fun removeProduct(product: com.twokingssolutions.diabeeto.db.entity.Product) {
        val currentProducts = _selectedProducts.value.toMutableList()
        currentProducts.removeAll { it.product.productId == product.productId }
        _selectedProducts.value = currentProducts

        val currentStates = _productCalculationStates.value.toMutableMap()
        currentStates.remove(product.productId)
        _productCalculationStates.value = currentStates
    }

    fun updateProductState(productId: String, newState: ProductCalculationState) {
        val currentStates = _productCalculationStates.value.toMutableMap()
        currentStates[productId] = newState
        _productCalculationStates.value = currentStates
    }

    fun clearSelectedProducts() {
        _selectedProducts.value = emptyList()
        _productCalculationStates.value = emptyMap()
    }


    private fun getCarbsPerServing(product: FullProductDetails): Double {
        return getCarbsValue(product, "perServing")
    }

    private fun getCarbsPer100g100ml(product: FullProductDetails): Double {
        return getCarbsValue(product, "per100g100ml")
    }

    fun addProductAtPosition(fullProductDetails: FullProductDetails, position: Int, state: ProductCalculationState) {
        val currentProducts = _selectedProducts.value.toMutableList()
        val validPosition = position.coerceIn(0, currentProducts.size)
        currentProducts.add(validPosition, fullProductDetails)
        _selectedProducts.value = currentProducts

        val currentStates = _productCalculationStates.value.toMutableMap()
        currentStates[fullProductDetails.product.productId] = state
        _productCalculationStates.value = currentStates
    }
}