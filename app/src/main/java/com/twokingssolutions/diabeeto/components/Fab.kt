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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.NavRoutes

@Composable
fun Fab(
    navController: NavController
) {
    FloatingActionButton(
        onClick = {
            navController.navigate(NavRoutes.AddFoodItemRoute)
        },
        shape = RoundedCornerShape(30.dp),
        containerColor = Color.Black,
        contentColor = colorResource(R.color.primary_colour),
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Food Item",
            modifier = Modifier
                .size(50.dp)
        )
    }
}