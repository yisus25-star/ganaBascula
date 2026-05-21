package com.ganabascula.repository;

import com.ganabascula.entity.Reporte;
import com.ganabascula.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository
        extends JpaRepository<Reporte, Long> {

    List<Reporte> findByUsuario(Usuario usuario);
}