package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Int,
    val foodItem: String,
    val carbAmount: String
)
