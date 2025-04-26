package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.components.FoodItem
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun HomeScreen(
    navController: NavController,
    foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
) {
    val favouriteFoods by foodDatabaseViewModel.favouriteFoodList.collectAsState(initial = emptyList())

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val safeContentInsets = WindowInsets.safeContent
    val orientationAwareInsets = remember(isLandscape) {
        if (isLandscape) {
            safeContentInsets.only(WindowInsetsSides.Horizontal)
        } else {
            safeContentInsets.only(WindowInsetsSides.Vertical)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        floatingActionButton = {
            Fab(navController = navController)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                modifier = Modifier,
                containerColor = colorResource(R.color.secondary_colour),
                contentColor = colorResource(R.color.white_colour),
                tonalElevation = 10.dp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.diabeeto_app_name),
                    contentDescription = "placeholder",
                    tint = Color.Unspecified,
                    modifier = Modifier.scale(3f),
                )
            }
            FilterTextView(navController, foodDatabaseViewModel)
            LazyColumn {
                items(favouriteFoods) { food ->
                    FoodItem(navController, food)
                }
            }
        }
    }
}

