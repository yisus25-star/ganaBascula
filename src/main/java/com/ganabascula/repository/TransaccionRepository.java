package com.ganabascula.repository;

import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByUsuario(Usuario usuario);

    List<Transaccion> findByUsuarioAndFechaBetween(
            Usuario usuario,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    List<Transaccion> findByUsuarioAndNombreCompradorContainingIgnoreCase(
            Usuario usuario,
            String nombreComprador
    );
}