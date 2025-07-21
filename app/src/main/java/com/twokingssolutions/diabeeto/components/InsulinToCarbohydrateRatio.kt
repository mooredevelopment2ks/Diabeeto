package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twokingssolutions.diabeeto.R
import java.util.Locale

@Composable
fun InsulinToCarbohydrateRatio(
    modifier: Modifier = Modifier,
    initialRatio: Double = 10.0,
    onRatioChanged: (Double) -> Unit
) {
    var carbsPerUnit by remember(initialRatio) { mutableStateOf(initialRatio.toString()) }
    val validCarbsPerUnit = carbsPerUnit.toDoubleOrNull()?.coerceIn(1.0, 50.0) ?: initialRatio

    Column(modifier = modifier) {
        Text(
            text = "Insulin to Carb Ratio",
            fontSize = 20.sp,
            color = colorResource(R.color.secondary_colour),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "1 unit of insulin covers ${String.format(Locale.getDefault(), "%.1f", validCarbsPerUnit)}g of carbs",
            fontSize = 16.sp,
            color = colorResource(R.color.secondary_colour),
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = carbsPerUnit,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() || char == '.' }
                    carbsPerUnit = filtered
                    val newValue = filtered.toDoubleOrNull()?.coerceIn(1.0, 50.0) ?: initialRatio
                    onRatioChanged(newValue)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.RemoveCircleOutline,
                    contentDescription = "Decrease ratio",
                    modifier = Modifier.clickable {
                        val newValue = (carbsPerUnit.toDoubleOrNull() ?: initialRatio) - 1.0
                        if (newValue >= 1.0) {
                            carbsPerUnit = String.format(Locale.getDefault(),"%.1f", newValue)
                            onRatioChanged(newValue)
                        }
                    },
                    tint = colorResource(R.color.secondary_colour)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.AddCircleOutline,
                    contentDescription = "Increase ratio",
                    modifier = Modifier.clickable {
                        val newValue = (carbsPerUnit.toDoubleOrNull() ?: initialRatio) + 1.0
                        if (newValue <= 50.0) {
                            carbsPerUnit = String.format(Locale.getDefault(),"%.1f", newValue)
                            onRatioChanged(newValue)
                        }
                    },
                    tint = colorResource(R.color.secondary_colour)
                )
            }
        }
        Slider(
            value = validCarbsPerUnit.toFloat(),
            onValueChange = {
                carbsPerUnit = String.format(Locale.getDefault(),"%.1f", it)
                onRatioChanged(it.toDouble())
            },
            valueRange = 1f..50f,
            steps = 48,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}