package com.corpaliv.registros.dto;

import com.corpaliv.registros.model.EstadoRegistro;
import com.corpaliv.registros.model.FrecuenciaDonacion;
import com.corpaliv.registros.model.ModalidadColaboracion;
import com.corpaliv.registros.model.Registro;
import com.corpaliv.registros.model.TipoColaborador;

public record RegistroResponse(
        Long id,
        TipoColaborador tipo,
        String nombre,
        String contacto,
        String correo,
        ModalidadColaboracion modalidad,
        FrecuenciaDonacion frecuencia,
        String mensaje,
        String cartaNombre,
        EstadoRegistro estado,
        long fecha
) {
    public static RegistroResponse desde(Registro r) {
        return new RegistroResponse(
                r.getId(),
                r.getTipo(),
                r.getNombre(),
                r.getContacto(),
                r.getCorreo(),
                r.getModalidad(),
                r.getFrecuencia(),
                r.getMensaje(),
                r.getCartaNombre(),
                r.getEstado(),
                r.getFecha()
        );
    }
}