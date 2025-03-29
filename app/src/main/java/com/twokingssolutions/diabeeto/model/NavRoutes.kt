package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    // ListScreen with no arguments
    @Serializable
    data object MainRoute : NavRoutes()

    @Serializable
    data object AddFoodItemRoute : NavRoutes()

    // ListScreen with arguments
    @Serializable
    data class SearchResultsRoute(
        val foods: List<Food>
    ) : NavRoutes()

    @Serializable
    data class ViewFoodItemRoute(
        val food: Food
    ) : NavRoutes()
}