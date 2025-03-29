package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.viewModel.myFoodList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.twokingssolutions.diabeeto.components.UploadPhoto
import com.twokingssolutions.diabeeto.viewModel.MainViewModel

@Composable
fun ViewFoodItemScreen(
    navController: NavController,
    food: Food,
    mainViewModel: MainViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var foodItem by remember { mutableStateOf(food.foodItem) }
    var carbAmount by remember { mutableStateOf(food.carbAmount) }
    var notes by remember { mutableStateOf(food.notes) }
    var foodImageUri by remember { mutableStateOf(food.imageUri) }
    var editableView by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    Scaffold(
        // padding to make sure the content is not drawn under the system bars or camera cutout. Works only Api 35 and above
        contentWindowInsets = WindowInsets.safeContent,
        containerColor = Color(0xFFFFCE3B)
    ) { innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Carb Amount $carbAmount",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = carbAmount,
                onValueChange = { carbAmount = it },
                readOnly = !editableView,
                modifier = Modifier
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = foodItem,
                onValueChange = { foodItem = it },
                readOnly = !editableView,
                modifier = Modifier
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = notes ?: "notes = null",
                onValueChange = { notes = it },
                readOnly = !editableView,
                modifier = Modifier
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    unfocusedContainerColor = if (!editableView) Color.Transparent else Color.White,
                    focusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                    unfocusedIndicatorColor = if (!editableView) Color.Transparent else Color.Unspecified,
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            UploadPhoto(
                context = context,
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onPhotoUriChanged = { uri ->
                    foodImageUri = uri.toString()
                },
                foodImageUri = foodImageUri
            )
            if (editableView) {
                OutlinedButton(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDB8B00),
                    ),
                    border = BorderStroke(1.dp, Color(0xFFDB8B00)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Change Image")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton (
                    onClick = {
                        val index = myFoodList.indexOfFirst { it.id == food.id }
                        if (index != -1) {
                            myFoodList.removeAt(index)
                            mainViewModel.deleteFood(food)
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDB8B00),
                    ),
                    border = BorderStroke(1.dp, Color(0xFFDB8B00)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Delete")
                }
                FilledTonalButton(
                    onClick = {
                        editableView = !editableView
                        if (!editableView) {
                            val index = myFoodList.indexOfFirst { it.id == food.id }
                            if (index != -1) {
                                myFoodList[index] = food.copy(
                                    foodItem = foodItem,
                                    carbAmount = carbAmount,
                                    notes = notes,
                                    imageUri = foodImageUri
                                )
                                mainViewModel.updateFood(myFoodList[index])
                            }
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        contentColor = Color(0xFFFFCE3B),
                        containerColor = Color(0xFFDB8B00)
                    ),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(if (editableView) "Save" else "Edit")
                }
            }
        }
    }
}