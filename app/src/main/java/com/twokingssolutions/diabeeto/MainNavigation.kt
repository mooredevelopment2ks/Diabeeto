package com.twokingssolutions.diabeeto

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.screens.AddProductItemScreen
import com.twokingssolutions.diabeeto.screens.HomeScreen
import com.twokingssolutions.diabeeto.screens.InsulinCalculatorScreen
import com.twokingssolutions.diabeeto.screens.SearchResultScreen
import com.twokingssolutions.diabeeto.screens.SettingsScreen
import com.twokingssolutions.diabeeto.screens.ViewProductItemScreen
import com.twokingssolutions.diabeeto.screens.BarcodeScannerScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.ProductItemGraph
    ) {
        navigation<NavRoutes.ProductItemGraph>(
            startDestination = NavRoutes.HomeRoute
        ) {
            composable<NavRoutes.HomeRoute> {
                HomeScreen(
                    navController = navController
                )
            }
            composable<NavRoutes.BarcodeScannerRoute> {
                BarcodeScannerScreen(
                    navController = navController
                )
            }
            composable<NavRoutes.SearchResultsRoute> { backStackEntry ->
                val arguments = backStackEntry.toRoute<NavRoutes.SearchResultsRoute>()
                SearchResultScreen(
                    navController = navController,
                    query = arguments.queryString
                )
            }
            composable<NavRoutes.AddProductItemRoute> {
                AddProductItemScreen(
                    navController = navController
                )
            }
            composable<NavRoutes.ViewProductItemRoute> { backStackEntry ->
                val arguments = backStackEntry.toRoute<NavRoutes.ViewProductItemRoute>()
                ViewProductItemScreen(
                    navController = navController,
                    queryString = arguments.queryString
                )
            }
        }
        navigation<NavRoutes.InsulinCalculatorGraph>(
            startDestination = NavRoutes.InsulinCalculatorScreenRoute
        ) {
            composable<NavRoutes.InsulinCalculatorScreenRoute> {
                InsulinCalculatorScreen(
                    navController = navController
                )
            }
        }
        navigation<NavRoutes.SettingsGraph>(
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