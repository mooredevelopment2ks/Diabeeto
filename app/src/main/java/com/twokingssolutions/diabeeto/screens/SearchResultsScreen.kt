package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.model.ProductsByDepartment
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.ProductItem
import com.twokingssolutions.diabeeto.model.NavRoutes
import com.twokingssolutions.diabeeto.viewModel.InsulinCalculatorViewModel
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavController,
    query: String,
    productDatabaseViewModel: ProductDatabaseViewModel = koinViewModel(),
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
    val searchResults by productDatabaseViewModel.productByQuery.collectAsStateWithLifecycle()
    val isLoadingMore by productDatabaseViewModel.isLoadingMore.collectAsStateWithLifecycle()
    val hasMoreResults by productDatabaseViewModel.hasMoreResults.collectAsStateWithLifecycle()
    val selectedProducts by insulinCalculatorViewModel.selectedProducts.collectAsStateWithLifecycle()

    val isDepartmentSearch = query.startsWith("department:")
    val isBrowseAll = query.isBlank() || query == "browse_all"
    val displayQuery = when {
        isBrowseAll -> "All Products"
        isDepartmentSearch -> query.removePrefix("department:")
        else -> query
    }

    LaunchedEffect(query) {
        when {
            isBrowseAll -> {
                productDatabaseViewModel.loadAllProductsPaginated()
            }
            isDepartmentSearch -> {
                productDatabaseViewModel.onSearchTextChange(query)
            }
            else -> {
                productDatabaseViewModel.onSearchTextChange(query)
            }
        }
    }

    var isGroupingReady by remember { mutableStateOf(false) }
    var groupedResults by remember { mutableStateOf(emptyList<ProductsByDepartment>()) }

    LaunchedEffect(searchResults) {
        isGroupingReady = false
        if (searchResults.isNotEmpty()) {
            val unspecifiedPriority = 1
            val regularPriority = 0
            val grouped = searchResults.groupBy {
                it.department?.departmentName?.takeIf { name -> name.isNotBlank() } ?: "Unspecified"
            }
            .map { (departmentName, productList) ->
                ProductsByDepartment(departmentName, productList)
            }
            .sortedWith(
                compareBy<ProductsByDepartment> { department ->
                    if (department.departmentName == "Unspecified") unspecifiedPriority else regularPriority
                }.thenBy { it.departmentName.lowercase() }
            )
            groupedResults = grouped
        } else {
            groupedResults = emptyList()
        }
        isGroupingReady = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.primary_colour)
                ),
                title = {
                    Text(
                        when {
                            isBrowseAll -> "Browse All Products"
                            isDepartmentSearch -> "Department: $displayQuery"
                            else -> "Search Results for \"$query\""
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
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
            if (!isGroupingReady) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    groupedResults.forEach { departmentGroup ->
                        item {
                            Text(
                                text = departmentGroup.departmentName,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(
                            items = departmentGroup.products,
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
                                    productDatabaseViewModel.updateProductAndRefreshInMemory(updatedProduct)
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

                    if (hasMoreResults) {
                        item {
                            val triggerLoadMore = remember { mutableStateOf(false) }
                            LaunchedEffect(searchResults.size) {
                                if (searchResults.isNotEmpty()) {
                                    triggerLoadMore.value = true
                                }
                            }
                            if (triggerLoadMore.value) {
                                LaunchedEffect(Unit) {
                                    productDatabaseViewModel.loadMoreResults()
                                    triggerLoadMore.value = false
                            }
                            }
                            if (isLoadingMore) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
