package com.twokingssolutions.diabeeto.internalStorage

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.util.Log

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "images")
        if (!file.exists()) {
            file.mkdir()
        }
        val imageFile = File(file, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(imageFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        Uri.fromFile(imageFile)
    } catch (e: Exception) {
        Log.e("SaveImage", "Error saving image to internal storage", e)
        null
    }
}