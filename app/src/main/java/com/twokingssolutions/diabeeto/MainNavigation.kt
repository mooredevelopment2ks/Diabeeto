package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.twokingssolutions.diabeeto.model.CustomNavType
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.screens.AddFoodItemScreen
import com.twokingssolutions.diabeeto.screens.MainScreen
import com.twokingssolutions.diabeeto.screens.SearchResultScreen
import com.twokingssolutions.diabeeto.screens.ViewFoodItemScreen
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
                navController,
                onFoodItemClick = { food ->
                    navController.navigate(
                        NavRoutes.ViewFoodItemRoute(
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
                food = arguments.food,
                navController = navController
            )
        }
        composable<NavRoutes.AddFoodItemRoute> {
            AddFoodItemScreen(navController)
        }
        composable<NavRoutes.ViewFoodItemRoute>(
            typeMap = mapOf(typeOf<Food>() to CustomNavType.FoodType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.SearchResultsRoute>()

            ViewFoodItemScreen(
                food = arguments.food,
                navController = navController
            )
        }
    }
}