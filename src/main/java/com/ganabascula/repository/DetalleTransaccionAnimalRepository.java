package com.ganabascula.repository;

import com.ganabascula.entity.DetalleTransaccionAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleTransaccionAnimalRepository
        extends JpaRepository<DetalleTransaccionAnimal, Long> {
}