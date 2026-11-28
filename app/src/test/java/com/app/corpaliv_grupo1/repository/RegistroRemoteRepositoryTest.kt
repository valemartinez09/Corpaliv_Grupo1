package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.RegistrosApi
import com.app.corpaliv_grupo1.data.remote.dto.EstadoRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.EstadoRegistro
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import java.io.IOException

class RegistroRemoteRepositoryTest : FunSpec({

    val api = mockk<RegistrosApi>()
    val repo = RegistroRemoteRepository(api)

    fun respuesta(id: Long, estado: EstadoRegistro = EstadoRegistro.PENDIENTE) = RegistroResponse(
        id = id,
        tipo = TipoColaborador.PERSONA,
        nombre = "Ana Prueba",
        contacto = null,
        correo = "ana@test.cl",
        modalidad = ModalidadColaboracion.DONACION,
        frecuencia = null,
        mensaje = "Dato ficticio",
        cartaNombre = null,
        estado = estado,
        fecha = 1L
    )

    val datos = RegistroRequest(
        tipo = TipoColaborador.PERSONA,
        nombre = "Ana Prueba",
        contacto = "",
        correo = "ana@test.cl",
        modalidad = ModalidadColaboracion.DONACION,
        mensaje = "Dato ficticio"
    )

    test("listar devuelve los registros del servicio") {
        coEvery { api.listar(null) } returns listOf(respuesta(1), respuesta(2))

        val r = repo.listar()

        r.shouldBeInstanceOf<Resultado.Exito<List<RegistroResponse>>>()
        r.datos.size shouldBe 2
    }

    test("listar con filtro de estado lo envía a la API") {
        coEvery { api.listar(EstadoRegistro.PENDIENTE) } returns listOf(respuesta(1))

        repo.listar(EstadoRegistro.PENDIENTE)

        coVerify { api.listar(EstadoRegistro.PENDIENTE) }
    }

    test("listar con la red caída devuelve error de conexión") {
        coEvery { api.listar(null) } throws IOException("sin red")

        val r = repo.listar()

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "No se pudo conectar con el servidor"
    }

    test("obtener devuelve el registro pedido") {
        coEvery { api.obtener(7) } returns respuesta(7)

        val r = repo.obtener(7)

        r.shouldBeInstanceOf<Resultado.Exito<RegistroResponse>>()
        r.datos.id shouldBe 7
    }

    test("crear envía los datos y devuelve el registro guardado") {
        coEvery { api.crear(datos) } returns respuesta(10)

        val r = repo.crear(datos)

        r.shouldBeInstanceOf<Resultado.Exito<RegistroResponse>>()
        r.datos.id shouldBe 10
        coVerify { api.crear(datos) }
    }

    test("actualizar envía el id y los datos") {
        coEvery { api.actualizar(3, datos) } returns respuesta(3)

        val r = repo.actualizar(3, datos)

        r.shouldBeInstanceOf<Resultado.Exito<RegistroResponse>>()
        coVerify { api.actualizar(3, datos) }
    }

    test("marcarContactado envía el estado CONTACTADO") {
        coEvery { api.cambiarEstado(4, EstadoRequest(EstadoRegistro.CONTACTADO)) } returns
                respuesta(4, EstadoRegistro.CONTACTADO)

        val r = repo.marcarContactado(4)

        r.shouldBeInstanceOf<Resultado.Exito<RegistroResponse>>()
        r.datos.estado shouldBe EstadoRegistro.CONTACTADO
    }

    test("eliminar llama al endpoint con el id") {
        coEvery { api.eliminar(5) } just Runs

        val r = repo.eliminar(5)

        r.shouldBeInstanceOf<Resultado.Exito<Unit>>()
        coVerify { api.eliminar(5) }
    }
})