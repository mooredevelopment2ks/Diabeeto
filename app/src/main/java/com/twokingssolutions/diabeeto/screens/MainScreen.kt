package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.model.Food

@Composable
fun MainScreen(
    onFoodItemClick: (Food) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFCE3B),
        floatingActionButton = {
            Fab()
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        FilterTextView(innerPadding, onFoodItemClick = onFoodItemClick)
    }
}