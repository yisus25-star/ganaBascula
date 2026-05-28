package com.ganabascula.repository;

import com.ganabascula.entity.DocumentoICA;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoICARepository
        extends JpaRepository<DocumentoICA, Long> {

    List<DocumentoICA> findByUsuario(
            Usuario usuario
    );

    List<DocumentoICA> findByTransaccion(
            Transaccion transaccion
    );
}