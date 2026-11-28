package com.app.corpaliv_grupo1.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ResultadoTest : FunSpec({

    test("devuelve Exito cuando la llamada funciona") {
        val r = llamarApi { 42 }
        r shouldBe Resultado.Exito(42)
    }

    test("HttpException con cuerpo ApiError entrega el mensaje del servidor") {
        val cuerpo = """{"estado":401,"mensaje":"Credenciales inválidas","detalles":{}}"""
            .toResponseBody("application/json".toMediaType())
        val r = llamarApi<Int> { throw HttpException(Response.error<Any>(401, cuerpo)) }

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "Credenciales inválidas"
        r.codigo shouldBe 401
    }

    test("HttpException con cuerpo ilegible usa mensaje genérico con el código") {
        val cuerpo = "no es json".toResponseBody("text/plain".toMediaType())
        val r = llamarApi<Int> { throw HttpException(Response.error<Any>(500, cuerpo)) }

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "Error del servidor (500)"
        r.codigo shouldBe 500
    }

    test("IOException se traduce a error de conexión") {
        val r = llamarApi<Int> { throw IOException("sin red") }

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "No se pudo conectar con el servidor"
    }

    test("una excepción inesperada no tumba la app") {
        val r = llamarApi<Int> { throw IllegalStateException("json roto") }

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "Respuesta inesperada del servidor"
    }
})