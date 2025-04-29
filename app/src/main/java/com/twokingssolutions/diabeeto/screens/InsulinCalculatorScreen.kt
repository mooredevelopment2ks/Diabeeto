package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.FoodItem
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun InsulinCalculatorScreen(
    navController: NavController,
    insulinCalculatorViewModel: InsulinCalculatorViewModel = koinViewModel()
) {
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
    val vmSelectedFoods by insulinCalculatorViewModel.selectedFoods.collectAsState()
    val foodQuantities by insulinCalculatorViewModel.foodQuantities.collectAsState()
    val totalCarbAmount by insulinCalculatorViewModel.totalCarbAmount.collectAsState()
    val calculatedInsulin by insulinCalculatorViewModel.calculatedInsulinAmount.collectAsState()

    var removedFood by remember { mutableStateOf<Food?>(null) }
    var removedFoodQuantity by remember { mutableIntStateOf(1) }
    var removedFoodPosition by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        insulinCalculatorViewModel.refreshCalculations()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                modifier = Modifier,
                containerColor = colorResource(R.color.secondary_colour),
                contentColor = colorResource(R.color.white_colour),
                tonalElevation = 10.dp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Carbs",
                    color = colorResource(R.color.secondary_colour),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = "${totalCarbAmount}g",
                    color = colorResource(R.color.secondary_colour),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Insulin",
                    color = colorResource(R.color.secondary_colour),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = "$calculatedInsulin units",
                    color = colorResource(R.color.secondary_colour),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        insulinCalculatorViewModel.clearSelectedFoods()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.tertiary_colour),
                        contentColor = colorResource(R.color.secondary_colour)
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Clear All Foods",
                        color = colorResource(R.color.secondary_colour),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
            LazyColumn {
                items(
                    items = vmSelectedFoods,
                    key = { food -> food.id }
                ) { food ->
                    FoodItem(
                        navController = navController,
                        food = food,
                        quantity = foodQuantities[food.id] ?: 1,
                        onQuantityChanged = { newQuantity ->
                            insulinCalculatorViewModel.updateFoodQuantity(food.id, newQuantity)
                        },
                        onRemove = {
                            removedFoodPosition = vmSelectedFoods.indexOf(food)
                            removedFood = food
                            removedFoodQuantity = foodQuantities[food.id] ?: 1

                            insulinCalculatorViewModel.removeFood(food)
                            insulinCalculatorViewModel.refreshCalculations()

                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Removed ${food.foodItem}",
                                    actionLabel = "UNDO",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    removedFood?.let { foodToRestore ->
                                        insulinCalculatorViewModel.addFoodAtPosition(
                                            foodToRestore,
                                            removedFoodPosition
                                        )
                                        insulinCalculatorViewModel.updateFoodQuantity(
                                            foodToRestore.id,
                                            removedFoodQuantity
                                        )
                                        insulinCalculatorViewModel.refreshCalculations()
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}