package com.twokingssolutions.diabeeto.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.outlined.Image
import com.twokingssolutions.diabeeto.model.Food
import com.twokingssolutions.diabeeto.viewModel.myFoodList
import com.twokingssolutions.diabeeto.components.UploadPhoto

@Composable
fun AddFoodItemScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var foodTitle by remember { mutableStateOf("") }
    var carbAmount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var foodImageUri by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFFFCE3B),
        // padding to make sure the content is not drawn under the system bars or camera cutout. Works only Api 35 and above
        contentWindowInsets = WindowInsets.safeContent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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
                "Add Food Item",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = foodTitle,
                onValueChange = { foodTitle = it },
                label = { Text("Food Title") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFEF7FF),
                    focusedContainerColor = Color(0xFFFEF7FF)
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = carbAmount,
                onValueChange = { carbAmount = it },
                label = { Text("Carb Amount") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFEF7FF),
                    focusedContainerColor = Color(0xFFFEF7FF)
                )
            )
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes on food...") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFEF7FF),
                    focusedContainerColor = Color(0xFFFEF7FF)
                ),
                minLines = 6
            )
            Spacer(modifier = Modifier.height(30.dp))
            UploadPhoto(
                context = context,
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onPhotoUriChanged = { uri ->
                    foodImageUri = uri.toString()
                },
                foodImageUri = foodImageUri
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                ElevatedButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFEF7FF),
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = "Add Image"
                        )
                        Text(
                            text = "Add Image",
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                ElevatedButton(
                    onClick = {
                        val newId = (myFoodList.maxOfOrNull { it.id } ?: 0) + 1
                        myFoodList.add(Food(
                            id = newId,
                            foodItem = foodTitle,
                            carbAmount = carbAmount,
                            notes = notes,
                            imageUri = foodImageUri
                        ))
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFEF7FF),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Save Entry",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}