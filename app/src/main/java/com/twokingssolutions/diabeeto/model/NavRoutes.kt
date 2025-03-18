package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    // ListScreen with no arguments
    @Serializable
    data object MainRoute : NavRoutes()

    // ResultsScreen that accepts parameters for search results
    @Serializable
    data class SearchResultsRoute(
        val food: Food
    ) : NavRoutes()
}