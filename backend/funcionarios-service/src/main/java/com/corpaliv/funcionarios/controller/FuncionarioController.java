package com.corpaliv.funcionarios.controller;

import com.corpaliv.funcionarios.dto.FuncionarioRequest;
import com.corpaliv.funcionarios.dto.FuncionarioResponse;
import com.corpaliv.funcionarios.dto.LoginRequest;
import com.corpaliv.funcionarios.dto.LoginResponse;
import com.corpaliv.funcionarios.service.FuncionarioService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService servicio;

    public FuncionarioController(FuncionarioService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest datos) {
        return servicio.login(datos);
    }

    @GetMapping
    public List<FuncionarioResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public FuncionarioResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponse> crear(@Valid @RequestBody FuncionarioRequest datos) {
        FuncionarioResponse creado = servicio.crear(datos);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public FuncionarioResponse actualizar(@PathVariable Long id,
                                          @Valid @RequestBody FuncionarioRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}