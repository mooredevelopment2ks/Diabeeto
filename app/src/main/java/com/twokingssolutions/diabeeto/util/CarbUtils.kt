package com.twokingssolutions.diabeeto.util

import com.twokingssolutions.diabeeto.db.relation.FullProductDetails

fun getCarbsValue(product: FullProductDetails, field: String): Double {
    val nutritionalInfo = product.nutritionalInfoWithValues
    val carbsNutrient = nutritionalInfo?.nutrientValues?.find { nutrient ->
        nutrient.nutrientName?.lowercase()?.trim() == "carbohydrate"
    }
    val valueString = when (field) {
        "perServing" -> carbsNutrient?.quantityPerServing
        "per100g100ml" -> carbsNutrient?.quantityPer100g100ml
        else -> null
    }
    val numberPattern = "\\d+(?:\\.\\d+)?".toRegex()
    return valueString?.let { numberPattern.find(it)?.value?.toDoubleOrNull() } ?: 0.0
}

