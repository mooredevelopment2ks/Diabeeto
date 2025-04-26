package com.twokingssolutions.diabeeto.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.R
import com.twokingssolutions.diabeeto.components.BottomNavBar
import com.twokingssolutions.diabeeto.components.InsulinToCarbohydrateRatio
import com.twokingssolutions.diabeeto.viewModel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val safeContentInsets = WindowInsets.safeContent
    val orientationAwareInsets = remember(isLandscape) {
        if (isLandscape) {
            safeContentInsets.only(WindowInsetsSides.Horizontal)
        } else {
            safeContentInsets.only(WindowInsetsSides.Vertical)
        }
    }
    var insulinType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val insulinTypes = listOf(
        "Rapid-acting (Humalog/NovoLog/Apidra)",
        "Short-acting (Regular/Humulin R)",
        "Intermediate-acting (NPH/Humulin N)",
        "Long-acting (Lantus/Levemir)",
        "Ultra-long-acting (Tresiba/Toujeo)",
        "Premixed (Humulin 70/30)"
    )
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val carbsPerInsulinUnit by settingsViewModel.carbsPerUnit.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = orientationAwareInsets,
        containerColor = colorResource(R.color.primary_colour),
        bottomBar = {
            BottomNavBar(
                navController = navController,
                modifier = Modifier,
                containerColor = colorResource(R.color.secondary_colour),
                contentColor = colorResource(R.color.white_colour),
                tonalElevation = 10.dp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Insulin Type",
                fontSize = 20.sp,
                color = colorResource(R.color.secondary_colour),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = insulinType,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select insulin type") },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    insulinTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                insulinType = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            InsulinToCarbohydrateRatio(
                initialRatio = carbsPerInsulinUnit,
                onRatioChanged = { newRatio ->
                    settingsViewModel.setCarbsPerUnit(newRatio)
                }
            )
        }
    }
}