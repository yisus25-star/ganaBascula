package com.ganabascula.dto.request;

import com.ganabascula.entity.CategoriaAnimal.TipoAnimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoriaAnimalRequestDto {

    @NotNull(message = "El tipo de animal es obligatorio")
    private TipoAnimal tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El peso bruto es obligatorio")
    @Positive(message = "El peso bruto debe ser mayor a 0")
    private BigDecimal pesoBrutoKg;

    @NotNull(message = "El precio por kilo es obligatorio")
    @Positive(message = "El precio por kilo debe ser mayor a 0")
    private BigDecimal precioPorKilo;

    private Boolean aplicaDestare = false;

    private Boolean aplicaBono = false;
}