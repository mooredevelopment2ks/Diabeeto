package com.twokingssolutions.diabeeto.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Fab() {
    FloatingActionButton(
        onClick = {},
        shape = RoundedCornerShape(30.dp),
        containerColor = Color.Black,
        contentColor = Color(0xFFFFCE3B),
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
    }
}