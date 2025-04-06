package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.twokingssolutions.diabeeto.model.CustomNavType
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.screens.AddFoodItemScreen
import com.twokingssolutions.diabeeto.screens.MainScreen
import com.twokingssolutions.diabeeto.screens.SearchResultScreen
import com.twokingssolutions.diabeeto.screens.ViewFoodItemScreen
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainRoute
    ) {
        composable<NavRoutes.MainRoute> {
            MainScreen(
                navController = navController,
                foodDatabaseViewModel = foodDatabaseViewModel
            )
        }
        composable<NavRoutes.SearchResultsRoute>(
            typeMap = mapOf(typeOf<List<Food>>() to CustomNavType.FoodListType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.SearchResultsRoute>()
            SearchResultScreen(
                navController = navController,
                filteredFoods = arguments.foods
            )
        }
        composable<NavRoutes.AddFoodItemRoute> {
            AddFoodItemScreen(
                navController = navController,
                foodDatabaseViewModel = foodDatabaseViewModel
            )
        }
        composable<NavRoutes.ViewFoodItemRoute>(
            typeMap = mapOf(typeOf<Food>() to CustomNavType.FoodType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.ViewFoodItemRoute>()
            ViewFoodItemScreen(
                navController = navController,
                food = arguments.food
            )
        }
    }
}