package com.ganabascula.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ReporteResponseDto {

    private Long id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private BigDecimal totalVentas;

    private Integer totalTransacciones;
}