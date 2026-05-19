package com.ganabascula.repository;

import com.ganabascula.entity.Reporte;
import com.ganabascula.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByUsuario(Usuario usuario);

    List<Reporte> findByUsuarioOrderByFechaGeneracionDesc(Usuario usuario);
}