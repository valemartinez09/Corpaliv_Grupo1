package com.corpaliv.registros.controller;


import com.corpaliv.registros.dto.EstadoRequest;
import com.corpaliv.registros.dto.RegistroRequest;
import com.corpaliv.registros.dto.RegistroResponse;
import com.corpaliv.registros.model.EstadoRegistro;
import com.corpaliv.registros.service.RegistroService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService servicio;

    public RegistroController(RegistroService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<RegistroResponse> listar(@RequestParam(required = false) EstadoRegistro estado) {
        return servicio.listar(estado);
    }

    @GetMapping("/{id}")
    public RegistroResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<RegistroResponse> crear(@Valid @RequestBody RegistroRequest datos) {
        RegistroResponse creado = servicio.crear(datos);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public RegistroResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody RegistroRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @PatchMapping("/{id}/estado")
    public RegistroResponse cambiarEstado(@PathVariable Long id,
                                          @Valid @RequestBody EstadoRequest datos) {
        return servicio.cambiarEstado(id, datos.estado());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}