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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.Fab
import com.twokingssolutions.diabeeto.components.FilterTextView
import com.twokingssolutions.diabeeto.components.ProductItem
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productDatabaseViewModel: ProductDatabaseViewModel = koinViewModel(),
    insulinCalculatorViewModel: InsulinCalculatorViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
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
    val selectedProducts by insulinCalculatorViewModel.selectedProducts.collectAsStateWithLifecycle()
    val favouriteProducts by productDatabaseViewModel.favouriteProductList.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        floatingActionButton = { Fab(navController) },
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
            FilterTextView(
                navController = navController,
                productDatabaseViewModel = productDatabaseViewModel
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favourite Products",
                    fontSize = 24.sp,
                    color = colorResource(R.color.secondary_colour)
                )
            }
            LazyColumn {
                items(
                    items = favouriteProducts,
                    key = { it.product.productId }
                ) { fullProductDetails ->
                    val isAddedToCalculator = selectedProducts.any { it.product.productId == fullProductDetails.product.productId }
                    ProductItem(
                        fullProductDetails = fullProductDetails,
                        onItemClicked = {
                            navController.navigate(NavRoutes.ViewProductItemRoute(queryString = it.product.productId))
                        },
                        onFavoriteClicked = {
                            val updatedProduct = it.product.copy(isFavourite = !it.product.isFavourite)
                            coroutineScope.launch {
                                productDatabaseViewModel.updateProduct(updatedProduct)
                            }
                        },
                        onAddToInsulinCalculator = { product ->
                            insulinCalculatorViewModel.addProduct(product)
                        },
                        showFavoriteIcon = true,
                        showAddButton = true,
                        isAddedToCalculator = isAddedToCalculator
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}