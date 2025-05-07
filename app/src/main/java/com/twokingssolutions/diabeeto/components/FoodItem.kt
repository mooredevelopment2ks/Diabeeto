package com.twokingssolutions.diabeeto.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.Food
import com.twokingssolutions.diabeeto.model.NavRoutes
import androidx.navigation.compose.currentBackStackEntryAsState
import com.twokingssolutions.diabeeto.viewModel.FoodDatabaseViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItem(
    navController: NavController,
    food: Food,
    quantity: Int = 1,
    onQuantityChanged: (Int) -> Unit = {},
    addFoodItemToInsulinCalcScreen: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    var isFavourite by remember { mutableStateOf(food.isFavourite) }
    val foodDatabaseViewModel: FoodDatabaseViewModel = koinViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedRoute = when {
        currentRoute?.contains("HomeRoute") == true -> 0
        currentRoute?.contains("InsulinCalculator") == true -> 1
        currentRoute?.contains("SearchResultsRoute") == true -> 2
        else -> 0
    }
    var isRemoved by remember { mutableStateOf(false) }
    var isAdded by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                isAdded = false
                onRemove()
                true
            } else {
                false
            }
        }
    )
    val options = listOf("Per 100g", "Serving Size")
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(options[0]) }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 500),
            shrinkTowards = Alignment.Top
        ) + fadeOut(),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                val color by animateColorAsState(
                    when (dismissState.dismissDirection) {
                        SwipeToDismissBoxValue.EndToStart -> Color.Red
                        SwipeToDismissBoxValue.Settled -> Color.LightGray
                        else -> Color.Transparent
                    },
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            },
            enableDismissFromStartToEnd = false
        ) {
            Card(
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white_colour)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    if (selectedRoute != 1) {
                        if (!isFavourite && !food.isFavourite) {
                            Icon(
                                painter = painterResource(R.drawable.star_outlined_24dp_black),
                                contentDescription = "Add to Favourites",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        isFavourite = !isFavourite
                                        foodDatabaseViewModel.updateFood(
                                            Food(
                                                id = food.id,
                                                foodItem = food.foodItem,
                                                carbAmount = food.carbAmount,
                                                notes = food.notes,
                                                isFavourite = true
                                            )
                                        )
                                    },
                                tint = colorResource(R.color.inactive_colour)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Remove from Favourites",
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        isFavourite = !isFavourite
                                        foodDatabaseViewModel.updateFood(
                                            Food(
                                                id = food.id,
                                                foodItem = food.foodItem,
                                                carbAmount = food.carbAmount,
                                                notes = food.notes,
                                                isFavourite = false
                                            )
                                        )
                                    },
                                tint = colorResource(R.color.tertiary_colour)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                            .then(
                                if (selectedRoute != 1) {
                                    Modifier.clickable {
                                        navController.navigate(NavRoutes.ViewFoodItemRoute(food))
                                    }
                                } else {
                                    Modifier
                                }
                            )
                    ) {
                        Text(
                            text = food.foodItem,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .then(
                                    if (selectedRoute == 1) {
                                        Modifier.clickable {
                                            navController.navigate(NavRoutes.ViewFoodItemRoute(food))
                                        }
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                        if (selectedRoute != 1) {
                            Text(
                                text = "${food.carbAmount}g",
                                fontSize = 18.sp
                            )
                        } else {
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.menuAnchor(
                                        MenuAnchorType.PrimaryNotEditable,
                                        true
                                    )
                                ) {
                                    if (expanded) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown Icon",
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                            contentDescription = "Dropdown Icon",
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = "${food.carbAmount}g",
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = text,
                                        fontSize = 18.sp,
                                        fontStyle = FontStyle.Italic,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                    )
                                }
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    options.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                text = option
                                                expanded = false
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (selectedRoute == 1) {
                        Row(
                            modifier = Modifier
                                .weight(0.40f)
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.RemoveCircleOutline,
                                contentDescription = "Minus Food Quantity",
                                modifier = Modifier.clickable {
                                    if (quantity >= 2) onQuantityChanged(quantity - 1)
                                }
                            )
                            Text(
                                text = "x$quantity",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Icon(
                                imageVector = Icons.Outlined.AddCircleOutline,
                                contentDescription = "Add Food Quantity",
                                modifier = Modifier.clickable {
                                    onQuantityChanged(quantity + 1)
                                }
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(0.25f)
                                .fillMaxHeight()
                                .background(if (isAdded) Color(0xFF2E7D32) else colorResource(R.color.tertiary_colour))
                                .clickable {
                                    if (!isAdded) {
                                        isAdded = true
                                        addFoodItemToInsulinCalcScreen()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAdded) Icons.Filled.Check else Icons.Filled.Add,
                                contentDescription = if (isAdded) "Added" else "Add",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}