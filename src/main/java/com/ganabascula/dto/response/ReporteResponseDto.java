package com.ganabascula.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReporteResponseDto {

    private Long id;
    private LocalDateTime fechaGeneracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal totalVentas;
    private Integer totalAnimales;
    private Integer totalTransacciones;
}