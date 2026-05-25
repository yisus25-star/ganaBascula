package com.ganabascula.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MetricasAdminResponseDto {

    private Long ganaderosActivos;

    private Long ganaderosPendientes;

    private Long ganaderosRechazados;

    private Long totalVentas;

    private BigDecimal montoTotalTransado;
}