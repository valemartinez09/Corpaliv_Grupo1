package corpaliv_api.funcionarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FuncionarioRequest(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 40, message = "El usuario debe tener entre 3 y 40 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9._-]+$",
                message = "El usuario solo admite letras, números, punto, guion y guion bajo")
        String usuario,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre admite hasta 120 caracteres")
        String nombre,

        // Obligatoria al crear; opcional al actualizar (si no viene, no cambia)
        @Size(max = 72, message = "La contraseña admite hasta 72 caracteres")
        String clave,

        Boolean activo
) {
    @Override
    public String toString() {
        return "FuncionarioRequest[usuario=" + usuario + ", nombre=" + nombre + "]";
    }
}