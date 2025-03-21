package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.model.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTextView(
    navController: NavController,
    foodItems: List<Food>
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                inputText = it
                expanded = true
            },
            label = { Text("Input Text") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                inputText = ""
                                expanded = false
                            }
                    )
                }
            }
        )
        val filteredFoodItems = foodItems.filter { it.foodItem.contains(inputText, ignoreCase = true) }
        if (filteredFoodItems.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                filteredFoodItems.forEach { foodItem ->
                    DropdownMenuItem(
                        text = { Text(foodItem.foodItem) },
                        onClick = {
                            navController.navigate(NavRoutes.SearchResultsRoute(foodItem))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}