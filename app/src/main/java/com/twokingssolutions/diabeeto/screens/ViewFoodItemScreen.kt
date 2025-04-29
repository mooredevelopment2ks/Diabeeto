package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import org.koin.androidx.compose.koinViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun ViewFoodItemScreen(
    navController: NavController,
    food: Food
) {
    val foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
    val insulinCalculatorViewModel: InsulinCalculatorViewModel = koinViewModel()
    var carbAmount by remember { mutableStateOf(food.carbAmount.toString()) }
    var foodItem by remember { mutableStateOf(food.foodItem) }
    var notes by remember { mutableStateOf(food.notes) }
    var editableView by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

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
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Carb Amount ${carbAmount}g",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = if (editableView) carbAmount else "${carbAmount}g",
                onValueChange = { carbAmount = it.filter { char -> char.isDigit() } },
                readOnly = !editableView,
                modifier = Modifier.focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = {
                    if (editableView) {
                        Text(
                            text = "g",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = foodItem,
                onValueChange = { foodItem = it },
                readOnly = !editableView,
                modifier = Modifier.focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = notes,
                onValueChange = { notes = it },
                readOnly = !editableView,
                modifier = Modifier.focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        foodDatabaseViewModel.deleteFood(food)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(R.color.tertiary_colour),
                    ),
                    border = BorderStroke(1.dp, colorResource(R.color.tertiary_colour)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Delete")
                }
                FilledTonalButton(
                    onClick = {
                        if (editableView && (carbAmount.isEmpty() || foodItem.isEmpty())) {
                            Toast.makeText(
                                context,
                                "Please fill in food item and carb amount",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            editableView = !editableView
                            if (!editableView) {
                                val updatedFood = Food(
                                    id = food.id,
                                    foodItem = foodItem,
                                    carbAmount = carbAmount.toInt(),
                                    notes = notes,
                                    isFavourite = food.isFavourite
                                )
                                foodDatabaseViewModel.updateFood(updatedFood)
                                insulinCalculatorViewModel.updateFoodInList(updatedFood)
                            }
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        contentColor = colorResource(R.color.primary_colour),
                        containerColor = colorResource(R.color.tertiary_colour)
                    ),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(if (editableView) "Save" else "Edit")
                }
            }
        }
    }
}
