package com.ganabascula.service.impl;

import com.ganabascula.entity.TipoAnimal;
import com.ganabascula.service.CalculoGanaderoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculoGanaderoServiceImpl
        implements CalculoGanaderoService {

    private static final BigDecimal BONO_POR_ANIMAL =
            new BigDecimal("42800");

    private static final BigDecimal DESTARE_MACHOS =
            new BigDecimal("0.05");

    private static final BigDecimal DESTARE_HEMBRAS =
            new BigDecimal("0.06");

    @Override
    public boolean esMacho(TipoAnimal tipoAnimal) {

        return tipoAnimal == TipoAnimal.BECERRO
                || tipoAnimal == TipoAnimal.NOVILLO
                || tipoAnimal == TipoAnimal.TORO;
    }

    @Override
    public BigDecimal obtenerPorcentajeDestare(
            Boolean aplicaDestare,
            TipoAnimal tipoAnimal
    ) {

        if (!aplicaDestare) {
            return BigDecimal.ZERO;
        }

        return esMacho(tipoAnimal)
                ? DESTARE_MACHOS
                : DESTARE_HEMBRAS;
    }

    @Override
    public BigDecimal calcularPesoNeto(
            BigDecimal pesoBruto,
            BigDecimal porcentajeDestare
    ) {

        return pesoBruto.subtract(
                pesoBruto.multiply(porcentajeDestare)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularBono(
            Boolean aplicaBono,
            Integer cantidad
    ) {

        if (!aplicaBono) {
            return BigDecimal.ZERO;
        }

        return BONO_POR_ANIMAL.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }

    @Override
    public BigDecimal calcularSubtotal(
            BigDecimal pesoNeto,
            BigDecimal precioPorKilo,
            BigDecimal valorBono
    ) {

        return pesoNeto
                .multiply(precioPorKilo)
                .subtract(valorBono)
                .setScale(2, RoundingMode.HALF_UP);
    }
}