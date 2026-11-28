package com.app.corpaliv_grupo1.ui.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.Locale

class ArchivosTest : FunSpec({

    val localOriginal = Locale.getDefault()
    beforeTest { Locale.setDefault(Locale.US) }
    afterTest { Locale.setDefault(localOriginal) }

    test("menos de 1 KB se muestra en bytes") {
        Archivos.formatearTamano(0) shouldBe "0 B"
        Archivos.formatearTamano(1023) shouldBe "1023 B"
    }

    test("entre 1 KB y 1 MB se muestra en KB sin decimales") {
        Archivos.formatearTamano(1024) shouldBe "1 KB"
        Archivos.formatearTamano(850 * 1024L) shouldBe "850 KB"
    }

    test("desde 1 MB se muestra en MB con un decimal") {
        Archivos.formatearTamano(1024L * 1024) shouldBe "1.0 MB"
        Archivos.formatearTamano((1.5 * 1024 * 1024).toLong()) shouldBe "1.5 MB"
    }

    test("el límite es de 5 MB y los formatos incluyen PDF, Word y texto") {
        Archivos.MAX_BYTES shouldBe 5L * 1024 * 1024
        Archivos.TIPOS_PERMITIDOS.toList() shouldBe listOf(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
        )
    }
})