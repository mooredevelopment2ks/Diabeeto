package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import java.io.File
import androidx.core.net.toUri
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FoodItem(
    navController: NavController,
    food: Food
) {
    val context = LocalContext.current
    var isFavourite by remember { mutableStateOf(false) }
    val foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                if (!isFavourite && !food.isFavourite) {
                    Icon(
                        painter = painterResource(R.drawable.star_outlined_24dp_black),
                        contentDescription = "Add to Favourites",
                        modifier = Modifier.clickable {
                            isFavourite = !isFavourite
                            foodDatabaseViewModel.updateFood(
                                Food(
                                    id = food.id,
                                    foodItem = food.foodItem,
                                    carbAmount = food.carbAmount,
                                    notes = food.notes,
                                    imageUri = food.imageUri,
                                    isFavourite = true
                                )
                            )},
                        tint = colorResource(R.color.inactive_colour)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Remove from Favourites",
                        modifier = Modifier.clickable {
                            isFavourite = !isFavourite
                            foodDatabaseViewModel.updateFood(
                                Food(
                                    id = food.id,
                                    foodItem = food.foodItem,
                                    carbAmount = food.carbAmount,
                                    notes = food.notes,
                                    imageUri = food.imageUri,
                                    isFavourite = false
                                )
                            )},
                        tint = colorResource(R.color.tertiary_colour)
                    )
                }
            }
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


