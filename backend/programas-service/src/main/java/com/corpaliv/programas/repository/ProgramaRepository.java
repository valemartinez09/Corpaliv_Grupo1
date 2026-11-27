package com.corpaliv.programas.repository;

import com.corpaliv.programas.model.Programa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    List<Programa> findAllByOrderByOrdenAscIdAsc();

    List<Programa> findByActivoTrueOrderByOrdenAscIdAsc();

    boolean existsByTituloIgnoreCase(String titulo);

    boolean existsByTituloIgnoreCaseAndIdNot(String titulo, Long id);
}