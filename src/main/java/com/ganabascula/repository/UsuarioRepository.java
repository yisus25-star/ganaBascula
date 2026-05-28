package com.ganabascula.repository;

import com.ganabascula.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    @Query("""
           SELECT COUNT(u)
           FROM Usuario u
           """)
    Long contarUsuarios();
}