package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.twokingssolutions.diabeeto.viewModel.MainViewModel
import kotlin.reflect.typeOf

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MainRoute
    ) {
        composable<NavRoutes.MainRoute> {
            MainScreen(
                navController = navController
            )
        }
        composable<NavRoutes.SearchResultsRoute>(
            typeMap = mapOf(typeOf<List<Food>>() to CustomNavType.FoodListType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.SearchResultsRoute>()
            SearchResultScreen(
                navController = navController,
                foods = arguments.foods
            )
        }
        composable<NavRoutes.AddFoodItemRoute> {
            AddFoodItemScreen(
                navController = navController
            )
        }
        composable<NavRoutes.ViewFoodItemRoute>(
            typeMap = mapOf(typeOf<Food>() to CustomNavType.FoodType)
        ) { backStackEntry ->
            val arguments = backStackEntry.toRoute<NavRoutes.ViewFoodItemRoute>()
            ViewFoodItemScreen(
                navController = navController,
                food = arguments.food,
                mainViewModel = mainViewModel
            )
        }
    }
}