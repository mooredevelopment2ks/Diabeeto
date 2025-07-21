package com.twokingssolutions.diabeeto.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.db.relation.FullProductDetails
import com.twokingssolutions.diabeeto.util.getCarbsValue
import com.twokingssolutions.diabeeto.model.CarbCalculationMode
import com.twokingssolutions.diabeeto.model.ProductCalculationState
import java.util.Locale

@Composable
fun ProductItem(
    fullProductDetails: FullProductDetails,
    onItemClicked: (FullProductDetails) -> Unit = {},
    onFavoriteClicked: (FullProductDetails) -> Unit = {},
    onAddToInsulinCalculator: (FullProductDetails) -> Unit = {},
    onCalculationStateChange: (ProductCalculationState) -> Unit = {},
    calculationState: ProductCalculationState? = null,
    showFavoriteIcon: Boolean = false,
    showAddButton: Boolean = false,
    showQuantitySelector: Boolean = false,
    isAddedToCalculator: Boolean = false
) {
    val context = LocalContext.current
    var calculationMode by remember { mutableStateOf(calculationState?.calculationMode ?: CarbCalculationMode.PER_SERVING) }
    val carbsPerServing = calculationState?.carbsPerServing ?: getCarbsValue(fullProductDetails, "perServing")
    val carbsPer100g100ml = calculationState?.carbsPer100g100ml ?: getCarbsValue(fullProductDetails, "per100g100ml")
    val quantity = calculationState?.quantity ?: 1.0
    val calculatedCarbs = when (calculationMode) {
        CarbCalculationMode.PER_SERVING -> carbsPerServing * quantity
        CarbCalculationMode.PER_100G_100ML -> carbsPer100g100ml * quantity
        CarbCalculationMode.CUSTOM_AMOUNT -> (carbsPer100g100ml / 100.0) * quantity
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onItemClicked(fullProductDetails) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (showFavoriteIcon) {
                IconButton(onClick = { onFavoriteClicked(fullProductDetails) }) {
                    Icon(
                        imageVector = if (fullProductDetails.product.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = colorResource(id = R.color.secondary_colour)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = fullProductDetails.product.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Carbs: ${String.format(Locale.getDefault(), "%.1f", calculatedCarbs)}g/ml",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.secondary_colour)
                )
                TextButton(
                    onClick = {
                        calculationMode = when (calculationMode) {
                            CarbCalculationMode.PER_SERVING -> CarbCalculationMode.PER_100G_100ML
                            CarbCalculationMode.PER_100G_100ML -> CarbCalculationMode.CUSTOM_AMOUNT
                            CarbCalculationMode.CUSTOM_AMOUNT -> CarbCalculationMode.PER_SERVING
                        }
                        if (calculationState != null) {
                            onCalculationStateChange(
                                calculationState.copy(
                                    calculationMode = calculationMode
                                )
                            )
                        }
                    }
                ) {
                    Text(
                        text = when (calculationMode) {
                            CarbCalculationMode.PER_SERVING -> "Per Serving"
                            CarbCalculationMode.PER_100G_100ML -> "Per 100g/100ml"
                            CarbCalculationMode.CUSTOM_AMOUNT -> "Custom Amount"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.secondary_colour)
                    )
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Toggle view",
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp),
                        tint = colorResource(R.color.secondary_colour)
                    )
                }
            }

            if (showQuantitySelector && calculationState != null) {
                QuantitySelector(
                    quantity = quantity,
                    onQuantityChanged = { newQuantity ->
                        onCalculationStateChange(
                            calculationState.copy(quantity = newQuantity)
                        )
                    }
                )
            }

            if (showAddButton) {
                if (isAddedToCalculator) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Added to calculator",
                        tint = colorResource(id = R.color.secondary_colour)
                    )
                } else {
                    IconButton(onClick = {
                        onAddToInsulinCalculator(fullProductDetails)
                        Toast.makeText(
                            context,
                            "${fullProductDetails.product.name} added to insulin calculator",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add to calculator",
                            tint = colorResource(id = R.color.secondary_colour)
                        )
                    }
                }
            }
        }
    }
}