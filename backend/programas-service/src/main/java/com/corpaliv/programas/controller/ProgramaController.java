package com.corpaliv.programas.controller;

import com.corpaliv.programas.dto.ProgramaRequest;
import com.corpaliv.programas.dto.ProgramaResponse;
import com.corpaliv.programas.service.ProgramaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    private final ProgramaService servicio;

    public ProgramaController(ProgramaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProgramaResponse> listar(
            @RequestParam(defaultValue = "false") boolean soloActivos) {
        return servicio.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public ProgramaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ProgramaResponse> crear(@Valid @RequestBody ProgramaRequest datos) {
        ProgramaResponse creado = servicio.crear(datos);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ProgramaResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProgramaRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
