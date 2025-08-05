package com.twokingssolutions.diabeeto.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.model.NavRoutes

@Composable
fun BarcodeScannerIconButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { navController.navigate(NavRoutes.BarcodeScannerRoute) },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_barcode_scanner),
            contentDescription = "Barcode Scanner"
        )
    }
}