package com.example.mastermartini.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/** Manejo de las fotos de perfil (cámara y galería) en el almacenamiento interno de la app. */
object PhotoStorage {

    private fun photosDir(context: Context): File =
        File(context.filesDir, "photos").apply { mkdirs() }

    fun newPhotoFile(context: Context): File =
        File(photosDir(context), "profile_${System.currentTimeMillis()}.jpg")

    /** Uri segura (FileProvider) que se entrega a la app de cámara. */
    fun uriFor(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    /** Copia la imagen elegida en la galería al almacenamiento interno. */
    fun importFromGallery(context: Context, uri: Uri): String? = try {
        val target = newPhotoFile(context)
        context.contentResolver.openInputStream(uri)?.use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        }
        target.absolutePath
    } catch (e: Exception) {
        null
    }
}
