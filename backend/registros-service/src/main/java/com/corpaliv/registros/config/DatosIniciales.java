package com.corpaliv.registros.config;

import com.corpaliv.registros.model.FrecuenciaDonacion;
import com.corpaliv.registros.model.ModalidadColaboracion;
import com.corpaliv.registros.model.Registro;
import com.corpaliv.registros.model.TipoColaborador;
import com.corpaliv.registros.repository.RegistroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final RegistroRepository repositorio;

    public DatosIniciales(RegistroRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }
        repositorio.save(nuevo(TipoColaborador.PERSONA, "Persona de Prueba Uno", null,
                "prueba1@example.com", ModalidadColaboracion.DONACION,
                FrecuenciaDonacion.MENSUAL));
        repositorio.save(nuevo(TipoColaborador.EMPRESA, "Empresa Demo SpA", "Contacto Demo",
                "contacto@example.com", ModalidadColaboracion.ALIANZA, null));
    }

    private Registro nuevo(TipoColaborador tipo, String nombre, String contacto, String correo,
                           ModalidadColaboracion modalidad, FrecuenciaDonacion frecuencia) {
        Registro r = new Registro();
        r.setTipo(tipo);
        r.setNombre(nombre);
        r.setContacto(contacto);
        r.setCorreo(correo);
        r.setModalidad(modalidad);
        r.setFrecuencia(frecuencia);
        r.setMensaje("Registro ficticio de ejemplo");
        r.setFecha(System.currentTimeMillis());
        return r;
    }
}
