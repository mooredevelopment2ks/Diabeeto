package com.twokingssolutions.diabeeto.model

data class ProductCalculationState(
    val quantity: Double = 1.0,
    val carbsPerServing: Double = 0.0,
    val carbsPer100g100ml: Double = 0.0,
    val calculationMode: CarbCalculationMode = CarbCalculationMode.PER_SERVING
)
