package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddFoodItemScreen(
    navController: NavController,
    foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
) {
    var foodItem by remember { mutableStateOf("") }
    var carbAmount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
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
        containerColor = colorResource(R.color.primary_colour),
        contentWindowInsets = orientationAwareInsets
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Add Food Item",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = foodItem,
                onValueChange = { foodItem = it },
                label = { Text("Food Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedContainerColor = colorResource(R.color.white_colour),
                    focusedContainerColor = colorResource(R.color.white_colour)
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = carbAmount,
                onValueChange = { carbAmount = it.filter { char -> char.isDigit() } },
                label = { Text("Carb Amount") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedContainerColor = colorResource(R.color.white_colour),
                    focusedContainerColor = colorResource(R.color.white_colour)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes on food...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(R.color.secondary_colour),
                    unfocusedTextColor = colorResource(R.color.secondary_colour),
                    focusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedLabelColor = colorResource(R.color.secondary_colour),
                    unfocusedContainerColor = colorResource(R.color.white_colour),
                    focusedContainerColor = colorResource(R.color.white_colour)
                ),
                minLines = 6
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                ElevatedButton(
                    onClick = {
                        if (foodItem.isNotEmpty() && carbAmount.isNotEmpty()) {
                            foodDatabaseViewModel.insertFood(
                                Food(
                                    foodItem = foodItem,
                                    carbAmount = carbAmount.toInt(),
                                    notes = notes
                                )
                            )
                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in food item and carb amount",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white_colour),
                        contentColor = colorResource(R.color.tertiary_colour)
                    )
                ) {
                    Text(
                        text = "Save Entry",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

        }
    }
}
