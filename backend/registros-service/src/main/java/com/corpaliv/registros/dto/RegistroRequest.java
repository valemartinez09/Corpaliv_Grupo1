package com.corpaliv.registros.dto;

import com.corpaliv.registros.model.FrecuenciaDonacion;
import com.corpaliv.registros.model.ModalidadColaboracion;
import com.corpaliv.registros.model.TipoColaborador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotNull(message = "El tipo es obligatorio")
        TipoColaborador tipo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre admite hasta 120 caracteres")
        String nombre,

        @Size(max = 120, message = "El contacto admite hasta 120 caracteres")
        String contacto,

        @NotBlank(message = "El correo es obligatorio")
        @Size(max = 160, message = "El correo admite hasta 160 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "El correo no tiene un formato válido")
        String correo,

        @NotNull(message = "La modalidad es obligatoria")
        ModalidadColaboracion modalidad,

        FrecuenciaDonacion frecuencia,

        @Size(max = 1000, message = "El mensaje admite hasta 1000 caracteres")
        String mensaje,

        @Size(max = 255, message = "El nombre de la carta admite hasta 255 caracteres")
        String cartaNombre
) {
}