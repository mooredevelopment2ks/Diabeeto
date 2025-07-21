package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.NavRoutes

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(R.color.secondary_colour),
    contentColor: Color = colorResource(R.color.white_colour),
    tonalElevation: Dp = NavigationBarDefaults.Elevation
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedRoute = when {
        currentRoute?.contains("CarbCounter") == true -> 0
        currentRoute?.contains("InsulinCalculator") == true -> 1
        currentRoute?.contains("Settings") == true -> 2
        else -> 0
    }

    val items = listOf(
        "Carb Counter",
        "Insulin Calculator",
        "Settings"
    )
    val selectedIcons = listOf(
        Icons.Filled.Fastfood,
        Icons.Filled.Calculate,
        Icons.Filled.SettingsSuggest
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Fastfood,
        Icons.Outlined.Calculate,
        Icons.Outlined.SettingsSuggest
    )

    NavigationBar(
        modifier = modifier.height(120.dp),
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        items.forEachIndexed { index, item ->
            if (index == items.lastIndex) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedRoute == index) selectedIcons[index] else unselectedIcons[index],
                            contentDescription = item
                        )
                    },
                    label = { if (selectedRoute == index) Text(item, color = contentColor) else Text(item) },
                    selected = index == selectedRoute,
                    onClick = {
                        when (index) {
                            0 -> navController.navigate(NavRoutes.ProductItemGraph)
                            1 -> navController.navigate(NavRoutes.InsulinCalculatorGraph) {
                                launchSingleTop = true
                                popUpTo(NavRoutes.ProductItemGraph) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                            2 -> navController.navigate(NavRoutes.SettingsGraph)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = colorResource(R.color.tertiary_colour)
                    )
                )
            } else {
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedRoute == index) selectedIcons[index] else unselectedIcons[index],
                            contentDescription = item
                        )
                    },
                    label = { if (selectedRoute == index) Text(item, color = contentColor) else Text(item) },
                    selected = selectedRoute == index,
                    onClick = {
                        when (index) {
                            0 -> navController.navigate(NavRoutes.ProductItemGraph)
                            1 -> navController.navigate(NavRoutes.InsulinCalculatorGraph)
                            2 -> navController.navigate(NavRoutes.SettingsGraph)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = colorResource(R.color.tertiary_colour)
                    )
                )
                VerticalDivider(
                    thickness = 2.dp,
                    color = Color.Red
                )
            }
        }
    }
}