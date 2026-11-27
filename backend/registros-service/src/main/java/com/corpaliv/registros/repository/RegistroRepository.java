package com.corpaliv.registros.repository;


import com.corpaliv.registros.model.EstadoRegistro;
import com.corpaliv.registros.model.Registro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    List<Registro> findAllByOrderByFechaDesc();

    List<Registro> findByEstadoOrderByFechaDesc(EstadoRegistro estado);
}