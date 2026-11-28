package com.app.corpaliv_grupo1.navigation

import com.app.corpaliv_grupo1.model.FrecuenciaDonacion
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class NavegacionTest : FunSpec({

    test("la ruta del formulario sin frecuencia solo lleva el tipo") {
        Routes.Formulario.crear(TipoColaborador.EMPRESA) shouldBe "formulario/EMPRESA"
    }

    test("la ruta del formulario con frecuencia la agrega como argumento") {
        Routes.Formulario.crear(TipoColaborador.PERSONA, FrecuenciaDonacion.MENSUAL) shouldBe
                "formulario/PERSONA?frecuencia=MENSUAL"
    }

    test("los argumentos de la ruta coinciden con su patrón") {
        Routes.Formulario.route shouldBe "formulario/{tipo}?frecuencia={frecuencia}"
        Routes.Formulario.ARG_TIPO shouldBe "tipo"
        Routes.Formulario.ARG_FRECUENCIA shouldBe "frecuencia"
    }

    test("Screen.Detail reemplaza el id en la ruta") {
        Screen.Detail("42").buildRoute() shouldBe "detail_page/42"
    }

    test("las pantallas fijas tienen su ruta") {
        Screen.Home.route shouldBe "home_page"
        Screen.Profile.route shouldBe "profile_page"
        Screen.Settings.route shouldBe "settings_page"
    }

    test("las personas no pueden elegir alianza y las empresas sí") {
        TipoColaborador.PERSONA.modalidades shouldNotContain ModalidadColaboracion.ALIANZA
        TipoColaborador.EMPRESA.modalidades shouldContain ModalidadColaboracion.ALIANZA
    }

    test("las personas pueden ser voluntarias y las empresas no") {
        TipoColaborador.PERSONA.modalidades shouldContain ModalidadColaboracion.VOLUNTARIADO
        TipoColaborador.EMPRESA.modalidades shouldNotContain ModalidadColaboracion.VOLUNTARIADO
    }
})