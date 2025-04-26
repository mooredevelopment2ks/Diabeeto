package com.twokingssolutions.diabeeto.model

import com.twokingssolutions.diabeeto.db.Food
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    //Carb Counter Routes
    @Serializable
    data object CarbCounterRoute : NavRoutes()

    @Serializable
    data object HomeRoute : NavRoutes()

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

    // Insulin Calculator Routes
    @Serializable
    data object InsulinCalculatorRoute : NavRoutes()

    @Serializable
    data object InsulinCalculatorScreenRoute : NavRoutes()

    // Settings Routes
    @Serializable
    data object SettingsRoute : NavRoutes()

    @Serializable
    data object SettingsScreenRoute : NavRoutes()
}