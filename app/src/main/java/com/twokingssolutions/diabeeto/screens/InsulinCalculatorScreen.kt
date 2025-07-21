package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.ProductItem
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.mutableIntStateOf
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.model.ProductCalculationState
import java.util.Locale

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

    val selectedProducts by insulinCalculatorViewModel.selectedProducts.collectAsState()
    val productCalculationStates by insulinCalculatorViewModel.productCalculationStates.collectAsState()
    val totalCarbAmount by insulinCalculatorViewModel.totalCarbAmount.collectAsState()
    val calculatedInsulinAmount by insulinCalculatorViewModel.calculatedInsulinAmount.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var removedProduct by remember { mutableStateOf<FullProductDetails?>(null) }
    var removedProductPosition by remember { mutableIntStateOf(0) }
    var removedProductState by remember { mutableStateOf<ProductCalculationState?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            Text(
                text = "Insulin Calculator",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.secondary_colour),
                modifier = Modifier.padding(16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Carbs:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${String.format(Locale.getDefault(),"%.1f", totalCarbAmount)}g",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Insulin Needed:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${String.format(Locale.getDefault(), "%.1f", calculatedInsulinAmount)} units",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.secondary_colour)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        insulinCalculatorViewModel.clearSelectedProducts()
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
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedProducts.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No products added yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(R.color.secondary_colour)
                    )
                    Text(
                        text = "Add products from the search to calculate insulin",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.secondary_colour)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = selectedProducts,
                        key = { it.product.productId }
                    ) { product ->
                        val calculationState = productCalculationStates[product.product.productId]
                        var isRemoved by remember { mutableStateOf(false) }
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    removedProductPosition = selectedProducts.indexOf(product)
                                    removedProduct = product
                                    removedProductState = productCalculationStates[product.product.productId]
                                    insulinCalculatorViewModel.removeProduct(product.product)

                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Removed ${product.product.name}",
                                            actionLabel = "UNDO",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            removedProduct?.let { productToRestore ->
                                                removedProductState?.let { stateToRestore ->
                                                    insulinCalculatorViewModel.addProductAtPosition(
                                                        productToRestore,
                                                        removedProductPosition,
                                                        stateToRestore
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                        )

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
                                ProductItem(
                                    fullProductDetails = product,
                                    onItemClicked = {
                                        navController.navigate(NavRoutes.ViewProductItemRoute(queryString = it.product.productId))
                                    },
                                    calculationState = calculationState,
                                    onCalculationStateChange = { newState ->
                                        insulinCalculatorViewModel.updateProductState(
                                            product.product.productId,
                                            newState
                                        )
                                    },
                                    showQuantitySelector = true,
                                    showFavoriteIcon = false,
                                    showAddButton = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}