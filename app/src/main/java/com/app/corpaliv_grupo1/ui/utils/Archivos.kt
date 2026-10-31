package com.app.corpaliv_grupo1.ui.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.app.corpaliv_grupo1.model.ArchivoAdjunto
import java.util.Locale

/** Utilidades para el documento que la persona adjunta (carta de presentación). */
object Archivos {

    /** Formatos aceptados: PDF, Word (.doc y .docx) y texto plano. */
    val TIPOS_PERMITIDOS: Array<String> = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain"
    )

    /** Tamaño máximo: 5 MB. */
    const val MAX_BYTES: Long = 5L * 1024 * 1024

    /** Lee nombre, tamaño y tipo del documento elegido en el selector del sistema. */
    fun leer(contexto: Context, uri: Uri): ArchivoAdjunto {
        val resolver = contexto.contentResolver
        var nombre = "documento"
        var tamano = 0L

        runCatching {
            resolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val colNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val colTamano = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (colNombre >= 0 && !cursor.isNull(colNombre)) {
                        nombre = cursor.getString(colNombre)
                    }
                    if (colTamano >= 0 && !cursor.isNull(colTamano)) {
                        tamano = cursor.getLong(colTamano)
                    }
                }
            }
        }

        // Si el proveedor no informó el tamaño, se consulta directamente al archivo
        if (tamano <= 0L) {
            tamano = runCatching {
                resolver.openFileDescriptor(uri, "r")?.use { it.statSize } ?: 0L
            }.getOrDefault(0L)
        }

        return ArchivoAdjunto(
            uri = uri.toString(),
            nombre = nombre,
            tamanoBytes = tamano,
            tipoMime = resolver.getType(uri)
        )
    }

    /** Convierte bytes a texto legible: "850 KB", "1,2 MB". */
    fun formatearTamano(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> String.format(Locale.getDefault(), "%.0f KB", bytes / 1024.0)
        else -> String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024.0))
    }
}