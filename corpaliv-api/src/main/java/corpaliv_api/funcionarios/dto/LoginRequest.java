package corpaliv_api.funcionarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(max = 40, message = "El usuario admite hasta 40 caracteres")
        String usuario,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 72, message = "La contraseña admite hasta 72 caracteres")
        String clave
) {
    @Override
    public String toString() {
        return "LoginRequest[usuario=" + usuario + "]";
    }
}