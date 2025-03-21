package com.twokingssolutions.diabeeto.screens

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import androidx.compose.material.icons.outlined.Image

@Composable
fun AddFoodItemScreen(navController: NavHostController) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri?.let {
                scope.launch {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                    val bitmap = withContext(Dispatchers.IO) {
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                    photoBitmap = bitmap
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bitmap = withContext(Dispatchers.IO) {
                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                }
                photoBitmap = bitmap
            }
        }
    }

    // Dialog for choosing between camera and gallery
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Upload Photo") },
            text = { Text(text = "Choose an option") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    photoUri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        ContentValues()
                    )
                    photoUri?.let { cameraLauncher.launch(it) }
                }) {
                    Text(text = "Take Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text(text = "Choose from Gallery")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFFFCE3B),
        contentWindowInsets = WindowInsets.safeContent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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
            photoBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Selected Photo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
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
                    onClick = {/*TODO*/},
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