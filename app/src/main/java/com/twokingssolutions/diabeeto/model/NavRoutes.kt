package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    // ListScreen with no arguments
    @Serializable
    data object MainRoute : NavRoutes()

    @Serializable
    data object AddFoodItemRoute : NavRoutes()

    // ResultsScreen that accepts parameters for search results
    @Serializable
    data class SearchResultsRoute(
        val food: Food
    ) : NavRoutes()

    @Serializable
    data class ViewFoodItemRoute(
        val food: Food
    ) : NavRoutes()
}