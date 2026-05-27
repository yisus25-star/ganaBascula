package com.ganabascula.service;

import com.ganabascula.entity.TipoAnimal;

import java.math.BigDecimal;

public interface CalculoGanaderoService {

    boolean esMacho(TipoAnimal tipoAnimal);

    BigDecimal obtenerPorcentajeDestare(
            Boolean aplicaDestare,
            TipoAnimal tipoAnimal
    );

    BigDecimal calcularPesoNeto(
            BigDecimal pesoBruto,
            BigDecimal porcentajeDestare
    );

    BigDecimal calcularBono(
            Boolean aplicaBono,
            Integer cantidad
    );

    BigDecimal calcularSubtotal(
            BigDecimal pesoNeto,
            BigDecimal precioPorKilo,
            BigDecimal valorBono
    );
}
