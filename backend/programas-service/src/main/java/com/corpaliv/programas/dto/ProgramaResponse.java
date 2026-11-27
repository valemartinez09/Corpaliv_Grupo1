package com.corpaliv.programas.dto;

import com.corpaliv.programas.model.CategoriaPrograma;
import com.corpaliv.programas.model.Programa;

public record ProgramaResponse(
        Long id,
        String titulo,
        String descripcion,
        CategoriaPrograma categoria,
        int orden,
        boolean activo
) {
    public static ProgramaResponse desde(Programa p) {
        return new ProgramaResponse(
                p.getId(),
                p.getTitulo(),
                p.getDescripcion(),
                p.getCategoria(),
                p.getOrden(),
                p.isActivo()
        );
    }
}