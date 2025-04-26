package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.twokingssolutions.diabeeto.model.CustomNavType
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.screens.AddFoodItemScreen
import com.twokingssolutions.diabeeto.screens.HomeScreen
import com.twokingssolutions.diabeeto.screens.InsulinCalculatorScreen
import com.twokingssolutions.diabeeto.screens.SearchResultScreen
import com.twokingssolutions.diabeeto.screens.SettingsScreen
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
        startDestination = NavRoutes.CarbCounterRoute
    ) {
        navigation<NavRoutes.CarbCounterRoute>(
            startDestination = NavRoutes.HomeRoute
        ) {
            composable<NavRoutes.HomeRoute> {
                HomeScreen(
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
        navigation<NavRoutes.InsulinCalculatorRoute>(
            startDestination = NavRoutes.InsulinCalculatorScreenRoute
        ) {
            composable<NavRoutes.InsulinCalculatorScreenRoute> {
                InsulinCalculatorScreen(
                    navController = navController
                )
            }
        }
        navigation<NavRoutes.SettingsRoute>(
            startDestination = NavRoutes.SettingsScreenRoute
        ) {
            composable<NavRoutes.SettingsScreenRoute> {
                SettingsScreen(
                    navController = navController
                )
            }
        }
    }
}