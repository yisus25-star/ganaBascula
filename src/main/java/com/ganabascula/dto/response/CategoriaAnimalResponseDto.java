package com.ganabascula.dto.response;

import com.ganabascula.entity.TipoAnimal;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CategoriaAnimalResponseDto {

    private Long id;
    private TipoAnimal tipo;
    private Integer cantidad;
    private BigDecimal pesoBrutoKg;
    private Boolean aplicaDestare;
    private BigDecimal porcentajeDestare;
    private BigDecimal pesoNetoKg;
    private BigDecimal precioPorKilo;
    private Boolean aplicaBono;
    private BigDecimal valorBono;
    private BigDecimal valorCategoria;
}