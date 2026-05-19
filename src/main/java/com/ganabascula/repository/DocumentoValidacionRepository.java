package com.ganabascula.repository;

import com.ganabascula.entity.DocumentoValidacion;
import com.ganabascula.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface DocumentoValidacionRepository extends JpaRepository<DocumentoValidacion, Long> {

    Optional<DocumentoValidacion> findByUsuario(Usuario usuario);

    List<DocumentoValidacion> findAllByEstado(DocumentoValidacion.EstadoDocumento estado);
}