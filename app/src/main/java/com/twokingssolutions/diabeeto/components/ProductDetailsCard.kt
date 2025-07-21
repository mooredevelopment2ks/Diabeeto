package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.db.entity.Product

@Composable
fun ProductDetailsCard(
    product: Product,
    isEditable: Boolean,
    onProductChange: (Product) -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Product Information", style = MaterialTheme.typography.titleLarge)
            EditableTextField(
                label = "Name",
                value = product.name,
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(name = it)) }
            )
            EditableTextField(
                label = "Display Name",
                value = product.displayName ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(displayName = it)) }
            )
            EditableTextField(
                label = "Brand",
                value = product.brand ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(brand = it)) }
            )
            EditableTextField(
                label = "Barcode",
                value = product.barcode ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(barcode = it)) }
            )
            EditableTextField(
                label = "Package Size",
                value = product.packageSize ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(packageSize = it)) }
            )
            EditableTextField(
                label = "Cup Measure",
                value = product.cupMeasure ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(cupMeasure = it)) }
            )
            EditableTextField(
                label = "Full Description",
                value = product.fullDescription ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(fullDescription = it)) }
            )
            EditableTextField(
                label = "Ingredients",
                value = product.ingredients ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(ingredients = it)) }
            )
            EditableTextField(
                label = "Storage Instructions",
                value = product.storageInstructions ?: "",
                isEditable = isEditable,
                onValueChange = { onProductChange(product.copy(storageInstructions = it)) }
            )
        }
    }
}