package com.corpaliv.funcionarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Siempre se guarda en minúsculas, así "Ana" y "ana" son el mismo usuario
    @Column(nullable = false, unique = true, length = 40)
    private String usuario;

    @Column(nullable = false, length = 120)
    private String nombre;

    // Hash BCrypt. Nunca la contraseña en texto plano.
    @Column(nullable = false, length = 100)
    private String claveHash;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private int intentosFallidos;

    // Milisegundos desde 1970 hasta que termina el bloqueo (0 = sin bloqueo)
    @Column(nullable = false)
    private long bloqueadoHasta;

    public Long getId() { return id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClaveHash() { return claveHash; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public long getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(long bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }
}