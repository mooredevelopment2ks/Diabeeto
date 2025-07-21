package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.components.NutritionalInfoCard
import com.twokingssolutions.diabeeto.components.ProductDetailsCard
import com.twokingssolutions.diabeeto.components.StatementsCard
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductItemScreen(
    navController: NavController,
    queryString: String,
    productDatabaseViewModel: ProductDatabaseViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(queryString) {
        productDatabaseViewModel.getProductById(queryString)
        productDatabaseViewModel.loadAllAllergyStatements()
        productDatabaseViewModel.loadAllDietaryStatements()
    }

    val fullProductDetails by productDatabaseViewModel.selectedProduct.collectAsStateWithLifecycle()
    val allAllergyStatements by productDatabaseViewModel.allAllergyStatements.collectAsStateWithLifecycle()
    val allDietaryStatements by productDatabaseViewModel.allDietaryStatements.collectAsStateWithLifecycle()

    var productState by remember { mutableStateOf<Product?>(null) }
    var nutritionalInfoState by remember { mutableStateOf<NutritionalInformation?>(null) }
    var nutrientValuesState by remember { mutableStateOf<List<NutrientValues>>(emptyList()) }
    var selectedAllergies by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedDiets by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(fullProductDetails) {
        fullProductDetails?.let {
            productState = it.product.copy()
            nutritionalInfoState = it.nutritionalInformation?.copy() ?: NutritionalInformation(
                nutritionalInfoId = UUID.randomUUID().toString(),
                productId = it.product.productId,
                servingSize = null,
                servingsPerPack = null
            )
            nutrientValuesState = it.nutritionalInfoWithValues?.nutrientValues?.map { nv -> nv.copy() } ?: emptyList()
            selectedAllergies = it.allergyStatements.map { allergy -> allergy.allergyStatementId }.toSet()
            selectedDiets = it.dietaryStatements.map { diet -> diet.dietaryStatementId }.toSet()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Product") },
            text = { Text("Are you sure you want to delete this product? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productState?.let {
                            productDatabaseViewModel.deleteProduct(it)
                            navController.popBackStack()
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(fullProductDetails?.product?.name ?: "Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (fullProductDetails?.product?.isEditable == true) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Product")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (fullProductDetails != null) {
                FloatingActionButton(onClick = {
                    if (isEditing) {
                        coroutineScope.launch {
                            try {
                                val currentProduct = productState
                                val currentNutritionalInfo = nutritionalInfoState

                                if (currentProduct != null && currentNutritionalInfo != null) {
                                    productDatabaseViewModel.updateAllProductDetails(
                                        product = currentProduct,
                                        nutritionalInformation = currentNutritionalInfo,
                                        nutrientValues = nutrientValuesState,
                                        allergyIds = selectedAllergies.toList(),
                                        dietaryIds = selectedDiets.toList()
                                    )
                                    navController.popBackStack()
                                } else {
                                    snackbarHostState.showSnackbar(
                                        message = "Product data incomplete, cannot save.",
                                        actionLabel = "Dismiss"
                                    )
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(
                                    message = "Failed to update product: ${e.message}",
                                    actionLabel = "Dismiss"
                                )
                            }
                        }
                    } else {
                        isEditing = true
                    }
                }) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Save Changes" else "Edit"
                    )
                }
            }
        }
    ) { paddingValues ->
        if (productState == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text("Loading product details...")
            }
        } else {
            val product = productState!!
            val isEditable = product.isEditable

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ProductDetailsCard(product, isEditable) { updatedProduct ->
                        productState = updatedProduct
                    }
                }

                item {
                    NutritionalInfoCard(nutrientValuesState, isEditable) { updatedNutrients ->
                        nutrientValuesState = updatedNutrients
                    }
                }

                item {
                    StatementsCard(
                        title = "Allergy Statements",
                        allItems = allAllergyStatements,
                        selectedIds = selectedAllergies,
                        isEditable = isEditable,
                        showAllChips = isEditing,
                        onSelectionChanged = { id, isSelected ->
                            selectedAllergies = if (isSelected) {
                                selectedAllergies + id
                            } else {
                                selectedAllergies - id
                            }
                        },
                        nameExtractor = { it.statementName }
                    )
                }

                item {
                    StatementsCard(
                        title = "Dietary Statements",
                        allItems = allDietaryStatements,
                        selectedIds = selectedDiets,
                        isEditable = isEditable,
                        showAllChips = isEditing,
                        onSelectionChanged = { id, isSelected ->
                            selectedDiets = if (isSelected) {
                                selectedDiets + id
                            } else {
                                selectedDiets - id
                            }
                        },
                        nameExtractor = { it.statementName }
                    )
                }
            }
        }
    }
}
