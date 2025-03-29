package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Int,
    var foodItem: String,
    val carbAmount: String,
    val notes: String? = null,
    val imageUri: String? = null
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        // Remove all non-alphanumeric characters and convert to lowercase
        val alphaNumericQueryOnly = query.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
        val alphaNumericFoodItemSearch = foodItem.replace(Regex("[^A-Za-z0-9]"), "").lowercase()

        return alphaNumericFoodItemSearch.contains(alphaNumericQueryOnly)
    }
}