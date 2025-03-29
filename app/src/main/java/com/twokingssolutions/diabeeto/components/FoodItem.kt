package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.model.NavRoutes

@Composable
fun FoodItem(
    navController: NavController,
    food: Food
) {
    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .clickable {
                navController.navigate(NavRoutes.ViewFoodItemRoute(food))
            }
    ) {
        Column(
            Modifier.padding(20.dp)
        ) {
            Text(
                text = "Id = ${food.id}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "food item = ${food.foodItem}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "carbs amount = ${food.carbAmount}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "notes = ${food.notes}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "photo = ${food.imageUri ?: "No photo available"}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            food.imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uri)
                            .build()
                    ),
                    contentDescription = "Selected Photo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

