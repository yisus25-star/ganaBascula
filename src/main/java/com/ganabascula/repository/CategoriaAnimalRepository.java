package com.ganabascula.repository;

import com.ganabascula.entity.CategoriaAnimal;
import com.ganabascula.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaAnimalRepository extends JpaRepository<CategoriaAnimal, Long> {

    List<CategoriaAnimal> findByTransaccion(Transaccion transaccion);

    void deleteByTransaccion(Transaccion transaccion);
}