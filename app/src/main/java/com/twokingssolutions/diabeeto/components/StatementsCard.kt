package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.db.entity.AllergyStatements
import com.twokingssolutions.diabeeto.db.entity.DietaryStatements

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> StatementsCard(
    title: String,
    allItems: List<T>,
    selectedIds: Set<String>,
    isEditable: Boolean,
    showAllChips: Boolean,
    onSelectionChanged: (String, Boolean) -> Unit,
    nameExtractor: (T) -> String
) {
    val displayItems = if (showAllChips) allItems else allItems.filter { item ->
        val id = (item as? AllergyStatements)?.allergyStatementId
            ?: (item as? DietaryStatements)?.dietaryStatementId
            ?: ""
        selectedIds.contains(id)
    }

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                displayItems.forEach { item ->
                    val id = (item as? AllergyStatements)?.allergyStatementId
                        ?: (item as? DietaryStatements)?.dietaryStatementId
                        ?: ""
                    val isSelected = selectedIds.contains(id)
                    FilterChip(
                        selected = isSelected,
                        onClick = { if (isEditable) onSelectionChanged(id, !isSelected) },
                        label = { Text(nameExtractor(item)) },
                        enabled = isEditable,
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null
                    )
                }
            }
        }
    }
}