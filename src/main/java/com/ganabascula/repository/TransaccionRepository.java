package com.ganabascula.repository;

import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaccionRepository
        extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByUsuario(
            Usuario usuario
    );

    List<Transaccion> findByUsuarioAndFechaBetween(
            Usuario usuario,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    List<Transaccion>
    findByUsuarioAndNombreCompradorContainingIgnoreCase(
            Usuario usuario,
            String nombreComprador
    );

    @Query("""
           SELECT COALESCE(SUM(t.totalNeto), 0)
           FROM Transaccion t
           """)
    Double obtenerIngresosTotales();

    @Query("""
           SELECT COUNT(t)
           FROM Transaccion t
           """)
    Long contarVentas();

    @Query("""
           SELECT COALESCE(SUM(t.totalNeto), 0)
           FROM Transaccion t
           WHERE MONTH(t.fecha) = MONTH(CURRENT_DATE)
           AND YEAR(t.fecha) = YEAR(CURRENT_DATE)
           """)
    Double obtenerVentasMes();
}