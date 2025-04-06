package com.twokingssolutions.diabeeto.components

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.twokingssolutions.diabeeto.internalStorage.saveImageToInternalStorage

@Composable
fun UploadPhoto(
    context: Context,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onPhotoUriChanged: (Uri) -> Unit,
    foodImageUri: String?
) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let {
                val savedUri = saveImageToInternalStorage(context, it)
                savedUri?.let { uri -> onPhotoUriChanged(uri) }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedUri = saveImageToInternalStorage(context, it)
            savedUri?.let { uri -> onPhotoUriChanged(uri) }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Upload Photo") },
            text = {
                Column {
                    Text(text = "Choose an option")
                    Spacer(modifier = Modifier.height(20.dp))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
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
                    onDismiss()
                    galleryLauncher.launch("image/*")
                }) {
                    Text(text = "Choose from Gallery")
                }
            }
        )
    }

    foodImageUri?.let { uri ->
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .build()
            ),
            contentDescription = "Selected Photo",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}