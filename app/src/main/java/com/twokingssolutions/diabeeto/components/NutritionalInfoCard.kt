package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.db.entity.NutrientValues

@Composable
fun NutritionalInfoCard(
    nutrients: List<NutrientValues>,
    isEditable: Boolean,
    onNutrientsChange: (List<NutrientValues>) -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Nutritional Information", style = MaterialTheme.typography.titleLarge)
            nutrients.forEachIndexed { index, nutrient ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(nutrient.nutrientName ?: "Unknown", modifier = Modifier.weight(1f))
                    EditableTextField(
                        label = "per 100g/ml",
                        value = nutrient.quantityPer100g100ml ?: "0",
                        isEditable = isEditable,
                        onValueChange = {
                            val updatedList = nutrients.toMutableList()
                            updatedList[index] = nutrient.copy(quantityPer100g100ml = it)
                            onNutrientsChange(updatedList)
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    EditableTextField(
                        label = "per Serving",
                        value = nutrient.quantityPerServing ?: "0",
                        isEditable = isEditable,
                        onValueChange = {
                            val updatedList = nutrients.toMutableList()
                            updatedList[index] = nutrient.copy(quantityPerServing = it)
                            onNutrientsChange(updatedList)
                        },
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal
                    )
                }
            }
        }
    }
}