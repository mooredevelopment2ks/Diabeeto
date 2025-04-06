package com.twokingssolutions.diabeeto.screens

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
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.components.FoodItem
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    navController: NavController,
    foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
) {
    val foods by foodDatabaseViewModel.foodList.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeContent,
        containerColor = colorResource(R.color.primary_colour),
        floatingActionButton = {
            Fab(navController = navController)
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "placeholder",
                    tint = Color.Unspecified,
                    modifier = Modifier.scale(1.5f),
                )
            }
            FilterTextView(navController, foodDatabaseViewModel)
            LazyColumn {
                items(foods) { food ->
                    FoodItem(navController, food)
                }
            }
        }
    }
}

