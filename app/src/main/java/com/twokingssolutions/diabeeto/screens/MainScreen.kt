package com.twokingssolutions.diabeeto.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.components.FoodItem
import com.twokingssolutions.diabeeto.viewModel.myFoodList

@Composable
fun MainScreen(
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // padding to make sure the content is not drawn under the system bars or camera cutout. Works only Api 35 and above
        contentWindowInsets = WindowInsets.safeContent,
        containerColor = Color(0xFFFFCE3B),
        floatingActionButton = {
            Fab(navController =  navController)
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
                    modifier = Modifier
                        .scale(1.5f),
                )
            }
            FilterTextView(navController)
            LazyColumn {
                items(myFoodList) { food ->
                    if (myFoodList.isEmpty()) {
                        Toast.makeText(context, "No food items found", Toast.LENGTH_SHORT).show()
                    } else {
                        FoodItem(navController, food)
                    }
                }
            }
        }
    }
}

