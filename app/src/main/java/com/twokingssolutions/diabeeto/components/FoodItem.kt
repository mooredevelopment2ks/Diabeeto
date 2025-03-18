package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.model.Food

@Composable
fun FoodItem(
    mySearchResult: Food,
    onFoodItemClick: (Food) -> Unit
    ) {
    Column {
        Spacer(modifier = Modifier.padding(20.dp))
        Card(
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onFoodItemClick(mySearchResult)
                }
        )
        {
            Column(
                Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Id = ${mySearchResult.id}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text = "food item = ${mySearchResult.foodItem}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text = "carbs amount = ${mySearchResult.carbAmount}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

