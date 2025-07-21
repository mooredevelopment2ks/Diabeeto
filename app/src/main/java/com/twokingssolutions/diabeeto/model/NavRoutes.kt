package com.twokingssolutions.diabeeto.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    //Product Item Routes
    @Serializable
    data object ProductItemGraph : NavRoutes()

    @Serializable
    data object HomeRoute : NavRoutes()

    @Serializable
    data object AddProductItemRoute : NavRoutes()

    @Serializable
    data class SearchResultsRoute(
        val queryString: String
    ) : NavRoutes()

    @Serializable
    data class ViewProductItemRoute(
        val queryString: String
    ) : NavRoutes()

    // Insulin Calculator Routes
    @Serializable
    data object InsulinCalculatorGraph : NavRoutes()

    @Serializable
    data object InsulinCalculatorScreenRoute : NavRoutes()

    // Settings Routes
    @Serializable
    data object SettingsGraph : NavRoutes()

    @Serializable
    data object SettingsScreenRoute : NavRoutes()
}