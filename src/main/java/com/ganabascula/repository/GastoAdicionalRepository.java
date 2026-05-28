package com.ganabascula.repository;

import com.ganabascula.entity.GastoAdicional;
import com.ganabascula.entity.Transaccion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoAdicionalRepository
        extends JpaRepository<GastoAdicional, Long> {

    List<GastoAdicional> findByTransaccion(
            Transaccion transaccion
    );

    // GASTOS TOTALES
    @Query("""
           SELECT COALESCE(SUM(g.valor),0)
           FROM GastoAdicional g
           """)
    Double obtenerGastosTotales();
}