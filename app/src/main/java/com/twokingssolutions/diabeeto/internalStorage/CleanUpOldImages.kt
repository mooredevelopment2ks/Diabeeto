package com.twokingssolutions.diabeeto.internalStorage

import android.content.Context
import java.io.File

fun cleanUpOldImages(context: Context) {
    val imagesDir = File(context.filesDir, "images")
    if (imagesDir.exists()) {
        val files = imagesDir.listFiles()
        files?.forEach { file ->
            if (file.isFile && file.name.endsWith(".jpg")) {
                // Add your criteria for deleting old images
                if (file.lastModified() < System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) { // 1 week old
                    file.delete()
                }
            }
        }
    }
}