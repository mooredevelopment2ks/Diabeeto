package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twokingssolutions.diabeeto.R

@Composable
fun QuantitySelector(
    quantity: Double,
    onQuantityChanged: (Double) -> Unit
) {
    var text by remember { mutableStateOf(quantity.toString()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = {
                val newQuantity = (quantity - 1.0).coerceAtLeast(0.0)
                text = newQuantity.toString()
                onQuantityChanged(newQuantity)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity",
                tint = colorResource(R.color.secondary_colour)
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                val newQuantity = newValue.toDoubleOrNull()?.coerceIn(0.0, 9999.0) ?: quantity
                onQuantityChanged(newQuantity)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = colorResource(R.color.secondary_colour)
            ),
            modifier = Modifier.width(80.dp),
            singleLine = true,
            visualTransformation = VisualTransformation.None
        )

        IconButton(
            onClick = {
                val newQuantity = quantity + 1.0
                text = newQuantity.toString()
                onQuantityChanged(newQuantity)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity",
                tint = colorResource(R.color.secondary_colour)
            )
        }
    }
}