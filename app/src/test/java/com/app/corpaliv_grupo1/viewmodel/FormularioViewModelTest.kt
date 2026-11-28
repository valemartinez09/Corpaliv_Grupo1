package com.app.corpaliv_grupo1.viewmodel
import io.mockk.clearMocks
import androidx.lifecycle.SavedStateHandle
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.data.remote.dto.RegistroRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.ArchivoAdjunto
import com.app.corpaliv_grupo1.model.EstadoEnvio
import com.app.corpaliv_grupo1.model.EstadoRegistro
import com.app.corpaliv_grupo1.model.FrecuenciaDonacion
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.navigation.Routes
import com.app.corpaliv_grupo1.repository.RegistroRemoteRepository
import com.app.corpaliv_grupo1.repository.Resultado
import com.app.corpaliv_grupo1.ui.utils.Archivos
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class FormularioViewModelTest : FunSpec({

    val repo = mockk<RegistroRemoteRepository>()

    fun respuesta() = RegistroResponse(
        id = 1,
        tipo = TipoColaborador.PERSONA,
        nombre = "Ana Prueba",
        contacto = null,
        correo = "ana@test.cl",
        modalidad = ModalidadColaboracion.DONACION,
        frecuencia = null,
        mensaje = null,
        cartaNombre = null,
        estado = EstadoRegistro.PENDIENTE,
        fecha = 1L
    )

    fun crearVm(
        tipo: String? = null,
        frecuencia: String? = null
    ): FormularioViewModel {
        val args = buildMap<String, Any?> {
            tipo?.let { put(Routes.Formulario.ARG_TIPO, it) }
            frecuencia?.let { put(Routes.Formulario.ARG_FRECUENCIA, it) }
        }
        return FormularioViewModel(SavedStateHandle(args), repo)
    }

    // Deja el formulario de una persona completo y válido
    fun FormularioViewModel.llenarValido() {
        onNombreChange("Ana Prueba")
        onCorreoChange("ana@test.cl")
        onModalidadChange(ModalidadColaboracion.DONACION)
        onTerminosChange(true)
    }

    fun carta(mime: String? = "application/pdf", bytes: Long = 1000L) =
        ArchivoAdjunto("content://doc/1", "carta.pdf", bytes, mime)

    beforeTest {
        clearMocks(repo)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }
    afterTest { Dispatchers.resetMain() }

    // ---------- Estado inicial según la ruta ----------

    test("sin argumentos el tipo por defecto es PERSONA") {
        crearVm().estado.value.tipo shouldBe TipoColaborador.PERSONA
    }

    test("lee el tipo EMPRESA desde la ruta") {
        crearVm(tipo = "EMPRESA").estado.value.tipo shouldBe TipoColaborador.EMPRESA
    }

    test("un tipo inválido en la ruta cae en PERSONA") {
        crearVm(tipo = "OTRO").estado.value.tipo shouldBe TipoColaborador.PERSONA
    }

    test("con frecuencia en la ruta la modalidad queda en DONACION") {
        val s = crearVm(frecuencia = "MENSUAL").estado.value

        s.frecuencia shouldBe FrecuenciaDonacion.MENSUAL
        s.modalidad shouldBe ModalidadColaboracion.DONACION
    }

    test("sin frecuencia la modalidad parte sin elegir") {
        crearVm().estado.value.modalidad shouldBe null
    }

    // ---------- Edición de campos ----------

    test("escribir en un campo limpia su error") {
        val vm = crearVm()
        vm.validar()

        vm.onNombreChange("A")
        vm.onCorreoChange("a")
        vm.onTerminosChange(true)
        vm.onModalidadChange(ModalidadColaboracion.COMPRA)

        val e = vm.estado.value.errores
        e.nombre shouldBe null
        e.correo shouldBe null
        e.terminos shouldBe null
        e.modalidad shouldBe null
    }

    test("onContactoChange y onMensajeChange actualizan el estado") {
        val vm = crearVm()

        vm.onContactoChange("María")
        vm.onMensajeChange("Hola")

        vm.estado.value.contacto shouldBe "María"
        vm.estado.value.mensaje shouldBe "Hola"
    }

    // ---------- Carta de presentación ----------

    test("una carta PDF válida se acepta") {
        val vm = crearVm()

        vm.onCartaSeleccionada(carta())

        vm.estado.value.carta?.nombre shouldBe "carta.pdf"
        vm.estado.value.errores.carta shouldBe null
    }

    test("una carta con formato no permitido se rechaza") {
        val vm = crearVm()

        vm.onCartaSeleccionada(carta(mime = "image/png"))

        vm.estado.value.carta shouldBe null
        vm.estado.value.errores.carta shouldBe R.string.error_carta_tipo
    }

    test("una carta sin tipo MIME se rechaza") {
        val vm = crearVm()

        vm.onCartaSeleccionada(carta(mime = null))

        vm.estado.value.carta shouldBe null
        vm.estado.value.errores.carta shouldBe R.string.error_carta_tipo
    }

    test("una carta de más de 5 MB se rechaza") {
        val vm = crearVm()

        vm.onCartaSeleccionada(carta(bytes = Archivos.MAX_BYTES + 1))

        vm.estado.value.carta shouldBe null
        vm.estado.value.errores.carta shouldBe R.string.error_carta_tamano
    }

    test("una carta de exactamente 5 MB se acepta") {
        val vm = crearVm()

        vm.onCartaSeleccionada(carta(bytes = Archivos.MAX_BYTES))

        vm.estado.value.carta shouldBe carta(bytes = Archivos.MAX_BYTES)
    }

    test("onCartaEliminada quita la carta y su error") {
        val vm = crearVm()
        vm.onCartaSeleccionada(carta(mime = "image/png"))

        vm.onCartaEliminada()

        vm.estado.value.carta shouldBe null
        vm.estado.value.errores.carta shouldBe null
    }

    test("cambiar a una modalidad distinta de voluntariado descarta la carta") {
        val vm = crearVm()
        vm.onModalidadChange(ModalidadColaboracion.VOLUNTARIADO)
        vm.onCartaSeleccionada(carta())

        vm.onModalidadChange(ModalidadColaboracion.DONACION)

        vm.estado.value.carta shouldBe null
    }

    test("en voluntariado la carta se conserva") {
        val vm = crearVm()
        vm.onModalidadChange(ModalidadColaboracion.VOLUNTARIADO)
        vm.onCartaSeleccionada(carta())

        vm.onModalidadChange(ModalidadColaboracion.VOLUNTARIADO)

        vm.estado.value.carta shouldBe carta()
    }

    // ---------- Validación ----------

    test("un formulario vacío tiene errores de nombre, correo, modalidad y términos") {
        val vm = crearVm()

        vm.validar() shouldBe false

        val e = vm.estado.value.errores
        e.nombre shouldBe R.string.error_nombre
        e.correo shouldBe R.string.error_correo
        e.modalidad shouldBe R.string.error_modalidad
        e.terminos shouldBe R.string.error_terminos
        e.contacto shouldBe null   // en PERSONA no se pide contacto
    }

    test("una empresa debe indicar persona de contacto") {
        val vm = crearVm(tipo = "EMPRESA")
        vm.llenarValido()

        vm.validar() shouldBe false
        vm.estado.value.errores.contacto shouldBe R.string.error_contacto

        vm.onContactoChange("María Gómez")
        vm.validar() shouldBe true
    }

    test("un formulario completo de persona es válido") {
        val vm = crearVm()
        vm.llenarValido()

        vm.validar() shouldBe true
    }

    test("correos con formato inválido se rechazan") {
        val vm = crearVm()
        vm.llenarValido()

        listOf("ana", "ana@", "ana@test", "@test.cl", "ana@test.c", "ana test@a.cl").forEach {
            vm.onCorreoChange(it)
            vm.validar() shouldBe false
        }
    }

    test("correos con formato válido se aceptan") {
        val vm = crearVm()
        vm.llenarValido()

        listOf("ana@test.cl", "a.b+c@mi-sitio.org", "  ana@test.cl  ").forEach {
            vm.onCorreoChange(it)
            vm.validar() shouldBe true
        }
    }

    test("un error de carta no bloquea la validación") {
        val vm = crearVm()
        vm.llenarValido()
        vm.onCartaSeleccionada(carta(mime = "image/png"))

        vm.validar() shouldBe true
    }

    // ---------- Envío ----------

    test("enviar con el formulario inválido no llama al servicio") {
        val vm = crearVm()

        vm.enviar()

        coVerify(exactly = 0) { repo.crear(any()) }
        vm.estado.value.envio shouldBe EstadoEnvio.EDITANDO
    }

    test("enviar con éxito marca ENVIADO") {
        coEvery { repo.crear(any()) } returns Resultado.Exito(respuesta())
        val vm = crearVm()
        vm.llenarValido()

        vm.enviar()

        vm.estado.value.envio shouldBe EstadoEnvio.ENVIADO
        vm.estado.value.errorGuardado shouldBe false
    }

    test("enviar arma el request de una persona sin contacto y con datos limpios") {
        val captura = slot<RegistroRequest>()
        coEvery { repo.crear(capture(captura)) } returns Resultado.Exito(respuesta())
        val vm = crearVm(frecuencia = "MENSUAL")
        vm.onNombreChange("  Ana Prueba  ")
        vm.onContactoChange("Alguien")      // en PERSONA debe ignorarse
        vm.onCorreoChange("  ana@test.cl ")
        vm.onMensajeChange("  Hola  ")
        vm.onTerminosChange(true)

        vm.enviar()

        val r = captura.captured
        r.tipo shouldBe TipoColaborador.PERSONA
        r.nombre shouldBe "Ana Prueba"
        r.contacto shouldBe ""
        r.correo shouldBe "ana@test.cl"
        r.modalidad shouldBe ModalidadColaboracion.DONACION
        r.frecuencia shouldBe FrecuenciaDonacion.MENSUAL
        r.mensaje shouldBe "Hola"
        r.cartaNombre shouldBe null
    }

    test("enviar de una empresa incluye el contacto y la carta en voluntariado") {
        val captura = slot<RegistroRequest>()
        coEvery { repo.crear(capture(captura)) } returns Resultado.Exito(respuesta())
        val vm = crearVm(tipo = "EMPRESA")
        vm.onNombreChange("Empresa Ficticia")
        vm.onContactoChange("  María Gómez ")
        vm.onCorreoChange("contacto@empresa.cl")
        vm.onModalidadChange(ModalidadColaboracion.VOLUNTARIADO)
        vm.onCartaSeleccionada(carta())
        vm.onTerminosChange(true)

        vm.enviar()

        val r = captura.captured
        r.tipo shouldBe TipoColaborador.EMPRESA
        r.contacto shouldBe "María Gómez"
        r.modalidad shouldBe ModalidadColaboracion.VOLUNTARIADO
        r.cartaNombre shouldBe "carta.pdf"
    }

    test("si el servicio falla vuelve a EDITANDO con errorGuardado") {
        coEvery { repo.crear(any()) } returns Resultado.Error("fallo")
        val vm = crearVm()
        vm.llenarValido()

        vm.enviar()

        vm.estado.value.envio shouldBe EstadoEnvio.EDITANDO
        vm.estado.value.errorGuardado shouldBe true
    }

    test("después de un error se puede reintentar y se recupera") {
        coEvery { repo.crear(any()) } returns Resultado.Error("fallo")
        val vm = crearVm()
        vm.llenarValido()
        vm.enviar()

        coEvery { repo.crear(any()) } returns Resultado.Exito(respuesta())
        vm.enviar()

        vm.estado.value.envio shouldBe EstadoEnvio.ENVIADO
        vm.estado.value.errorGuardado shouldBe false
    }

    test("un registro ya enviado no se vuelve a enviar") {
        coEvery { repo.crear(any()) } returns Resultado.Exito(respuesta())
        val vm = crearVm()
        vm.llenarValido()
        vm.enviar()

        vm.enviar()

        coVerify(exactly = 1) { repo.crear(any()) }
    }
})