package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.components.FoodItem
import com.twokingssolutions.diabeeto.model.Food

@Composable
fun MainScreen(
    navController: NavController,
    onFoodItemClick: (Food) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFCE3B),
        floatingActionButton = {
            Fab(navController)
        },
        floatingActionButtonPosition = FabPosition.End,
        contentWindowInsets = WindowInsets.safeContent
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
                    modifier = Modifier
                        .scale(1.5f),
                )
            }
            FilterTextView(navController, mySearchResults)
            LazyColumn {
                items(mySearchResults) { food ->
                    FoodItem(food, onFoodItemClick)
                }
            }
        }
    }
}

// sample results data
val mySearchResults = listOf(
    Food(1, "1 Cup Of Rice Uncooked", "120g"),
    Food(2, "1 Cup Of Rice Cooked", "60g")
)