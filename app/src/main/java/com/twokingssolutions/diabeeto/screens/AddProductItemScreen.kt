package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.components.NutritionalInfoCard
import com.twokingssolutions.diabeeto.components.NutritionalInfoInputCard
import com.twokingssolutions.diabeeto.components.ProductDetailsCard
import com.twokingssolutions.diabeeto.components.StatementsCard
import com.twokingssolutions.diabeeto.db.entity.NutrientValues
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation
import com.twokingssolutions.diabeeto.db.entity.Product
import com.twokingssolutions.diabeeto.viewModel.ProductDatabaseViewModel
import java.util.UUID
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductItemScreen(
    navController: NavController,
    productDatabaseViewModel: ProductDatabaseViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productDatabaseViewModel.loadAllAllergyStatements()
        productDatabaseViewModel.loadAllDietaryStatements()
        productDatabaseViewModel.loadAllDepartments()
    }

    val allAllergyStatements by productDatabaseViewModel.allAllergyStatements.collectAsState()
    val allDietaryStatements by productDatabaseViewModel.allDietaryStatements.collectAsState()
    val allDepartments by productDatabaseViewModel.allDepartments.collectAsState()

    var productState by remember {
        mutableStateOf(
            Product(
                productId = UUID.randomUUID().toString(),
                name = "",
                barcode = null,
                displayName = null,
                packageSize = null,
                cupMeasure = null,
                fullDescription = null,
                brand = null,
                ingredients = null,
                storageInstructions = null,
                departmentId = null,
                isFavourite = false,
                isEditable = true
            )
        )
    }
    var nutritionalInfoState by remember {
        mutableStateOf(
            NutritionalInformation(
                nutritionalInfoId = UUID.randomUUID().toString(),
                productId = productState.productId,
                servingSize = null,
                servingsPerPack = null
            )
        )
    }
    var nutrientValuesState by remember {
        mutableStateOf(
            listOf(
                "Energy", "Protein", "Fat, total", "Saturated", "Carbohydrate", "Sugars", "Sodium"
            ).map {
                NutrientValues(
                    nutrientValueId = UUID.randomUUID().toString(),
                    nutritionalInfoId = nutritionalInfoState.nutritionalInfoId,
                    nutrientName = it,
                    quantityPer100g100ml = "",
                    quantityPerServing = ""
                )
            }
        )
    }
    var selectedAllergies by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedDiets by remember { mutableStateOf<Set<String>>(emptySet()) }
    var departmentName by remember { mutableStateOf("") }
    var departmentExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    val departmentId = if (departmentName.isNotBlank()) {
                        productDatabaseViewModel.getOrCreateDepartment(departmentName)
                    } else {
                        null
                    }

                    val finalProduct = productState.copy(departmentId = departmentId)

                    productDatabaseViewModel.insertProduct(
                        product = finalProduct,
                        nutritionalInformation = nutritionalInfoState,
                        nutrientValues = nutrientValuesState,
                        allergyIds = selectedAllergies.toList(),
                        dietaryIds = selectedDiets.toList()
                    )
                    navController.popBackStack()
                }
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save Product")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProductDetailsCard(
                    product = productState,
                    isEditable = true
                ) { updatedProduct ->
                    productState = updatedProduct
                }
            }
            item {
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Department", style = MaterialTheme.typography.titleLarge)
                        ExposedDropdownMenuBox(
                            expanded = departmentExpanded,
                            onExpandedChange = { departmentExpanded = !departmentExpanded }
                        ) {
                            OutlinedTextField(
                                value = departmentName,
                                onValueChange = { departmentName = it },
                                label = { Text("Select or add department") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                            )
                            ExposedDropdownMenu(
                                expanded = departmentExpanded,
                                onDismissRequest = { departmentExpanded = false }
                            ) {
                                allDepartments.forEach { department ->
                                    DropdownMenuItem(
                                        text = { Text(department.departmentName) },
                                        onClick = {
                                            departmentName = department.departmentName
                                            departmentExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                NutritionalInfoInputCard(
                    nutritionalInformation = nutritionalInfoState,
                    onNutritionalInfoChange = { nutritionalInfoState = it }
                )
            }
            item {
                NutritionalInfoCard(
                    nutrients = nutrientValuesState,
                    isEditable = true
                ) { updatedNutrients ->
                    nutrientValuesState = updatedNutrients
                }
            }
            item {
                StatementsCard(
                    title = "Allergy Statements",
                    allItems = allAllergyStatements,
                    selectedIds = selectedAllergies,
                    isEditable = true,
                    showAllChips = true,
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
                    isEditable = true,
                    showAllChips = true,
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
