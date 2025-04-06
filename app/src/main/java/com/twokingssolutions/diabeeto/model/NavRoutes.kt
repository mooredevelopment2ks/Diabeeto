package com.twokingssolutions.diabeeto.model

import com.twokingssolutions.diabeeto.db.Food
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object MainRoute : NavRoutes()

    @Serializable
    data object AddFoodItemRoute : NavRoutes()

    @Serializable
    data class SearchResultsRoute(
        val foods: List<Food>
    ) : NavRoutes()

    @Serializable
    data class ViewFoodItemRoute(
        val food: Food
    ) : NavRoutes()
}