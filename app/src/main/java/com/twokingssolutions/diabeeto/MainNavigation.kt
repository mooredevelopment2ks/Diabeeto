package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.twokingssolutions.diabeeto.model.CustomNavType
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.screens.MainScreen
import com.twokingssolutions.diabeeto.screens.SearchResultScreen
import kotlin.reflect.typeOf

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainRoute
    ) {
        composable<NavRoutes.MainRoute> {
            MainScreen(
                onFoodItemClick = { food ->
                    navController.navigate(
                        NavRoutes.SearchResultsRoute(
                            food = food
                        )
                    )
                }
            )
        }
        composable<NavRoutes.SearchResultsRoute>(
            typeMap = mapOf(typeOf<Food>() to CustomNavType.FoodType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.SearchResultsRoute>()
            SearchResultScreen(
                food = arguments.food
            )
        }
    }
}