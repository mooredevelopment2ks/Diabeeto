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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.db.entity.NutritionalInformation

@Composable
fun NutritionalInfoInputCard(
    nutritionalInformation: NutritionalInformation,
    onNutritionalInfoChange: (NutritionalInformation) -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Nutritional Information", style = MaterialTheme.typography.titleLarge)
            EditableTextField(
                label = "Serving Size",
                value = nutritionalInformation.servingSize ?: "",
                isEditable = true,
                onValueChange = { onNutritionalInfoChange(nutritionalInformation.copy(servingSize = it)) }
            )
            EditableTextField(
                label = "Servings Per Pack",
                value = nutritionalInformation.servingsPerPack?.toString() ?: "",
                isEditable = true,
                onValueChange = { onNutritionalInfoChange(nutritionalInformation.copy(servingsPerPack = it.toDoubleOrNull())) },
                keyboardType = KeyboardType.Decimal
            )
        }
    }
}