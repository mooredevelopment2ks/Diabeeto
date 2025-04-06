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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import java.io.File
import androidx.core.net.toUri

@Composable
fun FoodItem(
    navController: NavController,
    food: Food
) {
    val context = LocalContext.current
    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white_colour)
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
            food.imageUri.let { uriString ->
                val uri = uriString.toUri()
                val imageFile = File(uri.path ?: "")
                if (imageFile.exists()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(uri)
                                .build()
                        ),
                        contentDescription = "Selected Photo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Image not found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}


