package corpaliv_api.programas.model.dto;

import com.corpaliv.programas.model.CategoriaPrograma;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProgramaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100, message = "El título admite hasta 100 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 600, message = "La descripción admite hasta 600 caracteres")
        String descripcion,

        @NotNull(message = "La categoría es obligatoria")
        CategoriaPrograma categoria,

        @Min(value = 0, message = "El orden no puede ser negativo")
        Integer orden,

        Boolean activo
) {
}