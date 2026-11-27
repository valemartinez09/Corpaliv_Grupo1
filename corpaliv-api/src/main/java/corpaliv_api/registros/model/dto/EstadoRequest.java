package corpaliv_api.registros.model.dto;

import com.corpaliv.registros.model.EstadoRegistro;
import jakarta.validation.constraints.NotNull;

public record EstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoRegistro estado
) {
}